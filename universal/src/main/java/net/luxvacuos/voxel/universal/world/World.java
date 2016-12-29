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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class World implements IWorld {

	private String name;
	private IDimension activeDimension;
	private List<IDimension> dimensions;

	public World(String name) {
		this.name = name;
		dimensions = new ArrayList<>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void update(float delta) {
		for (IDimension iDimension : dimensions) {
			iDimension.update(delta);
		}
	}

	@Override
	public void dispose() {
		for (IDimension iDimension : dimensions) {
			iDimension.dispose();
		}
	}

	@Override
	public void addDimension(IDimension dimension) {
		dimensions.add(dimension);
	}

	@Override
	public IDimension getDimension(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActiveDimension(int id) {
		activeDimension = dimensions.get(id);
	}

	@Override
	public IDimension getActiveDimension() {
		return activeDimension;
	}

	@Override
	public Collection<IDimension> getDimensions() {
		return Collections.unmodifiableCollection(dimensions);
	}

}
