package frc.t4069.robots;

import edu.wpi.first.wpilibj.PWM;

public class RobotMap {
	// Motor section!
	public static final int RIGHT_MOTOR = 1;
	public static final int LEFT_MOTOR = 2;

	public static final int CAMERA_TILT_SERVO = 3;
	public static final int CAMERA_PAN_SERVO = 4;

	public static final int GYRO_CHANNEL = 1;
	public static final int ACCELEROMETER_CHANNEL = 1;
	public static final int SONAR_CHANNEL = 3;
	public static final int PHOTO_CHANNEL_1 = 1;
	public static final int PHOTO_CHANNEL_2 = 2;
	public static final int PHOTO_CHANNEL_3 = 3;

	public static final int PICKUP_ARM_MOTOR_1 = 7;
	public static final int PICKUP_ARM_MOTOR_2 = 8;
	public static final int PICKUP_SPIN_MOTOR = 5;

	public static final int SHOOT_CHANNEL = 10;

	public static final int BANNED_CHANNEL = 6;
	private static final PWM BANNED_PWM = new PWM(BANNED_CHANNEL);

}
