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

import java.util.UUID;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

public class UUIDComponent implements VoxelComponent {
	
	private UUID uuid;

	public UUIDComponent() {
		this.uuid = UUID.randomUUID();
	}
	
	public UUIDComponent(String uuidString) {
		this.uuid = UUID.fromString(uuidString);
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		String uuidString = compound.getString("UUID");
		this.uuid = UUID.fromString(uuidString);
	}

	@Override
	public TagCompound save() {
		return new CompoundBuilder().start("UUIDComponent").addString("UUID", this.uuid.toString()).build();
	}

}
