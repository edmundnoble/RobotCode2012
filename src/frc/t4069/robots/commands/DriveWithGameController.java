package frc.t4069.robots.commands;

import java.util.Date;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.utils.GameController;
import frc.t4069.utils.math.Point;

public class DriveWithGameController extends Command {

	private boolean XButtonVal = false;
	private boolean lastXButtonVal = false;
	private static final double killTimeSpin = 300;
	private double timeRunning = 0;
	private double startTime = 0;
	private long[] lastSensors = new long[3];
	private boolean[] falseConds, trueConds;

	public DriveWithGameController() {

	}

	protected void end() {
		// TODO Auto-generated method stub

	}

	protected void execute() {
		GameController gc = CommandBase.oi.getController();
		DriverStation ds = DriverStation.getInstance();
		double sensitivity = ds.getAnalogIn(2) / 5.0;

		processCamera(gc);
		processDriveTrain(gc, sensitivity);
		processArm(gc);
		processSpin(gc, ds);
		processShoot(gc);

		SmartDashboard.putDouble("Speed",
				CommandBase.pickupArm.testspeed);

	}

	protected void initialize() {
		// TODO Auto-generated method stub

	}

	protected void interrupted() {
		// TODO Auto-generated method stub

	}

	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void processArm(GameController gc) {
		if (gc.getButton(GameController.BTN_Y)) {
			CommandBase.pickupArm.forward();
		} else if (gc.getButton(GameController.BTN_A)) {
			CommandBase.pickupArm.reverse();
		} else {
			CommandBase.pickupArm.stop();
		}
	}

	protected void processCamera(GameController gc) {
		Point rightStick = gc.getRightStick();
		double y = (rightStick.y + 1) / 4.0 + 0.25;
		double x = (rightStick.x + 1) / 2.0;
		CommandBase.cameraMount.setTilt(y);
		CommandBase.cameraMount.setPan(x);
	}

	protected void processDriveTrain(GameController gc,
			double turnSensitivity) {
		if (gc.getButton(GameController.BTN_RB)
				|| gc.getButton(GameController.BTN_LB)) {
			CommandBase.drivetrain.hardBreak();
		} else {
			CommandBase.drivetrain.arcadeDrive(gc.getTrigger(),
					gc.getLeftStick().x * turnSensitivity);
		}
	}

	protected void processShoot(GameController gc) {
		Point leftStick = gc.getLeftStick();
		CommandBase.shootSystem.shoot(Math.abs(leftStick.y));
	}

	protected void processSpin(GameController gc, DriverStation ds) {
		boolean[] sensorOuts = CommandBase.sensors.getPhotoSensors();
		long currentTime = new Date().getTime();
		for (int i = 0; i < sensorOuts.length; i++) {
			if (sensorOuts[i]) {
				lastSensors[i] = (new Date().getTime() - currentTime) / 100;
			}
		}
		if (spinCheck(lastSensors)) { // TODO: add conditions
			toggleSpin(gc);
		}

	}

	private boolean spinCheck(long sensorTimes[]) {
		falseConds[0] = sensorTimes[1] < 10 && sensorTimes[2] < 20;
		falseConds[1] = sensorTimes[2] < 20 && sensorTimes[3] < 20;
		return true;
	}

	private void toggleSpin(GameController gc) {
		if (lastXButtonVal && !gc.getButton(GameController.BTN_X)) {
			XButtonVal = !XButtonVal;
			if (XButtonVal) {
				startTime = new Date().getTime();
			}
		}

		if (startTime - timeRunning > killTimeSpin) {
			XButtonVal = !XButtonVal;
		}

		if (XButtonVal) {
			timeRunning = new Date().getTime();
			CommandBase.pickupArm.runRoller(1);
		} else {
			CommandBase.pickupArm.runRoller(0);
		}
		lastXButtonVal = gc.getButton(GameController.BTN_X);
	}

}
