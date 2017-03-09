package org.usfirst.frc.team1350.robot.commands;

import org.usfirst.frc.team1350.robot.util.VisionThread;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class autonomousCommand extends CommandGroup {

	public autonomousCommand(VisionThread thread) {
		// addSequential(new AutoDrive());
		// addSequential(new AutoTurn());
		// addSequential(new AutoGear());
		// addSequential(new AutoPlace(thread));
		addSequential(new AutoPlace(thread));
		addSequential(new AutoDrive());
		// addSequential(new AutoTurn());
	}

	public void autoComm() {
		// addSequential(new AutoDrive());
		addSequential(new AutoTurn());
	}
}
