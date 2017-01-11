/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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
import net.luxvacuos.voxel.universal.core.WorldSimulation;

/**
 * 
 * World Simulation, this handles the simulation like time.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public final class ClientWorldSimulation extends WorldSimulation {

	private float moveFactor = 0;
	private float rotation = 0;

	public ClientWorldSimulation(final float time) {
		super(time); //10000
	}

	/**
	 * Update the simulation
	 * 
	 * @param delta
	 *            Delta to update
	 * @return Rotation for sun
	 */
	@Override
	public float update(float delta) {
		// Set move factor for waves
		this.moveFactor += ClientVariables.WAVE_SPEED * delta;
		this.moveFactor %= 6.3f;
		// Set time
		this.time += delta * TIME_MULTIPLIER;
		this.time %= 24000;
		// Set global time for clouds
		this.globalTime += delta * TIME_MULTIPLIER;
		float res = time * 0.015f;

		// Check for rain
		if (ClientVariables.raining) {
			this.rainFactor += 0.2f * delta;
		} else
			this.rainFactor -= 0.2f * delta;

		this.rainFactor = Maths.clamp(this.rainFactor, 0f, 1f);
		
		// Return the sun rotation
		return (this.rotation = (res - 90));
	}
	
	public float getRotation() {
		return this.rotation;
	}

	public float getMoveFactor() {
		return moveFactor;
	}

}
