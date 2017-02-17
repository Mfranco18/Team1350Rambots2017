package org.usfirst.frc.team1350.robot.commands;

//import org.usfirst.frc.team1350.robot.Log;
import org.usfirst.frc.team1350.robot.OI;
import org.usfirst.frc.team1350.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1350.robot.subsystems.NavxMicro;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TeleOpDriveTrain extends Command {

	
	private static TeleOpDriveTrain instance;
	public static TeleOpDriveTrain getInstance(){
		if (instance==null){
			instance = new TeleOpDriveTrain();
		}
		return instance;
	}

	//define variables
	private boolean squaredInputs;
	private final static double speed = 1;
	
    public TeleOpDriveTrain() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(DriveTrain.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	squaredInputs = false;
    	//SmartDashboard.putString("DB/LED 0", "TeleOpDrive is init");
    }
    

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    
    	
    	//checks to see if the trigger is pressed, and sends diff variables accordingly 
    	
    	if(DriveTrain.getInstance().orientationTriggerGet()){
    		DriveTrain.getInstance().tankDrive(-getRightStick(), -getLeftStick(), squaredInputs);
    	} else {
    		DriveTrain.getInstance().tankDrive(getLeftStick(), getRightStick(), squaredInputs);
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    //Called to give a double for the remotes 
    private static double getLeftStick(){
    	return (OI.getInstance().leftStick.getY()) * speed;
    }
    
    private static double getRightStick(){
    	return (OI.getInstance().rightStick.getY()) * speed;
    }
    
}
