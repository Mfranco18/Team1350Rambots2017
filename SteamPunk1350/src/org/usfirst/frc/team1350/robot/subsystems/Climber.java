package org.usfirst.frc.team1350.robot.subsystems;

import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.commands.ClimberControl;
import org.usfirst.frc.team1350.robot.commands.TeleOpDriveTrain;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

	//called by the ClimberControl to notify what the commands class requires 
    private static Climber instance;
    public static Climber getInstance(){
		if (instance==null){
			instance = new Climber();
		}
		return instance;
	}
	
    
    //Declaring the motor controllers
    private VictorSP climberMotorController;
	private RobotDrive robotDrive;
	private ClimberControl tankDrive;
	
	
	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(tankDrive);
    }
    
    public void init(){
		tankDrive = ClimberControl.getInstance();
		climberMotorController = new VictorSP(RobotMap.climberMotorController);
		robotDrive = new RobotDrive(climberMotorController, climberMotorController);
	}
    
    public void tankDrive(double left, double right, boolean squaredInputs){
		robotDrive.tankDrive(-left, -right, false);
	}
    
    
    public void driveLeftMotor(double speed, double time){
    	climberMotorController.set(speed);
		//Timer.delay(time);
    	climberMotorController.set(0);
	}
    
}

