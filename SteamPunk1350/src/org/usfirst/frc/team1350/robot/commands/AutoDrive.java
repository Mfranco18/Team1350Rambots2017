package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDrive extends Command {

	private double speed = 0.4;
	private double curve = 0.25;
	private boolean autoDriveOver;
	private int iterations = 0; 
	private DriveTrain drivetrain;
	
	
	protected AutoDrive(){
		requires(DriveTrain.getInstance());
		drivetrain = DriveTrain.getInstance();
	}
	
	protected void initialize(){
		autoDriveOver = true;
		setTimeout(6);
		
		
	}
	
	protected void execute() {
		//boolean autoDriveOver = DriveTrain.getInstance().autoDrive(speed, iterations);
		iterations++;
		drivetrain.autoDrive(speed, 0);
		
		
		
		SmartDashboard.putString("DB/String 5", "autodrive is called");
		SmartDashboard.putString("DB/String 4", "lmyo " + autoDriveOver);
    	
    }

    // Make this return false when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return  isTimedOut();
    	//return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
	

}
