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

package net.luxvacuos.voxel.universal.world.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.luxvacuos.igl.Logger;

public class Blocks {

	private static final Map<String, IBlockInfo<?>> blocks = new HashMap<>();
	private static final Map<String, List<IBlockInfo<?>>> prefixedBlocks = new HashMap<>();

	private Blocks() {
	}

	public static void register(IBlockInfo<?> block) {
		blocks.put(block.getName(), block);

		List<IBlockInfo<?>> prefixBlocks = prefixedBlocks.get(block.getName().split(":")[0]);
		if (prefixBlocks != null)
			prefixBlocks.add(block);
		else {
			List<IBlockInfo<?>> newPrefixBlocks = new ArrayList<>();
			newPrefixBlocks.add(block);
			prefixedBlocks.put(block.getName().split(":")[0], newPrefixBlocks);
		}
		Logger.log("Registered block:" + block.getName().split(":")[0] + ":" + block.getName());
	}

	public static IBlockInfo<?> getBlockByName(String name) {
		if (blocks.containsKey(name))
			return blocks.get(name);
		else
			Logger.warn(name + " was not found");
		return null;
	}

	public static IBlockInfo<?> getBlockByBlock(IBlock block) {
		return blocks.get(block.getName());
	}

	public static Set<IBlockInfo<?>> getBlocksByPrefix(String prefix) {
		Set<IBlockInfo<?>> list = new HashSet<>();
		List<IBlockInfo<?>> prefixList = prefixedBlocks.get(prefix);
		if (prefix == null) {
			Logger.warn("There are no blocks with prefix: " + prefix);
			return list;
		}
		list.addAll(prefixList);
		Logger.log("Found " + list.size() + " blocks with prefix: " + prefix);
		return list;
	}

}
