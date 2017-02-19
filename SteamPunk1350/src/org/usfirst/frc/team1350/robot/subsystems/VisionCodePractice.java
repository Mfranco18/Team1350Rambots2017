package org.usfirst.frc.team1350.robot.subsystems;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.opencv.imgproc.Imgproc;
//import org.usfirst.frc.team1350.VisionTest.VisionTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;



/**
 * This is a demo program showing the use of OpenCV to do vision processing. The
 * image is acquired from the USB camera, then a rectangle is put on the image and
 * sent to the dashboard. OpenCV has many methods for different types of
 * processing.
 */
public class VisionCodePractice extends IterativeRobot {
	Thread visionThread;
	//use this joystick to control the place where the tape is 
	Joystick stick;

	@Override
	public void robotInit() {
		visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			// Set the resolution
			camera.setResolution(320, 240);
			//camera.setResolution(640, 480);
			
			//Defenitions for the joystick and its x.y magnitudes 
			stick = new Joystick(0);
			double xaxis, yaxis;
			
			// defining the dio 
			//DigitalOutput light;
			//light = new DigitalOutput(0);
			
			

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Object Identification", 320, 240);
			//CvSource outputStream = CameraServer.getInstance().putVideo("Object Identification", 640, 480);
			
			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();
			
			//Outputs for contours
			ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
			
			//declare the array for the positions of the boxes
			int[] pegPosX;
			int pegX;
			
			//setting up an array for the distances 
			double[] distances = new double[2];

			
			/*
			if(true){
				yaxis = stick.getY();
				SmartDashboard.putString("DB/String 0", "XAxis " + yaxis);
			}
			*/

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				
				//called everytime to find the new positions of the joysticks 
				xaxis = stick.getX();
				yaxis = stick.getY();
				SmartDashboard.putString("DB/String 0", "XAxis " + yaxis);
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				
				//light.set(true);
				//delay();
				
				//Timer.delay(0.1);
				//cvSink.grabFrame(matOff);

				//light.set(false);
				//Timer.delay(0.1);
				
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				// Put a rectangle on the image
				/*Imgproc.rectangle(mat, new Point(xaxis*100, yaxis*100), new Point(xaxis*100+200, yaxis*100 + 200),
						new Scalar(255, 1, 1), 5);
				*/
				//Imgproc.cvtColor(mat, processedMat, Imgproc.COLOR_BGR2GRAY);
				
				
				//Core.absdiff(mat, matOff, matOutput);
			

				// Code for the contours
				// Step HSL_Threshold0:
				double[] hslThresholdHue = {45, 122};
				double[] hslThresholdSaturation = {87, 255.0};
				double[] hslThresholdLuminance = {112, 255.0};
				mat = hslThreshold(mat, hslThresholdHue, hslThresholdSaturation, hslThresholdLuminance);
//
////				// Step Find_Contours0:
//				boolean findContoursExternalOnly = false;
//				List<MatOfPoint> findContoursOutput = findContours(mat, findContoursExternalOnly);
//
////				
////				// Step Filter_Contours0:
//				double filterContoursMinArea = 150.0;
//				double filterContoursMinPerimeter = 0.0;
//				double filterContoursMinWidth = 0.0;
//				double filterContoursMaxWidth = 100;
//				double filterContoursMinHeight = 10.0;
//				double filterContoursMaxHeight = 640;
//				double[] filterContoursSolidity = {75, 100};
//				double filterContoursMaxVertices = 100.0;
//				double filterContoursMinVertices = 4;
//				double filterContoursMinRatio = 0;
//				double filterContoursMaxRatio = 1000;
//				List<MatOfPoint> filterContoursContours = filterContours(findContoursOutput, filterContoursMinArea, 
//						filterContoursMinPerimeter, filterContoursMinWidth, 
//						filterContoursMaxWidth, filterContoursMinHeight, 
//						filterContoursMaxHeight, filterContoursSolidity, 
//						filterContoursMaxVertices, filterContoursMinVertices, 
//						filterContoursMinRatio, filterContoursMaxRatio);

//				 
//				for(int i = 0; i < filterContoursOutput.size(); i++){
//					
//				//for(int i = 0; i < 1; i++){
//					MatOfPoint thisContour = filterContoursOutput.get(i);
//					Rect bb = Imgproc.boundingRect(thisContour);
//					Imgproc.rectangle(mat, new Point(bb.x, bb.y), new Point(bb.x+bb.width, bb.y +bb.height),
//							new Scalar(1, 1, 1), 5);
//					
//					
//					int height = bb.height;
//					double distanceH = 0;
//					distanceH = (3327.56/(height))-0.1898 + 2;
//					
//					//distances = new double[2];
//					distances[i] = distanceH;
//					
//					
//					if (i ==0){
//						//int height = bb.height;
//						int width = bb.width;
//						SmartDashboard.putString("DB/String 1", "Width " + height);
//						SmartDashboard.putString("DB/String 2", "Height " + width);
//						
//						//double distanceH = 0;
//						distanceH = (3327.56/(height))-0.1898 + 2;
//						SmartDashboard.putString("DB/String 3", "distance(h) = " + (int) distanceH);
//						
//						double distanceW = 0;
//						distanceW = (1191.63/(width))+5.1;
//						SmartDashboard.putString("DB/String 4", "distance(w) = " + (int) width);
//					}
//					
//					/*if (i == 1){
//						SmartDashboard.putString("DB/String 3", "Top " + bb.y);
//						SmartDashboard.putString("DB/String 4", "Height " + bb.height);
//					}
//					*/
//					
//					 
//					
//				}
				
				
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
				//outputStream.
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
		
		}
	
	
	/**
	 * Segment an image based on hue, saturation, and luminance ranges.
	 *
	 * @param input The image on which to perform the HSL threshold.
	 * @param hue The min and max hue
	 * @param sat The min and max saturation
	 * @param lum The min and max luminance
	 * @param output The image in which to store the output.
	 */
	private Mat hslThreshold(Mat input, double[] hue, double[] sat, double[] lum) {
		Mat convertedMat = new Mat();
		Imgproc.cvtColor(input, convertedMat, Imgproc.COLOR_BGR2HLS);
		Mat clampedMap = new Mat();
		Core.inRange(convertedMat, new Scalar(hue[0], lum[0], sat[0]),
			new Scalar(hue[1], lum[1], sat[1]), clampedMap);
		return clampedMap;
	}
	
	
	
