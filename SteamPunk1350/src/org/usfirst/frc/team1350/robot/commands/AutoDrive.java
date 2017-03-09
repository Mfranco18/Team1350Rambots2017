package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1350.robot.subsystems.NavxMicro;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDrive extends Command {

	private double speed = 0.5;
	private double curve = 0.1;
	private int iterations = 0;
	private DriveTrain drivetrain;
	double angle;
	double tolerance = 3;
	int time = 2;

	protected AutoDrive() {
		requires(DriveTrain.getInstance());
		drivetrain = DriveTrain.getInstance();
	}

	protected void initialize() {
		setTimeout(time);
		angle = NavxMicro.getInstance().getHeading();

	}

	protected void execute() {
		iterations++;

		// if (NavxMicro.getInstance().getHeading() < angle + tolerance
		// && NavxMicro.getInstance().getHeading() > angle - tolerance) {
		// drivetrain.autoDrive(speed, 0);
		// } else if (NavxMicro.getInstance().getHeading() > angle + tolerance)
		// {
		// drivetrain.autoDrive(0, -0.5);
		// } else if (NavxMicro.getInstance().getHeading() < angle - tolerance)
		// {
		// drivetrain.autoDrive(0, 0.5);
		// }

		drivetrain.autoDrive(0.2, curve);
	}

	// Make this return false when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return isTimedOut();
		// return true;
	}

	// Called once after isFinished returns true
	protected void end() {
		SmartDashboard.putString("DB/String 0", "auto drive over");

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

}
