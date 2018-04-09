/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import com.badlogic.gdx.utils.IntMap;
import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.voxel.universal.world.dimension.Dimension;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class World implements IWorld {

	private String name;
	private IDimension activeDimension;
	protected IntMap<IDimension> dims;

	public World(String name) {
		this.name = name;
		this.dims = new IntMap<>();
		File file = new File(
				CoreSubsystem.REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/directory")) + this.name);
		if (!file.exists())
			file.mkdirs();
	}

	protected IDimension createDimension(int id, TagCompound data) {
		return new Dimension(this, data, id);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void update(float delta) {
		this.activeDimension.update(delta);
		if (this.dims.size > 1) {
			for (IDimension dim : this.dims.values()) {
				if (dim.getID() == this.activeDimension.getID())
					continue;

				dim.update(delta);
			}
		}
	}
	
	@Override
	public void beforeUpdate(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterUpdate(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		for (IDimension dim : this.dims.values()) {
			dim.dispose();
		}
	}

	/*
	 * TODO: The world should be generating the Dimensions within itself,
	 * including loading and saving any revelant information it needs
	 */
	@Override
	public void addDimension(IDimension dimension) {
		this.dims.put(dimension.getID(), dimension);
	}

	@Override
	public void loadDimension(int id) {
		if (this.dims.containsKey(id))
			return;

		File file = new File(CoreSubsystem.REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/directory"))
				+ this.name + "/dim" + id + "_data.nbt");
		TagCompound data = null;
		NBTInputStream in = null;
		try {
			if (file.createNewFile()) { // True if the file was created
				CompoundBuilder builder = new CompoundBuilder().start();
				builder.addFloat("Time", 6500).addFloat("RainFactor", 0);
				builder.addLong("Seed", new Random().nextLong());
				data = builder.build();
			} else {
				in = new NBTInputStream(new BufferedInputStream(new FileInputStream(file)));
				data = new TagCompound(in, false);
			}
		} catch (Exception e) {
			Logger.error(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Logger.error(e);
				}
			}
		}

		this.dims.put(id, this.createDimension(id, data));
	}

	@Override
	public IDimension getDimension(int id) {
		return this.dims.get(id);
	}

	@Override
	public void setActiveDimension(int id) {
		this.activeDimension = this.dims.get(id);
	}

	@Override
	public IDimension getActiveDimension() {
		return this.activeDimension;
	}

	@Override
	public Collection<IDimension> getDimensions() {
		// return Collections.unmodifiableCollection();
		return null; // TODO: Fix
	}

}
