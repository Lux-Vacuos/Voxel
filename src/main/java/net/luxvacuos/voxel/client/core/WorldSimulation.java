package net.luxvacuos.voxel.client.core;

import net.luxvacuos.voxel.client.util.Maths;

public class WorldSimulation {

	private float moveFactor = 0;
	private float time = 0;
	private float globalTime = 0;
	private float accTime = 0;
	private float rainFactor;

	private static final float TIME_MULTIPLIER = 10;

	public WorldSimulation() {
		time = 10000;
	}

	public float update(float delta) {
		moveFactor += VoxelVariables.WAVE_SPEED * delta;
		moveFactor %= 6.3f;
		time += delta * TIME_MULTIPLIER;
		time %= 24000;
		globalTime += delta * TIME_MULTIPLIER;
		float res = time * 0.015f;

		if (VoxelVariables.raining) {
			rainFactor += 0.2f * delta;
		} else
			rainFactor -= 0.2f * delta;

		rainFactor = Maths.clamp(rainFactor, 0f, 1f);

		return res - 90;
	}

	public float getMoveFactor() {
		return moveFactor;
	}

	public float getGlobalTime() {
		return globalTime;
	}

	public float getRainFactor() {
		return rainFactor;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

}
