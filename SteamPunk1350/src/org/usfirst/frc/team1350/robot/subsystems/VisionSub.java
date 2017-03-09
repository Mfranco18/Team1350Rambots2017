package org.usfirst.frc.team1350.robot.subsystems;

import java.util.ArrayList;
import java.util.Arrays;
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
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class VisionSub extends Subsystem {

	private DriveTrain drivetrain;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	private static VisionSub instance;

	public static VisionSub getInstance() {
		if (instance == null) {
			instance = new VisionSub();
		}
		return instance;
	}

	// Thread visionThread;

	private Mat hslThresholdOutput = new Mat();
	private Mat hsvThresholdOutput = new Mat();
	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
	private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

	// double[] center = new double[2];
	// int xCor = 0;

	public boolean process() {

		// RobotDrive myRobot;
		// myRobot = new RobotDrive(0, 1);

		drivetrain = DriveTrain.getInstance();

		// visionThread = new Thread(() -> {

		int diff = 0;
		int prevDiff = 0;

		MatOfPoint thisContour;

		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();

		// Get the UsbCamera from CameraServer
		// UsbCamera camera =
		CameraServer.getInstance().startAutomaticCapture();
		// Set the resolution
		camera.setResolution(640, 480);
		// int dashData0 = (int) SmartDashboard.getNumber("DB/Slider 0",
		// 0.0);
		// camera.setExposureManual(dashData0);
		// int dashData0 = (int) SmartDashboard.getNumber("DB/Slider 0",
		// 0.0);
		camera.setExposureManual(80);

		// Get a CvSink. This will capture Mats from the camera
		CvSink cvSink = CameraServer.getInstance().getVideo();
		// Setup a CvSource. This will send images back to the Dashboard
		CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

		// Mats are very memory expensive. Lets reuse this Mat.
		Mat mat = new Mat();

		int j = 0;
		// This cannot be 'true'. The program will never exit if it is.
		// This
		// lets the robot stop this thread when restarting robot code or
		// deploying.
		// while (!Thread.interrupted()) {

		// Tell the CvSink to grab a frame from the camera and put
		// it
		// in the source mat. If there is an error notify the
		// output.
		if (cvSink.grabFrame(mat) == 0) {
			// Send the output the error.
			outputStream.notifyError(cvSink.getError());
			// skip the rest of the current iteration
			// continue;
		}

		// Step HSV_Threshold0:
		Mat hsvThresholdInput = mat;
		double[] hsvThresholdHue = { 42.086330935251794, 137.95904436860067 };
		double[] hsvThresholdSaturation = { 0, 255 };
		double[] hsvThresholdValue = { 204.09172661870505, 255.0 };
		hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

		// Step Find_Contours0:
		Mat findContoursInput = hsvThresholdOutput;
		boolean findContoursExternalOnly = false;
		findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

		// Step Filter_Contours0:
		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
		double filterContoursMinArea = 40;
		double filterContoursMinPerimeter = 20;
		double filterContoursMinWidth = 10;
		double filterContoursMaxWidth = 1000;
		double filterContoursMinHeight = 20;
		double filterContoursMaxHeight = 1000;
		double[] filterContoursSolidity = { 68, 100 };
		double filterContoursMaxVertices = 50.0;
		double filterContoursMinVertices = 4;
		double filterContoursMinRatio = 0;
		double filterContoursMaxRatio = 1000;
		filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter,
				filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight,
				filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio,
				filterContoursMaxRatio, filterContoursOutput);

		int distanceH = 0;
		int height = 0;
		int[] xArray = new int[5];
		int[] yArray = new int[5];
		int[] widthArray = new int[5];
		int[] heightArray = new int[5];

		int goodRectCounter = 0;

		for (int i = 0; i < xArray.length; i++) {
			xArray[i] = 0;
			yArray[i] = 0;
			widthArray[i] = 0;
			heightArray[i] = 0;
		}

		for (int i = 0; i < filterContoursOutput.size(); i++) {

			thisContour = filterContoursOutput.get(i);
			Rect bb = Imgproc.boundingRect(thisContour);

			if (bb.height < 2 * bb.width) {
				// Imgproc.rectangle(mat, new Point(bb.x, bb.y), new
				// Point(bb.x + bb.width, bb.y + bb.height),
				// new Scalar(1, 1, 255), 5);

			} else {
				// Imgproc.rectangle(mat, new Point(bb.x, bb.y), new
				// Point(bb.x + bb.width, bb.y + bb.height),
				// new Scalar(255, 1, 1), 5);
				heightArray[i] = bb.height;
				widthArray[i] = bb.width;
				xArray[i] = bb.x;
				yArray[i] = bb.y;
				goodRectCounter++;

			}
		}

		SmartDashboard.putString("DB/String 9", "goodRect  " + goodRectCounter);

		// variables to keep track of the array index
		int biggestIndex1 = 0, biggestIndex2 = 0;
		int[] arrayCopy = new int[5];

		prevDiff = diff;

		if (goodRectCounter == 1) {

			thisContour = filterContoursOutput.get(0);
			Rect bb = Imgproc.boundingRect(thisContour);

			Imgproc.rectangle(mat, new Point(bb.x, bb.y), new Point(bb.x + bb.width, bb.y + bb.height),
					new Scalar(1, 1, 1), 5);
			height = bb.height;
			distanceH = (int) (3327.56 / bb.height - 0.1898 + 2);
			diff = (bb.x + bb.width / 2) - 320;

			if (!(diff > prevDiff - 20 || diff < prevDiff + 20)) {
				diff = prevDiff;
			}

			SmartDashboard.putString("DB/String 2", "diff " + diff);
			SmartDashboard.putString("DB/String 3", "height " + height);
			SmartDashboard.putString("DB/String 4", "distance " + distanceH);

		} else {
			SmartDashboard.putString("DB/String 2", "diff " + diff);
			SmartDashboard.putString("DB/String 3", "height " + height);
			SmartDashboard.putString("DB/String 4", "distance " + distanceH);

			// find the two biggest
			arrayCopy = heightArray.clone();
			Arrays.sort(arrayCopy);

			for (int i = 0; i < arrayCopy.length; i++) {
				if (heightArray[i] == arrayCopy[4]) {
					biggestIndex1 = i;
				}
				if (heightArray[i] == arrayCopy[3]) {
					biggestIndex2 = i;
				}
			}

			// thisContour =
			// filterContoursOutput.get(biggestIndex1);
			// Rect bb1 = Imgproc.boundingRect(thisContour);
			//
			// thisContour =
			// filterContoursOutput.get(biggestIndex2);
			// Rect bb2 = Imgproc.boundingRect(thisContour);

			Rect bb1 = new Rect(xArray[biggestIndex1], yArray[biggestIndex1], widthArray[biggestIndex1],
					heightArray[biggestIndex1]);
			Imgproc.rectangle(mat, new Point(bb1.x, bb1.y), new Point(bb1.x + bb1.width, bb1.y + bb1.height),
					new Scalar(1, 1, 1), 5);

			Rect bb2 = new Rect(xArray[biggestIndex2], yArray[biggestIndex2], widthArray[biggestIndex2],
					heightArray[biggestIndex2]);
			Imgproc.rectangle(mat, new Point(bb2.x, bb2.y), new Point(bb2.x + bb2.width, bb2.y + bb2.height),
					new Scalar(1, 1, 1), 5);

			if (((bb1.y - bb2.y) > -10 && (bb1.y - bb2.y) < 10)) {
				diff = ((bb1.x + bb1.width / 2) + (bb2.x + bb2.width / 2)) / 2 - 320;

				// if (!(diff > prevDiff - 10 && diff < prevDiff +
				// 10))
				// {
				// diff = prevDiff;
				// }

				distanceH = (int) ((3327.56 / bb1.height - 0.1898 + 2) + (3327.56 / bb2.height - 0.1898 + 2)) / 2;
				SmartDashboard.putString("DB/String 2", "diff " + diff);
				// SmartDashboard.putString("DB/String 3", "height "
				// +
				// height);
				SmartDashboard.putString("DB/String 4", "distance " + distanceH);
			}

		}

		double speedR = 0;
		double speedL = 0;

		// Timer.delay(0.2);

		if (diff > 30) {
			speedL = -0.3;
			speedR = 0.3;
		} else if (diff < -30) {
			speedL = 0.3;
			speedR = -0.3;
		} else {
			if (distanceH < 20) {
				speedL = 0;
				speedR = 0;
			} else if (distanceH > 50) {
				speedL = 0.5;
				speedR = 0.5;
			} else {
				// speedL = 0.5 / 35 * distanceH - 0.3;
				// speedR = 0.5 / 35 * distanceH - 0.3;
				speedL = 0.5;
				speedR = 0.5;
			}
		}

		// if (distanceH > 20) {
		// speedR = 0.4;
		// speedL = 0.4;
		// if (diff > 20) {
		// speedL = -0.3;
		// speedR = 0.3;
		// } else if (diff < -20) {
		// speedL = 0.3;
		// speedR = -0.3;
		// }
		// } else {
		// speedR = 0;
		// speedL = 0;
		// }

		// myRobot.tankDrive(speedL, speedR);

		if (distanceH > 15) {
			speedL = 0.3;
		}

		if (diff > 20 || diff < -20) {
			speedR = diff / 1000;
			speedL = 0;
		}

		DriveTrain.getInstance().autoDrive(speedL, speedR);

		SmartDashboard.putString("DB/String 1", "num  rect = " + filterContoursOutput.size());
		outputStream.putFrame(mat);
		if ((diff < 20 && diff > -20) && distanceH < 20) {
			return false;
		} else {
			return true;
		}

	}
	// });
	// visionThread.setDaemon(true);
	// visionThread.start();
	// }

	/**
	 * This method is a generated getter for the output of a HSL_Threshold.
	 * 
	 * @return Mat output from HSL_Threshold.
	 */
	public Mat hslThresholdOutput() {
		return hslThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a HSV_Threshold.
	 * 
	 * @return Mat output from HSV_Threshold.
	 */
	public Mat hsvThresholdOutput() {
		return hsvThresholdOutput;
	}

	/**
	 * Segment an image based on hue, saturation, and value ranges.
	 *
	 * @param input
	 *            The image on which to perform the HSL threshold.
	 * @param hue
	 *            The min and max hue
	 * @param sat
	 *            The min and max saturation
	 * @param val
	 *            The min and max value
	 * @param output
	 *            The image in which to store the output.
	 */
	private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val, Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
		Core.inRange(out, new Scalar(hue[0], sat[0], val[0]), new Scalar(hue[1], sat[1], val[1]), out);
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

	// TODO Auto-generated method stub

}
