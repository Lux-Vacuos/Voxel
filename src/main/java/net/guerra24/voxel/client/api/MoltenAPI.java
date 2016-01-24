/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.api;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;

import net.guerra24.voxel.client.api.mod.ModStateLoop;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.block.BlockBase;

public class MoltenAPI {

	private GameResources gm;
	private List<ModStateLoop> modStateLoops;

	public MoltenAPI(GameResources gm) {
		this.gm = gm;
		modStateLoops = new ArrayList<>();
	}

	public void registetEntity(Entity mob) {
		gm.getPhysicsEngine().addEntity(mob);
	}

	public void removeEntity(Entity entity) {
		gm.getPhysicsEngine().removeEntity(entity);
	}

	public void registerBlock(BlockBase block) {
		Block.registerBlock(block);
	}

	public void registerSaveData(String key, String value) {
		gm.getGameSettings().registerValue(key, value);
	}

	public String getSaveData(String key) {
		return gm.getGameSettings().getValue(key);
	}

	public void registerModStateLoop(ModStateLoop modStateLoop) {
		modStateLoops.add(modStateLoop);
	}

	/**
	 * NOT USE THIS!!!!!!
	 * 
	 * <p>
	 * This is used internally for calling all modStates
	 * </p>
	 * 
	 * @return
	 */
	public List<ModStateLoop> getModStateLoops() {
		return modStateLoops;
	}

}
