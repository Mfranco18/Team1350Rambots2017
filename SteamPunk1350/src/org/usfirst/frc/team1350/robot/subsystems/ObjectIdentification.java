package org.usfirst.frc.team1350.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team1350.robot.commands.AutoGear;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
*
*/
public class ObjectIdentification extends Subsystem {

	private static ObjectIdentification instance;

	public static ObjectIdentification getInstance() {
		if (instance == null) {
			instance = new ObjectIdentification();
		}
		return instance;

	}

	public void init() {
		// Props

		double minHue = 65, maxHue = 114;
		double minSat = 135, maxSat = 255;
		double minValue = 155, maxValue = 255;

		double[] hslThresholdHue = { 45, 122 };
		double[] hslThresholdSaturation = { 87, 255.0 };
		double[] hslThresholdLuminance = { 112, 255.0 };

		double filterContoursMinArea = 20.0;
		double filterContoursMinPerimeter = 8.0;
		double filterContoursMinWidth = 0.0;
		double filterContoursMaxWidth = 100;
		double filterContoursMinHeight = 20.0;
		double filterContoursMaxHeight = 640;
		double[] filterContoursSolidity = { 40, 100 };
		double filterContoursMaxVertices = 50.0;
		double filterContoursMinVertices = 4;
		double filterContoursMinRatio = 0;
		double filterContoursMaxRatio = 1000;

		//

		// TODO tell marco. I basically did nothing but clear out all excess
		// camera stuff, simple thread, new cable, unthrottled router for dev,
		// TODO the identification pipline needs to be modified to actually find
		// & move the bot.
		// TODO may want to look at find lines rather than find contours.
		new Thread(() -> {
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			int resX = 320, resY = 240;
			camera.setResolution(resX, resY);
			camera.setFPS(30);

			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Object Id", resX, resY);

			// TODO show how I was able to use a single mat object to save
			// memory
			Mat image = new Mat();
			while (true) {
				if (cvSink.grabFrame(image) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}

				// TODO show marco I changed from hsl to hsv transform(grip
				// testing showed more promise with hsv)
				// image = hsvThreshold(image, minHue, minSat, minValue, maxHue,
				// maxSat, maxValue);
				image = hslThreshold(image, hslThresholdHue, hslThresholdSaturation, hslThresholdLuminance);

				boolean findContoursExternalOnly = false;
				List<MatOfPoint> findContoursOutput = findContours(image, findContoursExternalOnly);
				List<MatOfPoint> filterContoursContours = filterContours(findContoursOutput, filterContoursMinArea,
						filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth,
						filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity,
						filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio,
						filterContoursMaxRatio);

				double center = XPos(filterContoursContours);

				outputStream.putFrame(image);
			}
		}).start();
	}

	public double XPos(List<MatOfPoint> filterContoursContours) {
		double temp = 0;
		for (int i = 0; i <= 1; i++) {
			MatOfPoint thisContour = filterContoursContours.get(i);
			Rect bb = Imgproc.boundingRect(thisContour);
			if (i == 0) {
				SmartDashboard.putString("DB/String 4", "XPos = " + bb.x);
				temp = bb.x;
			}
		}
		AutoGear.getX(temp);
		SmartDashboard.putString("DB/String 8", "Peg Pos = " + temp);
		// placePos(temp);
		return temp;
	}

