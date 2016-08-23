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

package net.luxvacuos.voxel.universal.kryo;

import com.badlogic.ashley.utils.ImmutableArray;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.chunk.ChunkDataBuilder;
import net.luxvacuos.voxel.universal.world.chunk.ChunkSlice;
import net.luxvacuos.voxel.universal.world.utils.BlockDataArray;

public class ChunkDataSerializer extends Serializer<ChunkData> {

	@Override
	public void write(Kryo kryo, Output output, ChunkData object) {
		ImmutableArray<ChunkSlice> slices = object.getChunkSlices();
		output.writeInt(slices.size()); //Write the size of the array so we can read it back in correctly

		BlockDataArray bda = null;
		boolean wroteLength = false;
		for(ChunkSlice slice : slices) {
			output.writeByte(slice.yOffset); //Write the offset to the stream

			if(!slice.isEmpty()) {
				output.writeBoolean(true);
				wroteLength = true;
				bda = slice.getBlockDataArray(); //Set bda to the block data
				output.writeInt(bda.getData().length); //Write the length of the block data array to the stream
				output.writeInts(bda.getData()); //Write the data to the array
				bda = null;
			} else output.writeBoolean(false);

			if(slice.hasLightData()) {
				output.writeBoolean(true);
				bda = slice.getLightDataArray(); //Set bda to the light data
				if(!wroteLength) output.writeInt(bda.getData().length); //Write the length of the light data array to the stream if needed
				output.writeInts(bda.getData()); //write the data to the array
				bda = null;
			} else output.writeBoolean(false);
		}

	}

	@Override
	public ChunkData read(Kryo kryo, Input input, Class<ChunkData> type) {
		ChunkDataBuilder builder = new ChunkDataBuilder();
		ChunkSlice slice;
		int length = 0;
		byte offset;
		int[] array = null;
		
		int numSlices = input.readInt();
		for(int i = 0; i < numSlices; i++) {
			offset = input.readByte();
			slice = new ChunkSlice(offset);
			
			if(input.readBoolean()) {
				length = input.readInt();
				array = input.readInts(length);
				slice.setBlockDataArray(new BlockDataArray(array));
				array = null;
			}
			
			if(input.readBoolean()) {
				if(length == 0) length = input.readInt();
				array = input.readInts(length);
				slice.setLightDataArray(new BlockDataArray(array));
				array = null;
			}
			
			builder.setSlice(offset, slice);
			slice = null;
		}
		
		return builder.build();
	}

}
