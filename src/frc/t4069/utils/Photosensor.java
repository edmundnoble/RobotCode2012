package frc.t4069.utils;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SensorBase;

public class Photosensor extends SensorBase {

	private DigitalInput m_sensor;

	public Photosensor(int sensorChannel) {
		m_sensor = new DigitalInput(sensorChannel);
	}

	public boolean isOn() {
		return m_sensor.get();
	}

	public void free() {
		if (m_sensor != null) {
			m_sensor.free();
		}
	}

}
