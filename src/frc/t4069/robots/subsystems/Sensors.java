package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import frc.t4069.robots.RobotMap;
import frc.t4069.utils.MaxbotixSonar;
import frc.t4069.utils.math.Point3D;

public class Sensors {

	public ADXL345_I2C accelerometer;
	public Gyro gyroscope;
	public AnalogChannel gyroscopeChannel;
	public MaxbotixSonar sonar;
	private DigitalInput[] photoSensors;

	public Sensors() {
		this(RobotMap.ACCELEROMETER_CHANNEL, RobotMap.GYRO_CHANNEL,
				RobotMap.SONAR_CHANNEL, RobotMap.PHOTO_CHANNEL_1,
				RobotMap.PHOTO_CHANNEL_2, RobotMap.PHOTO_CHANNEL_3);
	}

	public Sensors(int accelerometerSlot, int gyroChannel,
			int sonarChannel, int photoChannel1, int photoChannel2,
			int photoChannel3) {
		gyroscopeChannel = new AnalogChannel(gyroChannel);
		accelerometer = new ADXL345_I2C(accelerometerSlot,
				ADXL345_I2C.DataFormat_Range.k2G);
		gyroscope = new Gyro(gyroscopeChannel);
		sonar = new MaxbotixSonar(sonarChannel);
		DigitalInput[] photoSensors = {
				new DigitalInput(photoChannel1),
				new DigitalInput(photoChannel2),
				new DigitalInput(photoChannel3) };
		this.photoSensors = photoSensors;
	}

	public Point3D getAccelerations() {
		double x, y, z;
		x = accelerometer.getAcceleration(ADXL345_I2C.Axes.kX);
		y = accelerometer.getAcceleration(ADXL345_I2C.Axes.kY);
		z = accelerometer.getAcceleration(ADXL345_I2C.Axes.kZ);
		return new Point3D(x, y, z);
	}

	public double getAngleFromGyro() {
		return gyroscope.getAngle();
	}

	public double getDistance() {
		return sonar.getDistance(false);
	}

	public double getDistance(boolean inch) {
		return sonar.getDistance(inch);
	}

	public boolean getPhotoSensor(int sensorNum) {
		return photoSensors[sensorNum].get();
	}
}
