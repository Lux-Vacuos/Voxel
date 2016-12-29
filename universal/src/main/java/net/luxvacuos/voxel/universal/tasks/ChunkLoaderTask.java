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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;

import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.util.Pair;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.chunk.ChunkDataBuilder;
import net.luxvacuos.voxel.universal.world.chunk.ChunkSlice;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.BlockIntDataArray;
import net.luxvacuos.voxel.universal.world.utils.BlockLongDataArray;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class ChunkLoaderTask implements Callable<Pair<ChunkNode, ChunkData>> {
	private DataInputStream in = null;
	private ChunkNode node;
	private boolean exists;

	public ChunkLoaderTask(IDimension dim, ChunkNode node) throws IOException, FileNotFoundException {
		this.node = node;

		String path = GlobalVariables.WORLD_PATH + dim.getWorldName() + "/" + dim.getID();
		String fullPath = path + "/" + "chunk_" + node.getX() + "_" + node.getZ() + ".dat";
		File file = new File(fullPath);

		if (this.exists = file.exists()) {
			this.in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		} else {
			new File(path).mkdirs();
		}
	}

	@Override
	public Pair<ChunkNode, ChunkData> call() throws Exception {
		ChunkDataBuilder builder = new ChunkDataBuilder();
		ChunkSlice slice;
		int length = 0;
		byte offset;
		int[] array0 = null;
		long[] array1 = null;
		int numSlices;

		if (this.exists) {
			numSlices = this.in.readInt();
			for (int i = 0; i < numSlices; i++) {
				offset = this.in.readByte();
				slice = new ChunkSlice(offset);

				if (this.in.readBoolean()) {
					length = this.in.readInt();
					array1 = new long[length];
					for (int input = 0; input < length; input++)
						array1[input] = this.in.readLong();
					slice.setBlockDataArray(new BlockLongDataArray(array1));
					array1 = null;
				}

				if (this.in.readBoolean()) {
					if (length == 0)
						length = this.in.readInt();
					array0 = new int[length];
					for (int input = 0; input < length; input++)
						array0[input] = this.in.readInt();
					slice.setLightDataArray(new BlockIntDataArray(array0));
					array0 = null;
				}

				builder.setSlice(offset, slice);
				slice = null;
			}

			this.in.close();

			return new Pair<ChunkNode, ChunkData>(this.node, builder.build());
		} else {
			for (byte i = 0; i < 16; i++) {
				slice = new ChunkSlice(i);
				builder.setSlice(i, slice);
				slice = null;
			}

			return new Pair<ChunkNode, ChunkData>(this.node, builder.build());
		}
	}

}
