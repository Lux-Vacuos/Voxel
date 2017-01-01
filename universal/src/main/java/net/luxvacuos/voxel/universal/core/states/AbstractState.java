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

package net.luxvacuos.voxel.universal.core.states;

import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.resources.IDisposable;

public abstract class AbstractState implements IState, IDisposable {
	private final String stateName;
	private boolean running;

	protected AbstractState(String name) {
		this.stateName = name;
	}

	@Override
	public void init() {
	}

	@Override
	public void start() {
		this.running = true;
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
	}

	@Override
	public void end() {
		this.running = false;
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public String getName() {
		return this.stateName;
	}

	@Override
	public int hashCode() {
		final int prime = 523;
		int result = 1;
		result = prime * result + ((stateName == null) ? 0 : stateName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractState))
			return false;
		AbstractState other = (AbstractState) obj;
		if (stateName == null) {
			if (other.stateName != null)
				return false;
		} else if (!stateName.equals(other.stateName))
			return false;
		return true;
	}

}
