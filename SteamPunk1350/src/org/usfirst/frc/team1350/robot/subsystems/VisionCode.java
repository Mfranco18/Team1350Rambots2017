//first run of the vision code

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
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class VisionCode extends Subsystem {

	Thread visionThread;
	
	private double PegX;
	
	private double[] boxXDim = new double[1];
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public class Robot extends IterativeRobot {
    	Thread visionThread;
    	//use this joystick to control the place where the tape is 
    	Joystick stick;

    	@Override
    	public void robotInit() {
    		visionThread = new Thread(() -> {
    			// Get the UsbCamera from CameraServer
    			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    			// Set the resolution
    			camera.setResolution(640, 480);
    			
    			// defining the dio 
    			DigitalOutput light;
    			light = new DigitalOutput(0);
    			
    			

    			
    			CvSink cvSink = CameraServer.getInstance().getVideo();
    			CvSource outputStream = CameraServer.getInstance().putVideo("Vision Code", 640, 480);

    			// Mats are very memory expensive. Lets reuse this Mat.
    			Mat mat = new Mat();
    			//Mat matOff = new Mat();
    			//Mat matOutput = new Mat();
    			
    			//Outputs
    			//Mat processedMat = new Mat();
    			
    			//Outputs for contours
    			Mat hslThresholdOutput = new Mat();
    			ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    			ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
    			
    			//declare the array for the positions of the boxes
    			int[] pegPosX;
    			int pegX;
    			
    			//setting up an array for the distances 
    			double[] distances = new double[2];

    			
    			while (!Thread.interrupted()) {

    				
    				if (cvSink.grabFrame(mat) == 0) {
    					// Send the output the error.
    					outputStream.notifyError(cvSink.getError());
    					// skip the rest of the current iteration
    					continue;
    				}

    				// Code for the contours
    				// Step HSL_Threshold0:
    				Mat hslThresholdInput = mat;
    				double[] hslThresholdHue = {39, 96};
    				double[] hslThresholdSaturation = {48, 255.0};
    				double[] hslThresholdLuminance = {191, 255.0};
    				hslThreshold(hslThresholdInput, hslThresholdHue, hslThresholdSaturation, hslThresholdLuminance, hslThresholdOutput);

    				// Step Find_Contours0:
    				Mat findContoursInput = hslThresholdOutput;
    				boolean findContoursExternalOnly = false;
    				findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);
    				
    				// Step Filter_Contours0:
    				ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
    				double filterContoursMinArea = 150.0;
    				double filterContoursMinPerimeter = 0.0;
    				double filterContoursMinWidth = 0.0;
    				double filterContoursMaxWidth = 100;
    				double filterContoursMinHeight = 10.0;
    				double filterContoursMaxHeight = 640;
    				double[] filterContoursSolidity = {75, 100};
    				double filterContoursMaxVertices = 100.0;
    				double filterContoursMinVertices = 4;
    				double filterContoursMinRatio = 0;
    				double filterContoursMaxRatio = 1000;
    				filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);

    				 
    				for(int i = 0; i < filterContoursOutput.size(); i++){
    					
    				//for(int i = 0; i < 1; i++){
    					MatOfPoint thisContour = filterContoursOutput.get(i);
    					Rect bb = Imgproc.boundingRect(thisContour);
    					Imgproc.rectangle(mat, new Point(bb.x, bb.y), new Point(bb.x+bb.width, bb.y +bb.height),
    							new Scalar(1, 1, 1), 5);
    					
    					
    					int height = bb.height;
    					double distanceH = 0;
    					distanceH = (3327.56/(height))-0.1898 + 2;
    					
    					//distances = new double[2];
    					distances[i] = distanceH;
    					
    					//put the x dimmension of the rectangle into an array
    					boxXDim[i] = bb.x + 0.5*(bb.width);
    					
    					
    					if (i ==0){
    						//int height = bb.height;
    						int width = bb.width;
    						SmartDashboard.putString("DB/String 1", "Width " + height);
    						SmartDashboard.putString("DB/String 2", "Height " + width);
    						
    						//double distanceH = 0;
    						distanceH = (3327.56/(height))-0.1898 + 2;
    						SmartDashboard.putString("DB/String 3", "distance(h) = " + (int) distanceH);
    						
    						double distanceW = 0;
    						distanceW = (1191.63/(width))+5.1;
    						SmartDashboard.putString("DB/String 4", "distance(w) = " + (int) width);
    					}
    					
    				}
    				
    				for (int j = 0; j < 2; j++ ){
    					PegX += boxXDim[j];
    				}
    				
    				PegX = PegX/2;
    				
    				// Give the output stream a new image to display
    				//outputStream.putFrame(mat);
    				
    			}
    		});
    		visionThread.setDaemon(true);
    		visionThread.start();
    		
    		}
    	
    
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
private void hslThreshold(Mat input, double[] hue, double[] sat, double[] lum,
	Mat out) {
	Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
	Core.inRange(out, new Scalar(hue[0], lum[0], sat[0]),
		new Scalar(hue[1], lum[1], sat[1]), out);
}



/**
 * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
 * @param input The image on which to perform the Distance Transform.
 * @param type The Transform.
 * @param maskSize the size of the mask.
 * @param output The image in which to store the output.
 */
private void findContours(Mat input, boolean externalOnly,
	List<MatOfPoint> contours) {
	Mat hierarchy = new Mat();
	contours.clear();
	int mode;
	if (externalOnly) {
		mode = Imgproc.RETR_EXTERNAL;
	}
	else {
		mode = Imgproc.RETR_LIST;
	}
	int method = Imgproc.CHAIN_APPROX_SIMPLE;
	Imgproc.findContours(input, contours, hierarchy, mode, method);
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
private void filterContours(List<MatOfPoint> inputContours, double minArea,
	double minPerimeter, double minWidth, double maxWidth, double minHeight, double
	maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
	minRatio, double maxRatio, List<MatOfPoint> output) {
	final MatOfInt hull = new MatOfInt();
	output.clear();
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
		output.add(contour);
	}
}


}


