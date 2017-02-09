	package org.usfirst.frc.team1350.robot.subsystems;

import org.usfirst.frc.team1350.robot.OI;
import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.commands.TeleOpDriveTrain;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;

/**
 *
 */
public class DriveTrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(tankDrive);
    }
   
    //called by the TeleOpDriveTrain to notify what the commands class requires 
    private static DriveTrain instance;
    public static DriveTrain getInstance(){
		if (instance==null){
			instance = new DriveTrain();
		}
		return instance;
	}
    
    //Declaring the motor controllers
    private VictorSP leftMotorController;
	private VictorSP rightMotorController;
	private RobotDrive robotDrive;
	private TeleOpDriveTrain tankDrive;
	//compressor 
	private Compressor compressor;
	//solenoids
	DoubleSolenoid leftSolenoid; 
	DoubleSolenoid rightSolenoid;
	Trigger solenoidSwitchLeft, solenoidSwitchRight;
	//private Command switchDriveGear; 
	 
	
	
	public DriveTrain(){
		//Log.info("Creating Drivetrain subsystem");
       //init();
	}
	
	public void init(){
		tankDrive =TeleOpDriveTrain.getInstance();
		leftMotorController = new VictorSP(RobotMap.rightMotorController);
		rightMotorController = new VictorSP(RobotMap.leftMotorController); 
		robotDrive = new RobotDrive(leftMotorController, rightMotorController);
		
		//compressor 
		compressor = new Compressor(RobotMap.Compressor);
		compressor.setClosedLoopControl(false);
		
		//solenoid 
		leftSolenoid = new DoubleSolenoid(0, 1);
		leftSolenoid.set(DoubleSolenoid.Value.kOff);
		rightSolenoid = new DoubleSolenoid(2, 3);
		rightSolenoid.set(DoubleSolenoid.Value.kOff);
		
		solenoidSwitchRight = new JoystickButton(OI.getInstance().leftStick, 1);
		solenoidSwitchLeft = new JoystickButton(OI.getInstance().rightStick, 1);
		
		
	}
	
	public void tankDrive(double left, double right, boolean squaredInputs){
		robotDrive.tankDrive(left, right, false);
		compressor.setClosedLoopControl(true);
		
		if(solenoidSwitchRight.get()){
			leftSolenoid.set(DoubleSolenoid.Value.kForward);
			rightSolenoid.set(DoubleSolenoid.Value.kForward);
		}
		
		if(solenoidSwitchLeft.get()){
			leftSolenoid.set(DoubleSolenoid.Value.kReverse);
			rightSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
		
		
	}
	
	public void autoDrive(double speed, double curve){
		robotDrive.drive(speed, curve);
	}
	
	public void driveLeftMotor(double speed, double time){
		leftMotorController.set(speed);
		Timer.delay(time);
		leftMotorController.set(0);
	}
	
	public void driveRightMotor(double speed, double time){
		rightMotorController.set(speed);
		Timer.delay(time);
		rightMotorController.set(0);
	}
	
	
}

