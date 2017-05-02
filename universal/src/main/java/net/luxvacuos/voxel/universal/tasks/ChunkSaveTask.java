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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.concurrent.Callable;

import com.badlogic.ashley.utils.ImmutableArray;
import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.stream.NBTOutputStream;

import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.world.chunk.ChunkSlice;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;

public class ChunkSaveTask implements Callable<Void> {
	private Collection<IChunk> chunks;

	public ChunkSaveTask(Collection<IChunk> chunks) {
		this.chunks = chunks;
	}

	@Override
	public Void call() throws Exception {
		String path;
		CompoundBuilder rootCompound = new CompoundBuilder();
		File file;
		NBTOutputStream out;
		for(IChunk chunk : this.chunks) {
			path = GlobalVariables.REGISTRY.getRegistryItem("/Voxel/Settings/World/directory") + chunk.getDimension().getWorldName()
					+ "/" + chunk.getDimension().getID() + "/"
					+ "chunk_" + chunk.getX() + "_" + chunk.getZ() + ".dat";

			file = new File(path);

			out = new NBTOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

			//Write the chunk coords to the file
			rootCompound.addInteger("ChunkX", chunk.getX()).addInteger("ChunkZ", chunk.getZ());
			
			//Write the Complex Block Metadata
			rootCompound.addCompound(chunk.getChunkData().getComplexBlockMetadata());

			ImmutableArray<ChunkSlice> slices = chunk.getChunkData().getChunkSlices();

			//Write the number of Chunk Slices in the Chunk
			rootCompound.addInteger("NumSlices", slices.size());

			CompoundBuilder sliceCompound = new CompoundBuilder();
			int i = 0;
			for(ChunkSlice slice : slices) {
				sliceCompound.start("ChunkSlice-"+i);
				sliceCompound.addByte("Offset", slice.getOffset());
				sliceCompound.addBoolean("Empty", slice.isEmpty());

				if(!slice.isEmpty())
					sliceCompound.addLongArray("BlockData", slice.getBlockDataArray().getData());

				rootCompound.addCompound(sliceCompound);
				i++;
			}

			rootCompound.build().writeNBT(out, false);
			out.flush();
		}

		return null;
	}

}
