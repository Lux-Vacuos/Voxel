/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.kernel.api;

import io.github.guerra24.voxel.client.kernel.api.mod.ChunkHandler;
import io.github.guerra24.voxel.client.kernel.world.block.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * World API
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category API
 */
public class APIWorld {

	public static Map<Integer, ChunkHandler> modsWorld;

	public APIWorld() {
		modsWorld = new HashMap<Integer, ChunkHandler>();
	}

	/**
	 * Register the ChunkHandler
	 * 
	 * @param id
	 *            Use APIWorld.getLastId()
	 * @param handler
	 *            Handler
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void registerChunkHandler(int id, ChunkHandler handler) {
		modsWorld.put(id, handler);
	}

	/**
	 * Gets the Mod from ID
	 * 
	 * @param id
	 *            ID
	 * @return Mod
	 * @author Guerra24 <pablo230699@hotmail.com>
	 * 
	 */
	public static ChunkHandler getChunkHandler(int id) {
		return modsWorld.get(id);
	}

	/**
	 * Register a block to the world generation
	 * 
	 * @param id
	 *            Block id
	 * @param block
	 *            Block
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void registerBlock(byte id, Block block) {
		Block.registerBlock(id, block);
	}

	/**
	 * Get last avaiable ID
	 * 
	 * @return ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static int getLastID() {
		return modsWorld.size();
	}

	/**
	 * Dispose the APIWorld
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void dispose() {
		modsWorld.clear();
	}

}
