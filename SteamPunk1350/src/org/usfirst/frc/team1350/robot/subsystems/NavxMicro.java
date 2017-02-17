package org.usfirst.frc.team1350.robot.subsystems;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1350.robot.RobotMap;

import com.kauailabs.navx.frc.AHRS;

public class NavxMicro extends Subsystem {
	
	//defining the nav-x micro as a ahrs
	AHRS navX;
	
	
	//called to notify what the commands class requires 
    private static NavxMicro instance;
    
    public static NavxMicro getInstance(){
		if (instance==null){
			instance = new NavxMicro();
		}
		return instance;
	}
    
    public void init(){
    	
    	 try {
		        /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
		        /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
		        /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
    		 navX = new AHRS(SerialPort.Port.kUSB); 
		        
		        
		        
		    } catch (RuntimeException ex ) {
		        DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
		    }
    	 
    	 
    	
    	 //navX.
    }
    
    public float getHeading(){
    	return navX.getFusedHeading();
    }
    
    
    public void showXDisplacment(){
    	 //SmartDashboard.putString("DB/String 5", "X Dis = " +navX.getDisplacementX());
    	 SmartDashboard.putString("DB/String 5", "Heading = " +navX.getFusedHeading());
    	 SmartDashboard.putString("DB/String 6", "Speed = " +navX.getVelocityX());
    	 SmartDashboard.putString("DB/String 7", "Acc = " +navX.getRawAccelX());
    	 
    	 
    	 //if (SmartDashboard.getBoolean("DB/Button 3")){
    		 boolean buttonValue = SmartDashboard.getBoolean("DB/Button 0", false);
    		 if (buttonValue){
    			 navX.reset();
    			 navX.resetDisplacement();
    		 
    		 
    	 }
    	 
    }

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

    
    

}
