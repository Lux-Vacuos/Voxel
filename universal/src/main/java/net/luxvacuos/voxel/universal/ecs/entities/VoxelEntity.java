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

package net.luxvacuos.voxel.universal.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.hackhalo2.nbt.stream.NBTOutputStream;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.ecs.components.NBTComponent;
import net.luxvacuos.voxel.universal.ecs.components.Name;
import net.luxvacuos.voxel.universal.ecs.components.UUIDComponent;
import net.luxvacuos.voxel.universal.util.IUpdatable;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class VoxelEntity extends Entity implements IUpdatable {

	public VoxelEntity(String name) {
		this.add(new Name(name));
		this.add(new UUIDComponent());
		this.add(new NBTComponent());
	}
	
	public VoxelEntity(String name, String uuid) {
		this.add(new Name(name));
		this.add(new UUIDComponent(uuid));
		this.add(new NBTComponent());
	}
	
	public VoxelEntity(TagCompound in) {
		this.add(new NBTComponent(in));
	}
	
	public void beforeUpdate(float delta) {
		
	}

	@Override
	public void update(float delta) {

	}
	
	public void afterUpdate(float delta) {
		
	}
	
	//XXX: This needs a better way to update
	public void updateDim(float delta, IDimension dim){
		
	}
	
	public void quickSave() {
		
	}
	
	public void save(NBTOutputStream out) {
		
	}

}
