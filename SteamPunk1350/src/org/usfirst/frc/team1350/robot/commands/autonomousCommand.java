package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.util.VisionThread;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class autonomousCommand extends CommandGroup {

	public autonomousCommand(VisionThread thread) {

		// // middle
		// addSequential(new AutoDrive1());
		//
		// right
		addSequential(new AutoDrive());
		addSequential(new AutoTurnRight());
		addSequential(new AutoDrive1());

		// addSequential(new AutoDrive1());
		// addSequential(new AutoTurnRight());

		// left
		// addSequential(new AutoDrive1());
		// addSequential(new AutoTurnLeft());
		// addSequential(new AutoDrive());
	}

	public void autoComm() {
		// addSequential(new AutoDrive());
		addSequential(new AutoTurnRight());
	}
}
