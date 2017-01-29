package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.subsystems.Lifter;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LifterDrive extends Command {

	
	private static TeleOpDriveTrain instance;
	public static TeleOpDriveTrain getInstance(){
		if (instance==null){
			instance = new TeleOpDriveTrain();
		}
		return instance;
	}
	
    public LifterDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Lifter.getInstance());
    }
    
    //Defenition of variables 
    private boolean squaredInputs;
	private final static double speed = 1;

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