	/**
	 * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
	 * @param input The image on which to perform the Distance Transform.
	 * @param type The Transform.
	 * @param maskSize the size of the mask.
	 * @param output The image in which to store the output.
	 */
	private List<MatOfPoint> findContours(Mat input, boolean externalOnly) {
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		int mode;
		
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		}
		else {
			mode = Imgproc.RETR_LIST;
		}
		
		Imgproc.findContours(input, contours, hierarchy, mode, Imgproc.CHAIN_APPROX_SIMPLE);
		
		return contours;
	}
	
	/**
	 * Filters out contours that do not meet certain criteria.
	 * @param inputContours is the input list of contours
	 * @param output is the the output list of contours
	 * @param minArea is the minimum area of a contour that will be kept
	 * @param minPerimeter is the minimum perimeter of a contour that will be kept
	 * @param minWidth minimum width of a contour
	 * @param maxWidth maximum width
	 * @param minHeight minimum height
	 * @param maxHeight maximimum height
	 * @param Solidity the minimum and maximum solidity of a contour
	 * @param minVertexCount minimum vertex Count of the contours
	 * @param maxVertexCount maximum vertex Count
	 * @param minRatio minimum ratio of width to height
	 * @param maxRatio maximum ratio of width to height
	 */
	private ArrayList<MatOfPoint> filterContours(List<MatOfPoint> inputContours, double minArea,
		double minPerimeter, double minWidth, double maxWidth, double minHeight, double
		maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
		minRatio, double maxRatio){
		final MatOfInt hull = new MatOfInt();
		ArrayList<MatOfPoint> matOfP = new ArrayList<MatOfPoint>();
		
		//operation
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			final Rect bb = Imgproc.boundingRect(contour);
			if (bb.width < minWidth || bb.width > maxWidth) continue;
			if (bb.height < minHeight || bb.height > maxHeight) continue;
			final double area = Imgproc.contourArea(contour);
			if (area < minArea) continue;
			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
			Imgproc.convexHull(contour, hull);
			MatOfPoint mopHull = new MatOfPoint();
			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++) {
				int index = (int)hull.get(j, 0)[0];
				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
				mopHull.put(j, 0, point);
			}
			final double solid = 100 * area / Imgproc.contourArea(mopHull);
			if (solid < solidity[0] || solid > solidity[1]) continue;
			if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
			final double ratio = bb.width / (double)bb.height;
			if (ratio < minRatio || ratio > maxRatio) continue;
			matOfP.add(contour);
		}
		
		return matOfP;
	}
	
}


/*
  distances[2] = 8.5;
				
				double angle = Math.acos((distances[2]*distances[2])- (distances[1]*distances[1]-(distances[0]*distances[0])/(-2*distances[0]*distances[1]));
				
				
				*/
