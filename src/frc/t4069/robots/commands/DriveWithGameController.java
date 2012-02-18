package frc.t4069.robots.commands;

import java.util.Date;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.t4069.utils.GameController;
import frc.t4069.utils.math.Point;

public class DriveWithGameController extends Command {

	private boolean XButtonVal = false, lastXButtonVal = false;
	private static final double killTimeSpin = 300;
	private double timeRunning = 0;
	private double startTime = 0;
	private long[] sensorTimes = new long[3],
			lastSensorTimes = new long[3];

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
		boolean[] sensorVals = CommandBase.sensors.getPhotoSensors();
		for (int i = 0; i < sensorVals.length; i++) {
			SmartDashboard.putBoolean("Photosensor " + (i + 1),
					sensorVals[i]);
		}
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
		for (int i = 0; i < sensorOuts.length - 1; i++) {
			lastSensorTimes[i] = sensorTimes[i];
			if (sensorOuts[i]) {
				sensorTimes[i] = currentTime;
			}
			sensorTimes[i] = currentTime - sensorTimes[i];
		}
		toggleSpin(gc,
				spinCheck(sensorTimes, lastSensorTimes, currentTime));

	}

	private boolean spinCheck(long[] sensorTimes,
			long[] lastSensorTimes, long currentTime) {
		boolean[] falseConds = {
				sensorTimes[0] < 100 && sensorTimes[1] < 200,
				sensorTimes[1] < 200 && sensorTimes[2] < 200,
				sensorTimes[0] > lastSensorTimes[0] - 1000,
				sensorTimes[2] == currentTime
						&& sensorTimes[1] < currentTime / 9,
				sensorTimes[0] > sensorTimes[1]
						&& sensorTimes[1] > currentTime / 9 };
		for (int i = 0; i < falseConds.length - 1; i++) {
			if (falseConds[i])
				return false;
		}
		return true;
	}

	private void toggleSpin(GameController gc, boolean run) {
		if (lastXButtonVal && !gc.getButton(GameController.BTN_X)) {
			XButtonVal = !XButtonVal;
			if (XButtonVal) {
				startTime = new Date().getTime();
			}
		}
		if (startTime - timeRunning > killTimeSpin) {
			XButtonVal = !XButtonVal;
		}
		if (!run) {
			XButtonVal = false;

			if (XButtonVal) {
				timeRunning = new Date().getTime();
				CommandBase.pickupArm.runRoller(1);
			} else {
				CommandBase.pickupArm.runRoller(0);
			}
			lastXButtonVal = gc.getButton(GameController.BTN_X);
		}

	}
}
