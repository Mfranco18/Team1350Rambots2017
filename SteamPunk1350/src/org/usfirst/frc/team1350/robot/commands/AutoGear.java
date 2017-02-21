package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoGear extends Command {

	private DriveTrain drivetrain;

	static double pegPoint;

	public AutoGear() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(DriveTrain.getInstance());
		drivetrain = DriveTrain.getInstance();
	}

	public static void getX(double temp) {
		pegPoint = temp;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		pegPoint = 0;

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		SmartDashboard.putString("DB/String 9", "Peg Pos = " + pegPoint);
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
