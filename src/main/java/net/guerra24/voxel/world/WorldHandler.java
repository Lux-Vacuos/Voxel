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

package net.guerra24.voxel.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.guerra24.voxel.api.VAPI;
import net.guerra24.voxel.resources.GameResources;

/**
 * World Handler
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public class WorldHandler {

	/**
	 * World Hanlder Data
	 */
	private Map<Integer, DimensionalWorld> worlds;
	private int activeWorld;

	/**
	 * Constructor
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public WorldHandler() {
		worlds = new HashMap<Integer, DimensionalWorld>();
	}

	/**
	 * Add a world
	 * 
	 * @param id
	 *            Id of the World
	 * @param seed
	 *            Seed of the World
	 * @param api
	 *            Voxel API
	 * @param gm
	 *            Game Resources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void addWorld(int id, Random seed, VAPI api, GameResources gm) {
		if (!worldExist(id)) {
			DimensionalWorld world = new DimensionalWorld();
			world.startWorld("World-" + id, seed, id, api, gm);
			worlds.put(world.getWorldID(), world);
		}
		activeWorld = id;
	}

	/**
	 * Check if a world Exist
	 * 
	 * @param id
	 *            World ID
	 * @return true if exist
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public boolean worldExist(int id) {
		if (worlds.get(id) != null)
			return true;
		else
			return false;
	}

	/**
	 * Remove World
	 * 
	 * @param world
	 *            World ID
	 * @param gm
	 *            Game Resources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void removeWorld(int world, GameResources gm) {
		worlds.get(world).clearChunkDimension(gm);
	}

	/**
	 * Get Dimensional World
	 * 
	 * @param world
	 *            World ID
	 * @return Dimensional World
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public DimensionalWorld getWorld(int world) {
		return worlds.get(world);
	}

	/**
	 * Get Active World
	 * 
	 * @return Active World ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public int getActiveWorld() {
		return activeWorld;
	}
}
