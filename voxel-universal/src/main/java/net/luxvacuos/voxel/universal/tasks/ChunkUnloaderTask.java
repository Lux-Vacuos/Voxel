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

package net.luxvacuos.voxel.universal.tasks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.stream.NBTOutputStream;

import net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;

public class ChunkUnloaderTask implements Callable<IChunk> {
	private IChunk chunk;

	public ChunkUnloaderTask(IChunk chunk) throws IOException {
		this.chunk = chunk;

	}

	@Override
	public IChunk call() throws Exception {
		String path = CoreSubsystem.REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/directory"))
				+ chunk.getDimension().getWorldName() + "/" + chunk.getDimension().getID() + "/" + "chunk_"
				+ chunk.getX() + "_" + chunk.getY() + "_" + chunk.getZ() + ".dat";

		File file = new File(path);
		if (!file.exists())
			file.createNewFile(); // This shouldn't happen, but just in case it does

		NBTOutputStream out = new NBTOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		CompoundBuilder rootCompound = new CompoundBuilder().start();
		// Write the chunk coords to the file
		rootCompound.addInteger("ChunkX", chunk.getX()).addInteger("ChunkY", chunk.getY()).addInteger("ChunkZ",
				chunk.getZ());

		rootCompound.addBoolean("Empty", chunk.getChunkData().isEmpty());

		// Write the Complex Block Metadata
		rootCompound.addCompound(chunk.getChunkData().getBlockEntityData());

		// Write Blocks
		CompoundBuilder blocksCompound = new CompoundBuilder().start("Blocks");
		for (int i = 0; i < 4096; ++i) {
			CompoundBuilder blockCompound = new CompoundBuilder().start(Integer.toString(i));
			IBlock b = chunk.getChunkData().getBlocks()[i];
			blockCompound.addString("Name", b.getName());
			blockCompound.addInteger("Metadata", b.getMetadata());
			blocksCompound.addCompound(blockCompound);
		}
		rootCompound.addCompound(blocksCompound);

		rootCompound.build().writeNBT(out, false);
		out.close();
		return this.chunk;
	}

}
