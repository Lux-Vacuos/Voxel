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

package net.luxvacuos.voxel.universal.ecs.components;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.lightengine.universal.ecs.components.LEComponent;

public class ChunkLoader implements LEComponent {

	private int chunkRadius = 10;
	
	public ChunkLoader() {
		
	}
	
	public ChunkLoader(int chunkRadius) {
		this.chunkRadius = chunkRadius;
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		if(compound.hasTagByName("ChunkRadius")) {
			this.chunkRadius = compound.getInt("ChunkRadius");
		}
	}

	@Override
	public TagCompound save() {
		return new CompoundBuilder().start("ChunkLoaderCompound").addInteger("ChunkRadius", this.chunkRadius).build();
	}

	public int getChunkRadius() {
		return this.chunkRadius;
	}

	public void setChunkRadius(int chunkRadius) {
		this.chunkRadius = chunkRadius;
	}

}
