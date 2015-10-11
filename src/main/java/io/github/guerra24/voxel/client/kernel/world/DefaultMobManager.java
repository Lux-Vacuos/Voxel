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

import java.util.ArrayList;
import java.util.List;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.entities.IEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Player;

public class DefaultMobManager implements IMobManagerController {
	private List<IEntity> mobs = new ArrayList<IEntity>();
	private Player player;

	/**
	 * Constructor
	 * 
	 * @param gm
	 *            GameResources
	 */
	public DefaultMobManager(GameResources gm) {
		init(gm);
	}

	@Override
	public void init(GameResources gm) {
		player = new Player(BlocksResources.cubeGlassUP, new Vector3f(0, 80, -4), 0, 0, 0, 1);
	}

	@Override
	public void update(float delta, GameResources gm, GuiResources gi, DimensionalWorld world, VAPI api) {
		for (int x = 0; x < mobs.size(); x++) {
			mobs.get(x).update(delta, gm, gi, world, api);
		}
	}

	@Override
	public void registerMob(IEntity entity) {
		mobs.add(entity);
	}

	@Override
	public void dispose() {
		mobs.clear();
	}

	@Override
	public List<IEntity> getMobs() {
		return mobs;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

}
