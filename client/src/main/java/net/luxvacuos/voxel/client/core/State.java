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

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.universal.api.mod.ModStateLoop;

/**
 * State
 * 
 * @author danirod
 * @category Kernel
 */
public abstract class State {

	private List<ModStateLoop> modsLoops = new ArrayList<>();

	public void start() {
	}

	void iUpdate(Voxel voxel, float delta) throws Exception {
		update(voxel, delta);
		for (ModStateLoop modStateLoop : modsLoops) {
			modStateLoop.update(voxel, delta);
		}
	}

	public abstract void update(Voxel voxel, float delta) throws Exception;

	public abstract void render(Voxel voxel, float alpha);

	public void end() {
	}

	public List<ModStateLoop> getModsLoops() {
		return modsLoops;
	}
}
