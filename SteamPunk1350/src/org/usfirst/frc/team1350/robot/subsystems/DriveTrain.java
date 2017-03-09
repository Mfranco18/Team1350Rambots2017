package org.usfirst.frc.team1350.robot.subsystems;

import org.usfirst.frc.team1350.robot.OI;
import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.commands.TeleOpDriveTrain;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(tankDrive);
	}

	// called by the TeleOpDriveTrain to notify what the commands class requires
	private static DriveTrain instance;

	public static DriveTrain getInstance() {
		if (instance == null) {
			instance = new DriveTrain();
		}
		return instance;
	}

	// Declaring the motor controllers
	private VictorSP leftMotorController;
	private VictorSP rightMotorController;
	private RobotDrive robotDrive;
	private TeleOpDriveTrain tankDrive;
	// compressor
	private Compressor compressor;

	Trigger compressorValueOn, compressorValueOff;
	// solenoids for practice bot
	DoubleSolenoid leftSolenoid;
	DoubleSolenoid rightSolenoid;

	// solenoids for the game bot
	Solenoid firstLeft, firstRight, secondLeft, secondRight;
	Trigger solenoidSwitchLeft, solenoidSwitchRight;

	Trigger gearForwardDrive, gearBackDrive;
	// private Command switchDriveGear;

	// variables for the autoDrive
	double autoSpeed = 0.5;
	double autoCurve = 0.0;

	// timer
	Timer timer = new Timer();

	public DriveTrain() {
		// Log.info("Creating Drivetrain subsystem");
		// init();
	}

	public void init() {

		// declarations for the drive system
		tankDrive = TeleOpDriveTrain.getInstance();
		leftMotorController = new VictorSP(RobotMap.rightMotorController);
		rightMotorController = new VictorSP(RobotMap.leftMotorController);
		robotDrive = new RobotDrive(leftMotorController, rightMotorController);

		// compressor
		compressor = new Compressor(RobotMap.Compressor);
		compressor.setClosedLoopControl(false);
		// trigger for the cmpressor with the xbox
		compressorValueOn = new JoystickButton(OI.getInstance().XboxControllerLeft, RobotMap.xboxCompressorButtonOn);
		// JoystickButton(OI.getInstance().XboxControllerLeft,
		// RobotMap.xboxCompressorButtonOn);
		// compressorValueOn = new JoystickButton(OI.getInstance().leftStick,
		// RobotMap.leftTriggerCompOn);
		// compressorValueOff = new JoystickButton(OI.getInstance().rightStick,
		// RobotMap.rightTriggerCompOff);

		compressorValueOff = new JoystickButton(OI.getInstance().XboxControllerLeft, RobotMap.xboxCompressorButtonOff);

		/*
		 * //solenoid for practice leftSolenoid = new DoubleSolenoid(0, 1);
		 * leftSolenoid.set(DoubleSolenoid.Value.kOff); rightSolenoid = new
		 * DoubleSolenoid(2, 3); rightSolenoid.set(DoubleSolenoid.Value.kOff);
		 */
		solenoidSwitchRight = new JoystickButton(OI.getInstance().leftStick, 1);
		solenoidSwitchLeft = new JoystickButton(OI.getInstance().rightStick, 1);

		// solenoids for competition
		firstLeft = new Solenoid(0);
		firstLeft.set(false);
		secondLeft = new Solenoid(1);
		secondLeft.set(false);
		firstRight = new Solenoid(2);
		firstRight.set(true);
		secondRight = new Solenoid(3);
		secondRight.set(true);

		// switching the robots orientation
		gearForwardDrive = new JoystickButton(OI.getInstance().leftStick, 3);
		gearBackDrive = new JoystickButton(OI.getInstance().leftStick, 2);

	}

	public void tankDrive(double left, double right, boolean squaredInputs) {

		// gearForwardDrive.whenActive(driveReverse(left, right, false));
		/*
		 * if(gearForwardDrive.get()){ robotDrive.tankDrive(-right, -left,
		 * false); }
		 */
		// gearForwardDrive.whenInactive(driveNormal(left, right, false));
		robotDrive.tankDrive(left, right, false);

		compressorOn();
		SmartDashboard.putString("DB/String 5", "Compressor Status = " + compressor.enabled());

		switchSolenoid();

		boolean buttonValue = SmartDashboard.getBoolean("DB/Button 1", false);

		if (buttonValue) {
			// autoDrive(autoSpeed, autoCurve);

		}
	}

	private Command driveNormal(double left, double right, boolean b) {
		robotDrive.tankDrive(left, right, false);
		return null;
	}

	private Command driveReverse(double left, double right, boolean temp) {
		robotDrive.tankDrive(-right, -left, false);
		return null;
	}

	// monitors the compressor for the rest of the match
	private void compressorOn() {

		if (compressorValueOn.get()) {
			compressor.setClosedLoopControl(true);
		}

		if (compressorValueOff.get()) {
			compressor.setClosedLoopControl(false);
		}

	}

	// on the triggers of the two main joysticks, allows to shift up and down
	private void switchSolenoid() {
		if (solenoidSwitchRight.get()) {
			/*
			 * //for the practice bot
			 * leftSolenoid.set(DoubleSolenoid.Value.kForward);
			 * rightSolenoid.set(DoubleSolenoid.Value.kForward);
			 */

			// for the real bot
			firstLeft.set(false);
			secondLeft.set(false);

			firstRight.set(true);
			secondRight.set(true);
		}

		if (solenoidSwitchLeft.get()) {
			/*
			 * //for the practice bot
			 * leftSolenoid.set(DoubleSolenoid.Value.kReverse);
			 * rightSolenoid.set(DoubleSolenoid.Value.kReverse);
			 */

			// for the real bot
			firstRight.set(false);
			secondRight.set(false);

			firstLeft.set(true);
			secondLeft.set(true);

		}
	}

	public void testMotors() {

	}

	public void AutoTurn(double speed, double curve) {
		robotDrive.tankDrive(0, -0.5);
	}

	// does not work
	public void autoDrive(double speed, double curve) {
		// wrks for 0.25 speed

		robotDrive.drive(speed, curve);

		// robotDrive.drive(speed, 0);

	}

	public void driveLeftMotor(double speed, double time) {
		leftMotorController.set(speed);
		Timer.delay(time);
		leftMotorController.set(0);
	}

	public void driveRightMotor(double speed, double time) {
		rightMotorController.set(speed);
		Timer.delay(time);
		rightMotorController.set(0);
	}

	// allows the driver to switch the orientation of the robot
	public boolean orientationTriggerGet() {

		return gearForwardDrive.get();
	}

}
