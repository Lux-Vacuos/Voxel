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


package io.github.guerra24.voxel.client.kernel.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;

public class WorldHandler {

	private Map<Integer, DimensionalWorld> worlds;
	private int activeWorld;

	public WorldHandler() {
		worlds = new HashMap<Integer, DimensionalWorld>();
	}

	public void addWorld(int id, Random seed, VAPI api, GameResources gm) {
		if (!worldExist(id)) {
			DimensionalWorld world = new DimensionalWorld();
			world.startWorld("World-" + id, seed, id, api, gm);
			worlds.put(world.getWorldID(), world);
		}
		activeWorld = id;
	}

	public boolean worldExist(int id) {
		if (worlds.get(id) != null)
			return true;
		else
			return false;
	}

	public void removeWorld(int world, GameResources gm) {
		worlds.get(world).clearChunkDimension(gm);
	}

	public DimensionalWorld getWorld(int world) {
		return worlds.get(world);
	}

	public int getActiveWorld() {
		return activeWorld;
	}
}
