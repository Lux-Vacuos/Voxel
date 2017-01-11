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

package net.luxvacuos.voxel.universal.core;

public class WorldSimulation implements IWorldSimulation {
	
	protected float time = 0;
	protected float globalTime = 0;
	protected float rainFactor = 0;
	
	public WorldSimulation() {
		this.time = 10000;
	}
	
	protected WorldSimulation(float time) {
		this.time = time;
	}

	@Override
	public float getGlobalTime() {
		return this.globalTime;
	}

	@Override
	public float getRainFactor() {
		return this.rainFactor;
	}

	@Override
	public float getTime() {
		return this.time;
	}

	@Override
	public void setTime(float time) {
		this.time = time;
	}

	@Override
	public float update(float deltaTime) {
		return 0;
	}

}
