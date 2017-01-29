package org.usfirst.frc.team1350.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Lifter extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	 //called by the LifterDrive to notify what the commands class requires 
    private static Lifter instance;
    public static Lifter getInstance(){
		if (instance==null){
			instance = new Lifter();
		}
		return instance;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

