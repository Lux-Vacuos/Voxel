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

package net.guerra24.voxel.api;

import java.util.HashMap;
import java.util.Map;

import net.guerra24.voxel.api.mod.ChunkHandler;
import net.guerra24.voxel.world.block.Block;
import net.guerra24.voxel.world.block.IBlock;

/**
 * World API
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category API
 */
public class WorldVAPI {

	public Map<Integer, ChunkHandler> modsWorld;

	public WorldVAPI() {
		modsWorld = new HashMap<Integer, ChunkHandler>();
	}

	/**
	 * Register the ChunkHandler
	 * 
	 * @param id
	 *            Use APIWorld.getLastId()
	 * @param handler
	 *            Handler
	 */
	public void registerChunkHandler(int id, ChunkHandler handler) {
		modsWorld.put(id, handler);
	}

	/**
	 * Gets the Mod from ID
	 * 
	 * @param id
	 *            ID
	 * @return Mod
	 * 
	 */
	public ChunkHandler getChunkHandler(int id) {
		return modsWorld.get(id);
	}

	/**
	 * Register a block to the world generation
	 * 
	 * @param id
	 *            Block id
	 * @param block
	 *            Block
	 */
	public void registerBlock(byte id, IBlock block) {
		Block.registerBlock(id, block);
	}

	/**
	 * Get last avaiable ID
	 * 
	 * @return ID
	 */
	public int getLastID() {
		return modsWorld.size();
	}

	/**
	 * Dispose the APIWorld
	 * 
	 */
	public void dispose() {
		modsWorld.clear();
	}

}
