package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.subsystems.Climber;
import org.usfirst.frc.team1350.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class IntakeControl extends Command {

	private static IntakeControl instance;
	public static IntakeControl getInstance(){
		if (instance==null){
			instance = new IntakeControl();
		}
		return instance;
	}
	
	
	//define variables
	private boolean squaredInputs;
	private final static double speed = 1;
	
	
    public IntakeControl() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Intake.getInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	squaredInputs = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	
    	if (SmartDashboard.getBoolean("DB/Button 3", false)){
    		Intake.getInstance().tankDrive(speed, speed, squaredInputs);
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
}
