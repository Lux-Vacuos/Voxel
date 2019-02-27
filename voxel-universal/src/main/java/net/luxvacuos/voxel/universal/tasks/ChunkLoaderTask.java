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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Callable;

import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.chunk.ChunkDataBuilder;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class ChunkLoaderTask implements Callable<ChunkData> {
	private NBTInputStream in = null;
	private File file;
	private String path;

	public ChunkLoaderTask(IDimension dim, ChunkNode node) {
		path = CoreSubsystem.REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/directory")) + dim.getWorldName()
				+ "/" + dim.getID();
		String fullPath = path + "/" + node.getX() + "_" + node.getY() + "_" + node.getZ() + ".dat";
		file = new File(fullPath);
	}

	@Override
	public ChunkData call() throws Exception {
		ChunkDataBuilder builder = new ChunkDataBuilder();
		TagCompound root;
		// So.. moving file checking heavily improves performance on windows
		if (file.exists() && file.length() != 0L) {
			this.in = new NBTInputStream(new BufferedInputStream(new FileInputStream(file)));
			root = new TagCompound(this.in, false);
			// Load Complex Block Metadata
			builder.setBlockEntityData(root.getCompound("BlockMetadata"));
			builder.setEmpty(root.getByte("Empty") == 1);

			// Load Blocks
			builder.setBlocks(root.getCompound("Blocks"));

			this.in.close();
		}

		return builder.build();

	}

}
