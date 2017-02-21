package org.usfirst.frc.team1350.robot.subsystems;

import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.commands.KickerControl;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Kicker extends Subsystem {

	private static Kicker instance;

	public static Kicker getInstance() {
		if (instance == null) {
			instance = new Kicker();
		}
		return instance;
	}

	// Declaring the motor controllers
	private VictorSP kickerMotorController;
	private RobotDrive robotDrive;
	private KickerControl tankDrive;

	public void init() {
		tankDrive = KickerControl.getInstance();
		kickerMotorController = new VictorSP(RobotMap.shooterKickerMotorController);
		robotDrive = new RobotDrive(kickerMotorController, kickerMotorController);
	}

	public void tankDrive(double left, double right, boolean squaredInputs) {
		robotDrive.tankDrive(-left, -right, false);
	}

	public void driveLeftMotor(double speed, double time) {
		kickerMotorController.set(speed);
		// Timer.delay(time);
		kickerMotorController.set(0);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(tankDrive);
	}
}
