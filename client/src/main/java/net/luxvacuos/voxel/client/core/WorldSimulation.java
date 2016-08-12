/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.core;

import net.luxvacuos.voxel.client.util.Maths;

/**
 * 
 * World Simulation, this handles the simulation like time.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class WorldSimulation {

	private float moveFactor = 0;
	private float time = 0;
	private float globalTime = 0;
	private float rainFactor;

	private static final float TIME_MULTIPLIER = 10;

	public WorldSimulation() {
		time = 7000;
	}

	/**
	 * Update the simulation
	 * 
	 * @param delta
	 *            Delta to update
	 * @return Rotation for sun
	 */
	public float update(float delta) {
		// Set move factor for waves
		moveFactor += VoxelVariables.WAVE_SPEED * delta;
		moveFactor %= 6.3f;
		// Set time
		time += delta * TIME_MULTIPLIER;
		time %= 24000;
		// Set global time for clouds
		globalTime += delta * TIME_MULTIPLIER;
		float res = time * 0.015f;

		// Check for rain
		if (VoxelVariables.raining) {
			rainFactor += 0.2f * delta;
		} else
			rainFactor -= 0.2f * delta;

		rainFactor = Maths.clamp(rainFactor, 0f, 1f);
		// Return the sun rotation
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
