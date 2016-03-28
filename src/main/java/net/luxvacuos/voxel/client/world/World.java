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

package net.luxvacuos.voxel.client.world;

import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.voxel.client.resources.GameResources;

public abstract class World {

	protected String name;
	protected Dimension activeDimension;

	private Map<Integer, Dimension> dimensions;

	public World(String name) {
		this.name = name;
		dimensions = new HashMap<>();
	}

	public abstract void init(GameResources gm);

	public void addDimension(Dimension dim) {
		dimensions.put(dim.getDimensionID(), dim);
	}

	public Dimension getDimension(int id) {
		return dimensions.get(id);
	}

	public void setActiveDimension(Dimension dim) {
		this.activeDimension = dim;
	}

	public void switchDimension(int id, GameResources gm) {
		activeDimension.getPhysicsEngine().removeEntity(gm.getCamera());
		activeDimension = getDimension(id);
		activeDimension.getPhysicsEngine().addEntity(gm.getCamera());
	}

	public Dimension getActiveDimension() {
		return activeDimension;
	}

	public String getName() {
		return name;
	}
	
	public void dispose(GameResources gm){
		for (Dimension dim : dimensions.values()) {
			dim.clearDimension(gm);
		}
	}

	public Map<Integer, Dimension> getDimensions() {
		return dimensions;
	}
}