	/**
	 * Filters out contours that do not meet certain criteria.
	 * 
	 * @param inputContours
	 *            is the input list of contours
	 * @param output
	 *            is the the output list of contours
	 * @param minArea
	 *            is the minimum area of a contour that will be kept
	 * @param minPerimeter
	 *            is the minimum perimeter of a contour that will be kept
	 * @param minWidth
	 *            minimum width of a contour
	 * @param maxWidth
	 *            maximum width
	 * @param minHeight
	 *            minimum height
	 * @param maxHeight
	 *            maximimum height
	 * @param Solidity
	 *            the minimum and maximum solidity of a contour
	 * @param minVertexCount
	 *            minimum vertex Count of the contours
	 * @param maxVertexCount
	 *            maximum vertex Count
	 * @param minRatio
	 *            minimum ratio of width to height
	 * @param maxRatio
	 *            maximum ratio of width to height
	 */
	private ArrayList<MatOfPoint> filterContours(List<MatOfPoint> inputContours, double minArea, double minPerimeter,
			double minWidth, double maxWidth, double minHeight, double maxHeight, double[] solidity,
			double maxVertexCount, double minVertexCount, double minRatio, double maxRatio) {
		final MatOfInt hull = new MatOfInt();
		ArrayList<MatOfPoint> matOfP = new ArrayList<MatOfPoint>();

		// operation
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			final Rect bb = Imgproc.boundingRect(contour);
			if (bb.width < minWidth || bb.width > maxWidth)
				continue;
			if (bb.height < minHeight || bb.height > maxHeight)
				continue;
			final double area = Imgproc.contourArea(contour);
			if (area < minArea)
				continue;
			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter)
				continue;
			Imgproc.convexHull(contour, hull);
			MatOfPoint mopHull = new MatOfPoint();
			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++) {
				int index = (int) hull.get(j, 0)[0];
				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1] };
				mopHull.put(j, 0, point);
			}
			final double solid = 100 * area / Imgproc.contourArea(mopHull);
			if (solid < solidity[0] || solid > solidity[1])
				continue;
			if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)
				continue;
			final double ratio = bb.width / (double) bb.height;
			if (ratio < minRatio || ratio > maxRatio)
				continue;
			matOfP.add(contour);
		}

		return matOfP;
	}

	/**
	 * Sets the values of pixels in a binary image to their distance to the
	 * nearest black pixel.
	 * 
	 * @param input
	 *            The image on which to perform the Distance Transform.
	 * @param type
	 *            The Transform.
	 * @param maskSize
	 *            the size of the mask.
	 * @param output
	 *            The image in which to store the output.
	 */
	private List<MatOfPoint> findContours(Mat input, boolean externalOnly) {
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		int mode;

		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		} else {
			mode = Imgproc.RETR_LIST;
		}
		// TODO show how this was a destructive method call that modified the
		// image. I copied it to see the hsl transform for debugging. We don't
		// need to clone ot here
		// TOOD remove for speed perfs
		Mat test = input.clone();
		Imgproc.findContours(test, contours, hierarchy, mode, Imgproc.CHAIN_APPROX_SIMPLE);

		return contours;
	}

	/**
	 * Segment an image based on hue, saturation, and luminance ranges.
	 *
	 * @param input
	 *            The image on which to perform the HSL threshold.
	 * @param hue
	 *            The min and max hue
	 * @param sat
	 *            The min and max saturation
	 * @param lum
	 *            The min and max luminance
	 * @param output
	 *            The image in which to store the output.
	 */
	// TODO tell marco to not use the array as a way of passoing more data like
	// that to the method, confusing and hard to read, tech debt
	private Mat hsvThreshold(Mat input, double minHue, double minSat, double minValue, double maxHue, double maxSat,
			double maxValue) {
		// TODO Show marco you are allowed to pass the same source and dest mat,
		// saves memory & improves perf
		// TODO show how its still needed to return the input as convention
		Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2HSV);
		Core.inRange(input, new Scalar(minHue, minSat, minValue), new Scalar(maxHue, maxSat, maxValue), input);
		return input;
	}

	/**
	 * Segment an image based on hue, saturation, and luminance ranges.
	 *
	 * @param input
	 *            The image on which to perform the HSL threshold.
	 * @param hue
	 *            The min and max hue
	 * @param sat
	 *            The min and max saturation
	 * @param lum
	 *            The min and max luminance
	 * @param output
	 *            The image in which to store the output.
	 */
	private Mat hslThreshold(Mat input, double[] hue, double[] sat, double[] lum) {
		Mat convertedMat = new Mat();
		Imgproc.cvtColor(input, convertedMat, Imgproc.COLOR_BGR2HLS);
		Mat clampedMap = new Mat();
		Core.inRange(convertedMat, new Scalar(hue[0], lum[0], sat[0]), new Scalar(hue[1], lum[1], sat[1]), clampedMap);
		return clampedMap;
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}