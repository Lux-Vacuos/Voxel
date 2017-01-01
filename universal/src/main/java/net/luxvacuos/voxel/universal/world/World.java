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

package net.luxvacuos.voxel.universal.world;

import java.util.Collection;

import com.badlogic.gdx.utils.IntMap;

import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class World implements IWorld {

	private String name;
	private IDimension activeDimension;
	private IntMap<IDimension> dims;

	public World(String name) {
		this.name = name;
		this.dims = new IntMap<>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void update(float delta) {
		this.activeDimension.update(delta);
		for (IDimension dim : this.dims.values()) {
			if(dim.getID() == this.activeDimension.getID()) continue;
			
			dim.update(delta);
		}
	}

	@Override
	public void dispose() {
		for (IDimension dim : this.dims.values()) {
			dim.dispose();
		}
	}

	/*
	 * TODO:
	 * The world should be generating the Dimensions within itself, including loading and saving
	 * any revelant information it needs
	 */
	@Override
	public void addDimension(IDimension dimension) {
		this.dims.put(dimension.getID(), dimension);
	}

	@Override
	public IDimension getDimension(int id) {
		return this.dims.get(id);
	}

	@Override
	public void setActiveDimension(int id) {
		activeDimension = this.dims.get(id);
	}

	@Override
	public IDimension getActiveDimension() {
		return activeDimension;
	}

	@Override
	public Collection<IDimension> getDimensions() {
		//return Collections.unmodifiableCollection();
		return null; //TODO: Fix
	}

}
