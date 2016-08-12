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

import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.block.BlockEntity;
import net.luxvacuos.voxel.client.world.block.BlocksResources;

public class BlockNode extends BlockEntity {

	public BlockNode(Integer x, Integer y, Integer z) {
		super(x, y, z, BlocksResources.getNode());
		transparent = true;
		objModel = true;
	}

	public BlockNode() {
		super();
		transparent = true;
		objModel = true;
	}
	
	@Override
	public void update(Dimension dimension, float delta) {
		getModel().rotX += 16 * delta;
		getModel().rotY += 20 * delta;
		getModel().rotZ += 24 * delta;
	}

	@Override
	public void init() {
		setModel(BlocksResources.getNode());
	}

	@Override
	public byte getId() {
		return 18;
	}

}
