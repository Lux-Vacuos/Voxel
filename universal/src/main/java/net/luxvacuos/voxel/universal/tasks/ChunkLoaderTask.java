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

package net.luxvacuos.voxel.universal.tasks;

import java.util.concurrent.Callable;

import net.luxvacuos.voxel.universal.util.Pair;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class ChunkLoaderTask implements Callable<Pair<ChunkNode, ChunkData>> {
	private ChunkNode node;
	
	public ChunkLoaderTask(ChunkNode node) {
		this.node = node;
	}

	@Override
	public Pair<ChunkNode, ChunkData> call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
