package org.usfirst.frc.team1350.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class autonomousCommand extends CommandGroup {

	public autonomousCommand() {
		// addSequential(new AutoDrive());
		// addSequential(new AutoTurn());
		addSequential(new AutoGear());
	}

	public void autoComm() {
		// addSequential(new AutoDrive());
		addSequential(new AutoTurn());
	}
}
