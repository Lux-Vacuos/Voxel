package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;

public class Particle {
	private Vector3f m_Position;
	private Vector3f m_Velocity;
	private Vector3f m_Color;
	private float m_fRotate;
	private float m_fSize;
	private float m_fAge;
	private float m_fLifeTime;

	public Particle(Vector3f position, Vector3f velocity, Vector3f color,
			float fRotate, float fSize, float fAge, float fLifeTime) {
		this.m_Position = position;
		this.m_Velocity = velocity;
		this.m_Color = color;
		this.m_fRotate = fRotate;
		this.m_fSize = fSize;
		this.m_fAge = fAge;
		this.m_fLifeTime = fLifeTime;
	}

	public Vector3f getM_Position() {
		return m_Position;
	}

	public void setM_Position(Vector3f m_Position) {
		this.m_Position = m_Position;
	}

	public Vector3f getM_Velocity() {
		return m_Velocity;
	}

	public void setM_Velocity(Vector3f m_Velocity) {
		this.m_Velocity = m_Velocity;
	}

	public Vector3f getM_Color() {
		return m_Color;
	}

	public void setM_Color(Vector3f m_Color) {
		this.m_Color = m_Color;
	}

	public float getM_fRotate() {
		return m_fRotate;
	}

	public void setM_fRotate(float m_fRotate) {
		this.m_fRotate = m_fRotate;
	}

	public float getM_fSize() {
		return m_fSize;
	}

	public void setM_fSize(float m_fSize) {
		this.m_fSize = m_fSize;
	}

	public float getM_fAge() {
		return m_fAge;
	}

	public void setM_fAge(float m_fAge) {
		this.m_fAge = m_fAge;
	}

	public float getM_fLifeTime() {
		return m_fLifeTime;
	}

	public void setM_fLifeTime(float m_fLifeTime) {
		this.m_fLifeTime = m_fLifeTime;
	}

}
