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

package net.luxvacuos.voxel.server.core;

import net.luxvacuos.voxel.server.util.Maths;

public class WorldSimulation {

	private float time = 0;
	private float globalTime = 0;
	private float rainFactor;

	private static final float TIME_MULTIPLIER = 10;

	public WorldSimulation() {
		time = 12000;
	}

	public void update(float delta) {
		time += delta * TIME_MULTIPLIER;
		time %= 24000;
		globalTime += delta * TIME_MULTIPLIER;

		if (VoxelVariables.raining)
			rainFactor += 0.2f * delta;
		else
			rainFactor -= 0.2f * delta;
		rainFactor = Maths.clamp(rainFactor, 0f, 1f);
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
