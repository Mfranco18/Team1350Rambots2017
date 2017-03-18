package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1350.robot.util.VisionThread;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoPlace extends Command {

	private DriveTrain drivetrain;
	int time, distanceH, diff;
	double speedL, speedR, prevSpeedL, prevSpeedR;
	VisionThread visionThread;

	public AutoPlace(VisionThread thread) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(DriveTrain.getInstance());
		drivetrain = DriveTrain.getInstance();
		this.visionThread = thread;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		time = 8;
		setTimeout(time);
		prevSpeedL = 0;
		prevSpeedR = 0;
		speedL = 0;
		speedR = 0;
		SmartDashboard.putString("DB/String 0", "");
		SmartDashboard.putString("DB/String 1", "");
		SmartDashboard.putString("DB/String 2", "");
		SmartDashboard.putString("DB/String 3", "");
		SmartDashboard.putString("DB/String 4", "");
		SmartDashboard.putString("DB/String 5", "");
		SmartDashboard.putString("DB/String 6", "");
		SmartDashboard.putString("DB/String 7", "");
		SmartDashboard.putString("DB/String 8", "");
		SmartDashboard.putString("DB/String 9", "");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// thread
		diff = visionThread.getDiff();

		// variable which is the last known location of the contours
		distanceH = visionThread.getDistance();

		if (diff > 30) {
			speedL = -0.2;
			// speedR = 0.2;
			speedR = 0;
			if (diff > 80) {
				// speedR = 0.3;
				speedL = -0.3;
			}
			drivetrain.tankDrive(speedL, speedR, false);

		} else if (diff < -30) {
			speedL = 0;
			speedR = -0.2;
			if (diff < -80) {
				// speedL = 0.3;
				speedR = -0.3;
			}
			drivetrain.tankDrive(speedL, speedR, false);

		} else {
			if (distanceH < 20) {
				speedL = 0;
				speedR = 0;
				drivetrain.tankDrive(speedL, speedR, false);

			} else if (distanceH > 50) {
				speedL = -0.3;
				speedR = -0.3;
				drivetrain.autoDrive(0.5, 0.05);
			} else {
				speedL = 0.5 / 35 * distanceH - 0.3;
				speedR = 0.5 / 35 * distanceH - 0.3;
				drivetrain.autoDrive(-0.5, -0.5);
			}
		}

		SmartDashboard.putString("DB/String 0", "auto place was reached");
		SmartDashboard.putString("DB/String 1", "speedL " + speedL);
		SmartDashboard.putString("DB/String 2", "speedR " + speedR);
		SmartDashboard.putString("DB/String 3", "diff " + diff);
		SmartDashboard.putString("DB/String 4", "distanceH " + distanceH);
		// drivetrain.tankDrive(speedL, speedR, false);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return isTimedOut();
	}

	// Called once after isFinished returns true
	protected void end() {
		// SmartDashboard.putString("DB/String 9", "time " + timeElasped());

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
