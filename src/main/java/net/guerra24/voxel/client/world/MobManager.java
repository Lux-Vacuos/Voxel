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

package net.guerra24.voxel.client.world;

import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.api.ModInitialization;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.models.ModelTexture;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.resources.models.TexturedModel;
import net.guerra24.voxel.client.world.entities.IEntity;
import net.guerra24.voxel.client.world.entities.Player;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public abstract class MobManager {
	private List<IEntity> mobs = new ArrayList<IEntity>();
	private Player player;

	/**
	 * Constructor
	 * 
	 * @param gm
	 *            GameResources
	 */
	public MobManager(GameResources gm) {
		init(gm);
		init();
	}

	public void init(GameResources gm) {
		ModelTexture texture = new ModelTexture(gm.getLoader().loadTextureEntity("player"));
		RawModel model = gm.getLoader().getObjLoader().loadObjModel("player", gm.getLoader());
		player = new Player(new TexturedModel(model, texture), new Vector3f(0, 0, 0), 0, 0, 0, 1);
	}

	protected abstract void init();

	public void update(float delta, GameResources gm, IWorld world, ModInitialization api) {
		for (IEntity iEntity : mobs) {
			iEntity.update(delta, gm, world, api);
		}
	}

	public void registerMob(IEntity entity) {
		mobs.add(entity);
	}

	public void cleanUp() {
		mobs.clear();
		dispose();
	}

	protected abstract void dispose();

	public List<IEntity> getMobs() {
		return mobs;
	}

	public Player getPlayer() {
		return player;
	}

}
