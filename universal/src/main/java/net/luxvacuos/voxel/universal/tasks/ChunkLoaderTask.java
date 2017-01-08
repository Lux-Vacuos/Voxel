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

package net.luxvacuos.voxel.universal.tasks;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.chunk.ChunkDataBuilder;
import net.luxvacuos.voxel.universal.world.chunk.ChunkSlice;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class ChunkLoaderTask implements Callable<ChunkData> {
	private NBTInputStream in = null;
	private boolean exists;

	public ChunkLoaderTask(IDimension dim, ChunkNode node) throws IOException, FileNotFoundException {
		String path = GlobalVariables.WORLD_PATH + dim.getWorldName() + "/" + dim.getID();
		String fullPath = path + "/" + "chunk_" + node.getX() + "_" + node.getZ() + ".dat";
		File file = new File(fullPath);

		if (this.exists = (file.exists() && file.length() != 0L)) {
			this.in = new NBTInputStream(new BufferedInputStream(new FileInputStream(file)));
		} else {
			new File(path).mkdirs();
		}
	}

	@Override
	public ChunkData call() throws Exception {
		ChunkDataBuilder builder = new ChunkDataBuilder();
		ChunkSlice slice;
		TagCompound root;
		
		if(this.exists) {
			root = new TagCompound(this.in, false);
			builder.setBlockMetadata(root.getCompound("BlockMetadata"));
			int slices = root.getInt("NumSlices");
			
			TagCompound sliceCompound;
			for(byte i = 0; i < slices; i++) {
				sliceCompound = root.getCompound("ChunkSlice-"+i);
				slice = new ChunkSlice(sliceCompound.getByte("Offset"));
				builder.setSlice(slice.getOffset(), slice);
				
				if(sliceCompound.getByte("Empty") == 0) {
					builder.setSliceData(slice.getOffset(), sliceCompound.getLongArray("BlockData"));
				}
				
				slice = null;
			}
		} else {
			for (byte i = 0; i < 16; i++) {
				slice = new ChunkSlice(i);
				builder.setSlice(i, slice);
				slice = null;
			}
			builder.setBlockMetadata(new TagCompound("BlockMetadata"));
		}
		
		return builder.build();

	}

}
