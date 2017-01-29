	package org.usfirst.frc.team1350.robot.subsystems;

import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.commands.TeleOpDriveTrain;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

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
	
	
	public DriveTrain(){
		//Log.info("Creating Drivetrain subsystem");
       //init();
	}
	
	public void init(){
		tankDrive =TeleOpDriveTrain.getInstance();
		leftMotorController = new VictorSP(RobotMap.rightMotorController);
		rightMotorController = new VictorSP(RobotMap.leftMotorController); 
		robotDrive = new RobotDrive(leftMotorController, rightMotorController);
	}
	
	public void tankDrive(double left, double right, boolean squaredInputs){
		robotDrive.tankDrive(-left, -right, false);
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

