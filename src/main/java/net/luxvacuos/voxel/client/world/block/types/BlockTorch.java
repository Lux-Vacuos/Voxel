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

package net.luxvacuos.voxel.client.world.block.types;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.block.BlockEntity;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.universal.util.vector.Vector3f;

public class BlockTorch extends BlockBase {

	public BlockTorch() {
		transparent = true;
		usesSingleModel = true;
	}

	@Override
	public byte getId() {
		return 9;
	}

	@Override
	public BlockEntity getSingleModel(Vector3f pos) {
		return new BlockEntity(BlocksResources.cubeTorch, pos, 0, 0, 0, 1, "SINGLE MODEL", getId());
	}

	@Override
	public BoundingBox getBoundingBox(Vector3f pos) {
		return new BoundingBox(new Vector3(pos.x + 0.3f, pos.y, pos.z + 0.3f),
				new Vector3(pos.x + 0.7f, pos.y + 0.5f, pos.z + 0.7f));
	}

}
