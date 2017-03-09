package org.usfirst.frc.team1350.robot.subsystems;

import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.commands.AgitatorControl;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Agitator extends Subsystem {

	private static Agitator instance;

	public static Agitator getInstance() {
		if (instance == null) {
			instance = new Agitator();
		}
		return instance;
	}

	// Declaring the motor controllers
	private VictorSP AgitatorMotorController;
	private RobotDrive robotDrive;
	private AgitatorControl tankDrive;

	public void init() {
		tankDrive = AgitatorControl.getInstance();
		AgitatorMotorController = new VictorSP(RobotMap.agitatorMotorController);
		robotDrive = new RobotDrive(AgitatorMotorController, AgitatorMotorController);
	}

	public void tankDrive(double left, double right, boolean squaredInputs) {
		robotDrive.tankDrive(-left, -right, false);
	}

	public void driveLeftMotor(double speed, double time) {
		AgitatorMotorController.set(speed);
		// Timer.delay(time);
		AgitatorMotorController.set(0);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(tankDrive);
	}
}
