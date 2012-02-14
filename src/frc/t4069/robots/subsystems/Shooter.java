package frc.t4069.robots.subsystems;

import edu.wpi.first.wpilibj.Victor;

import frc.t4069.robots.RobotMap;

public class Shooter {

	private Victor m_shootVictor;

	public Shooter() {
		this(RobotMap.SHOOT_CHANNEL);
	}

	public Shooter(int channel) {
		m_shootVictor = new Victor(channel);
	}

	public void shoot() {
		shoot(1.0);
	}

	public void shoot(double power) {
		m_shootVictor.set(power);
	}

}
