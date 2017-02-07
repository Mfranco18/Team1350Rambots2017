package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.OI;
import org.usfirst.frc.team1350.robot.subsystems.Climber;
import org.usfirst.frc.team1350.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimberControl extends Command {

	private static ClimberControl instance;
	public static ClimberControl getInstance(){
		if (instance==null){
			instance = new ClimberControl();
		}
		return instance;
	}

	//define variables
	private boolean squaredInputs;
	private final static double speed = 1;
	
	
	public ClimberControl() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		requires(Climber.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	squaredInputs = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Climber.getInstance().tankDrive(getXboxLeftStick(), getXboxLeftStick(), squaredInputs);
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
    
  //Called to get the xbox controller moment
    private static double getXboxLeftStick(){
    	return (OI.getInstance().XboxControllerLeft.getY()) * speed;
    }
}
