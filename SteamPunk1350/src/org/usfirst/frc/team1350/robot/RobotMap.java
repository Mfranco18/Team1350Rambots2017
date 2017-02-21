package org.usfirst.frc.team1350.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// PWM

	// Motor Controllers
	public static final int rightMotorController = 0;
	public static final int leftMotorController = 1;
	public static final int climberMotorController = 2;
	public static final int intakeMotor = 3;
	public static final int shootingMotorController = 4;
	public static final int shooterKickerMotorController = 5;

	// Inputs

	// JoySticks
	public static final int right_Joystick = 0;
	public static final int left_Joystick = 1;
	public static final int XboxController = 2;

	// Triggers
	public static final int xboxIntakeButton = 2;
	public static final int xboxCompressorButtonOn = 5;
	public static final int xboxCompressorButtonOff = 6;
	public static final int xboxButtonShooter = 1;
	public static final int xboxButtonKicker = 3;

	// PCM

	// Compressor
	public static final int Compressor = 0;

	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
