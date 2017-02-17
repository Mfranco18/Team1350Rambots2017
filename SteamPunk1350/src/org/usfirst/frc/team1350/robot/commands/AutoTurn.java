package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1350.robot.subsystems.NavxMicro;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoTurn extends Command {
	private double speed = 0.25;
	private double curve = 0.25;
	private boolean AngleReached;
	private int iterations = 0; 
	private DriveTrain drivetrain;
	private double goalAngle;
	double temp, Angle;
	double turnAmount = 45.0;
	double tolerance = 1.5;
	
	
	protected AutoTurn(){
		requires(DriveTrain.getInstance());
		drivetrain = DriveTrain.getInstance();
		
	}
	
	protected void initialize(){
		setTimeout(4);
		Angle = NavxMicro.getInstance().getHeading();
		if (Angle > turnAmount){
			goalAngle = Angle-turnAmount;
		} else{
			goalAngle = 360+(turnAmount-Angle);
		}
		
		AngleReached = false;
		
		
		
	}
	
	protected void execute() {
		//boolean autoDriveOver = DriveTrain.getInstance().autoDrive(speed, iterations);
		//drivetrain.autoDrive(0, 1);
		//drivetrain.tankDrive(-0.25, 0.25, false);
		drivetrain.AutoTurn(speed, 1);
    }

    // Make this return false when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if ( NavxMicro.getInstance().getHeading() < goalAngle+tolerance &&  NavxMicro.getInstance().getHeading()> goalAngle-tolerance){
			AngleReached = true;
		}
    	return  AngleReached;
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

