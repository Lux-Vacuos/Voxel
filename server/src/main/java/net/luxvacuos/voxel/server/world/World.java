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

package net.luxvacuos.voxel.server.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.server.core.ServerVariables;
import net.luxvacuos.voxel.server.resources.ServerGameResources;

public abstract class World {

	protected String name;
	protected Dimension activeDimension;

	private Map<Integer, Dimension> dimensions;
	private File file;
	private boolean saved = false;

	public World(String name) {
		this.name = name;
		dimensions = new HashMap<>();
		file = new File(ServerVariables.WORLD_PATH + name + "/world.dat");
	}

	public void init() {
		Logger.log("Loading World: " + name);
		saved = false;
		load(ServerGameResources.getInstance());
		localInit(ServerGameResources.getInstance());
		Logger.log("Load Completed");
	}

	protected abstract void localInit(ServerGameResources gm);

	private void load(ServerGameResources gm) {
		if (file.exists()) {
			Input input;
			try {
				input = new Input(new FileInputStream(file));
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void save() {
		Output output;
		try {
			output = new Output(new FileOutputStream(file));
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addDimension(Dimension dim) {
		dimensions.put(dim.getDimensionID(), dim);
	}

	public Dimension getDimension(int id) {
		return dimensions.get(id);
	}

	public void setActiveDimension(Dimension dim) {
		this.activeDimension = dim;
	}

	public void switchDimension(int id, ServerGameResources gm) {
		activeDimension = getDimension(id);
	}

	public Dimension getActiveDimension() {
		return activeDimension;
	}

	public String getName() {
		return name;
	}

	public void dispose() {
		if (!saved) {
			Logger.log("Saving " + name);
			save();
			for (Dimension dim : dimensions.values()) {
				dim.clearDimension();
			}
			saved = true;
		}
	}

	public Map<Integer, Dimension> getDimensions() {
		return dimensions;
	}
}
