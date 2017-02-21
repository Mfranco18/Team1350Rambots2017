package org.usfirst.frc.team1350.robot.subsystems;

import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.commands.ShooterControl;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

	private static Shooter instance;

	public static Shooter getInstance() {
		if (instance == null) {
			instance = new Shooter();
		}
		return instance;
	}

	// Declaring the motor controllers
	private VictorSP shooterMotorController;
	private RobotDrive robotDrive;
	private ShooterControl tankDrive;

	public void init() {
		tankDrive = ShooterControl.getInstance();
		shooterMotorController = new VictorSP(RobotMap.shootingMotorController);
		robotDrive = new RobotDrive(shooterMotorController, shooterMotorController);
	}

	public void tankDrive(double left, double right, boolean squaredInputs) {
		robotDrive.tankDrive(-left, -right, false);
	}

	public void driveLeftMotor(double speed, double time) {
		shooterMotorController.set(speed);
		// Timer.delay(time);
		shooterMotorController.set(0);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(tankDrive);
	}
}
