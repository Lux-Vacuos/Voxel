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

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.esotericsoftware.kryonet.util.ObjectIntMap;

import net.luxvacuos.igl.Logger;

public final class Blocks {
	private static final IntMap<IBlock> blocks = new IntMap<IBlock>(64);
	private static final ObjectIntMap<String> blockNameToID = new ObjectIntMap<String>(64);
	private static final Array<String> registeredPrefixes = new Array<String>();
	private static int index = 0;
	private static String prefix = null;

	private Blocks() {
	}

	public static void startRegister(String prefix) {
		String lowercase = prefix.toLowerCase();

		if (!registeredPrefixes.contains(lowercase, true)) {
			Logger.log("Registering new prefix '" + lowercase + "'");
			registeredPrefixes.add(lowercase);
		}

		Blocks.prefix = lowercase;
	}

	public static void register(IBlock block) {
		if (prefix == null || block.getName() == null)
			throw new IllegalStateException("Cannot register blocks with null prefix or block name!");

		String prefixedBlockName = (prefix + ":" + block.getName()).toLowerCase();

		if (block instanceof BlockBase)
			((BlockBase) block).setID(index);
		else if (block instanceof AbstractBlockEntityBase)
			((AbstractBlockEntityBase) block).setID(index);
		else
			throw new IllegalStateException("Supplied block " + block.getName()
					+ " has an unsupported base class IBlock implementation " + block.getClass().getSuperclass());

		index++;

		Logger.log("Registering block '" + prefixedBlockName + "' with ID " + block.getID());

		blockNameToID.put(prefixedBlockName, block.getID());
		blocks.put(block.getID(), block);
	}

	public static void finishRegister() {
		prefix = null;
		index += 100;
	}

	public static IBlock getBlockByID(int id) {
		if (blocks.containsKey(id))
			return blocks.get(id);
		else {
			Logger.warn("ID " + id + " did not return any blocks from registry!");
			return null;
		}
	}

	public static IBlock getBlockByName(String name) {
		String lowercase = name.toLowerCase();

		if (isNamePrefixed(lowercase)) {
			if (blockNameToID.containsKey(lowercase))
				return blocks.get(blockNameToID.get(lowercase, 0));
			else {
				Logger.warn("Prefixed name '" + name + "' did not return any blocks from registry!");
				return null;
			}
		}

		String prefixed;
		synchronized (registeredPrefixes) {
			for (String prefix : registeredPrefixes) {
				prefixed = prefix + ":" + lowercase;
				if (blockNameToID.containsKey(prefixed))
					return blocks.get(blockNameToID.get(prefixed, 0));
			}
		}

		Logger.warn("Name '" + name + "' did not return any blocks from registry!");
		return null;
	}

	public static Set<IBlock> getAllBlocksByName(String name) {
		Set<IBlock> list = new HashSet<IBlock>();
		String lowercase = name.toLowerCase();

		if (isNamePrefixed(lowercase)) {
			if (blockNameToID.containsKey(lowercase))
				list.add(blocks.get(blockNameToID.get(lowercase, 0)));
			else {
				Logger.warn("Prefixed name '" + name + "' did not return any blocks from registry!");
			}
		} else {
			String prefixed;
			synchronized (registeredPrefixes) {
				for (String prefix : registeredPrefixes) {
					prefixed = prefix + ":" + lowercase;
					if (blockNameToID.containsKey(prefixed))
						list.add(blocks.get(blockNameToID.get(prefixed, 0)));
				}
			}
		}

		Logger.log("Found " + list.size() + " blocks with name '" + name + "' in Registry");
		return list;
	}

	private static boolean isNamePrefixed(String name) {
		return name.contains(":");
	}

}
