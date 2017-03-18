package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1350.robot.subsystems.NavxMicro;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDrive1 extends Command {

	private double speed = 0.4;
	private double curve = -0.04;
	private int iterations = 0;
	private DriveTrain drivetrain;
	double angle;
	double tolerance = 3;
	double time = 2.4;

	protected AutoDrive1() {
		requires(DriveTrain.getInstance());
		drivetrain = DriveTrain.getInstance();
	}

	protected void initialize() {
		setTimeout(time);
		angle = NavxMicro.getInstance().getHeading();

	}

	protected void execute() {
		iterations++;

		drivetrain.autoDrive(0.3, 0);
		drivetrain.autoDrive(0.3, 0);
		// drivetrain.tankDrive(0.4, 0.4, false);
		drivetrain.tankDrive(0.3, -0.4, false);

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
