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

package net.luxvacuos.voxel.server.world.block.types;

import net.luxvacuos.voxel.server.world.Dimension;
import net.luxvacuos.voxel.server.world.block.BlockEntity;

public class BlockNode extends BlockEntity {

	public BlockNode(Integer x, Integer y, Integer z) {
		super(x, y, z);
		transparent = true;
	}

	public BlockNode() {
		super();
		transparent = true;
	}

	@Override
	public void update(Dimension dimension, float delta) {
	}

	@Override
	public byte getId() {
		return 18;
	}

}
