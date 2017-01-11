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

package net.luxvacuos.voxel.server.core;

import net.luxvacuos.voxel.universal.core.WorldSimulation;

public class ServerWorldSimulation extends WorldSimulation {

	public ServerWorldSimulation() {
		super(10000);
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
		// Set time
		time += delta * TIME_MULTIPLIER;
		time %= 24000;
		// Set global time for clouds
		globalTime += delta * TIME_MULTIPLIER;
		float res = time * 0.015f;

		// Return the sun rotation
		return res - 90;
	}

}
