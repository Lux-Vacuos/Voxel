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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.world.chunk.ChunkSlice;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;

public class ChunkUnloaderTask implements Callable<IChunk> {
	private DataOutputStream out;
	private IChunk chunk;

	public ChunkUnloaderTask(IChunk chunk) throws IOException {
		this.chunk = chunk;
		
		String path = GlobalVariables.WORLD_PATH + chunk.getDimension().getWorldName()
				+ "/" + chunk.getDimension().getID() + "/"
				+ "chunk_" + chunk.getX() + "_" + chunk.getZ() + ".dat";
		
		File file = new File(path);
		if(!file.exists()) file.createNewFile(); //This shouldn't happen, but just in case it does
		
		this.out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}

	@Override
	public IChunk call() throws Exception {
		ImmutableArray<ChunkSlice> slices = this.chunk.getChunkData().getChunkSlices();
		this.out.writeInt(slices.size());
		
		int[] bda0 = null;
		long[] bda1 = null;
		boolean wroteLength = false;
		for(ChunkSlice slice : slices) {
			this.out.writeByte(slice.getOffset());
			
			if(!slice.isEmpty()) {
				this.out.writeBoolean(true);
				wroteLength = true;
				bda1 = slice.getBlockDataArray().getData();
				this.out.writeInt(bda1.length);
				for(int i = 0; i < bda1.length; i++)  this.out.writeLong(bda1[i]);
				bda1 = null;
			} else this.out.writeBoolean(false);
			
			if(slice.hasLightData()) {
				this.out.writeBoolean(true);
				bda0 = slice.getLightDataArray().getData(); //Set bda to the light data
				if(!wroteLength) this.out.writeInt(bda0.length); //Write the length of the light data array to the stream if needed
				for(int i = 0; i < bda0.length; i++)  this.out.writeInt(bda0[i]);
				bda0 = null;
			} else this.out.writeBoolean(false);
		}
		
		this.out.close();
		return this.chunk;
	}

}
