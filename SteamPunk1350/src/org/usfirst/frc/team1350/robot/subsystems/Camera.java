package org.usfirst.frc.team1350.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of OpenCV to do vision processing. The
 * image is acquired from the USB camera, then a rectangle is put on the image
 * and sent to the dashboard. OpenCV has many methods for different types of
 * processing.
 */
public class Camera extends IterativeRobot {
	Thread visionThread;

	private Mat hslThresholdOutput = new Mat();
	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
	private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

	// double[] center = new double[2];
	// int xCor = 0;

	private static Camera instance;

	public static Camera getInstance() {
		if (instance == null) {
			instance = new Camera();
		}
		return instance;
	}

	public void cameraInit() {
		visionThread = new Thread(() -> {

			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			// Set the resolution
			camera.setResolution(640, 480);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat. If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}

				// Step HSL_Threshold0:
				Mat hslThresholdInput = mat;
				double[] hslThresholdHue = { 60, 121.63822525597271 };
				double[] hslThresholdSaturation = { 87.14028776978417, 255.0 };
				double[] hslThresholdLuminance = { 126.12410071942446, 255.0 };
				hslThreshold(hslThresholdInput, hslThresholdHue, hslThresholdSaturation, hslThresholdLuminance,
						hslThresholdOutput);

				// Step Find_Contours0:
				Mat findContoursInput = hslThresholdOutput;
				boolean findContoursExternalOnly = false;
				findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

				// Step Filter_Contours0:
				ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
				double filterContoursMinArea = 40;
				double filterContoursMinPerimeter = 20;
				double filterContoursMinWidth = 10;
				double filterContoursMaxWidth = 1000;
				double filterContoursMinHeight = 10;
				double filterContoursMaxHeight = 1000;
				double[] filterContoursSolidity = { 68, 100 };
				double filterContoursMaxVertices = 50.0;
				double filterContoursMinVertices = 4;
				double filterContoursMinRatio = 0;
				double filterContoursMaxRatio = 1000;
				filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter,
						filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight,
						filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices,
						filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio,
						filterContoursOutput);

				for (int i = 0; i < filterContoursOutput.size(); i++) {
					MatOfPoint thisContour = filterContoursOutput.get(i);
					Rect bb = Imgproc.boundingRect(thisContour);

					Imgproc.rectangle(mat, new Point(bb.x, bb.y), new Point(bb.x + bb.width, bb.y + bb.height),
							new Scalar(255, 1, 1), 5);

					if (i == 0)
						SmartDashboard.putString("DB/String 0", "x= " + bb.x);

					// xCor += bb.x;
					// center[i] = bb.x + (bb.width / 2);

				}
				// xCor /= filterContoursOutput.size();

				// SmartDashboard.putString("DB/String 0", "XCor= " + xCor);

				// double mid = (center[0] + center[1]) / 2;

				// Imgproc.rectangle(mat, new Point(mid - 3, 0), new Point(mid +
				// 3, 640), new Scalar(255, 1, 1), 5);

				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
	}

	/**
	 * This method is a generated getter for the output of a HSL_Threshold.
	 * 
	 * @return Mat output from HSL_Threshold.
	 */
	public Mat hslThresholdOutput() {
		return hslThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * 
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public ArrayList<MatOfPoint> findContoursOutput() {
		return findContoursOutput;
	}

	/**
	 * This method is a generated getter for the output of a Filter_Contours.
	 * 
	 * @return ArrayList<MatOfPoint> output from Filter_Contours.
	 */
	public ArrayList<MatOfPoint> filterContoursOutput() {
		return filterContoursOutput;
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
	private void hslThreshold(Mat input, double[] hue, double[] sat, double[] lum, Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
		Core.inRange(out, new Scalar(hue[0], lum[0], sat[0]), new Scalar(hue[1], lum[1], sat[1]), out);
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
	private void findContours(Mat input, boolean externalOnly, List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		} else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);
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
	private void filterContours(List<MatOfPoint> inputContours, double minArea, double minPerimeter, double minWidth,
			double maxWidth, double minHeight, double maxHeight, double[] solidity, double maxVertexCount,
			double minVertexCount, double minRatio, double maxRatio, List<MatOfPoint> output) {
		final MatOfInt hull = new MatOfInt();
		output.clear();
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
			output.add(contour);
		}
	}

}
