package org.usfirst.frc.team1350.robot.subsystems;

import org.usfirst.frc.team1350.robot.RobotMap;
import org.usfirst.frc.team1350.robot.commands.ClimberControl;
import org.usfirst.frc.team1350.robot.commands.IntakeControl;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

	 private static Intake instance;
	    public static Intake getInstance(){
			if (instance==null){
				instance = new Intake();
			}
			return instance;
		}
	    
	    //Declaring the motor controllers
	    private VictorSP intakeMotorController;
		private RobotDrive robotDrive;
		private IntakeControl tankDrive;
	
		
		
	 public void init(){
			tankDrive = IntakeControl.getInstance();
			intakeMotorController = new VictorSP(RobotMap.intakeMotor);
			robotDrive = new RobotDrive(intakeMotorController, intakeMotorController);
		}
	 
	 public void tankDrive(double left, double right, boolean squaredInputs){
			robotDrive.tankDrive(-left, -right, false);
		}
	    
	    
	    public void driveLeftMotor(double speed, double time){
	    	intakeMotorController.set(speed);
			//Timer.delay(time);
	    	intakeMotorController.set(0);
		}
	   
	
	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(tankDrive);
    }
}

