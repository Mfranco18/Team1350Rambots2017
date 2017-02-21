package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.OI;
import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.subsystems.Kicker;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class KickerControl extends Command {

	private static KickerControl instance;

	public static KickerControl getInstance() {
		if (instance == null) {
			instance = new KickerControl();
		}
		return instance;
	}

	// define variables
	private boolean squaredInputs;
	private final static double speed = 0.85;
	private Trigger XboxButtonKicker;

	public KickerControl() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Kicker.getInstance());
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		squaredInputs = false;
		XboxButtonKicker = new JoystickButton(OI.getInstance().XboxControllerLeft, RobotMap.xboxButtonKicker);

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		// if (SmartDashboard.getBoolean("DB/Button 3", false)){
		if (XboxButtonKicker.get()) {
			Kicker.getInstance().tankDrive(speed, speed, squaredInputs);
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
