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

package net.luxvacuos.voxel.universal.world.block;

import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.world.utils.BlockNode;

public interface IBlockInfo<T extends IBlock> {
	
	public String getName();
	
	public BlockMaterial getMaterial();
	
	public BoundingBox getBoundingBox(BlockNode pos);
	
	public T newInstance(BlockNode node);

}
