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

package net.guerra24.voxel.client.core.states;

import java.util.Random;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.core.GlobalStates.GameState;

/**
 * Loading Screen State
 * 
 * @author danirod
 * @category Kernel
 */
public class LoadingState extends State {

	public LoadingState() {
		super(5);
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		Random seed;
		if (VoxelVariables.isCustomSeed) {
			seed = new Random(VoxelVariables.seed.hashCode());
		} else {
			seed = new Random();
		}
		voxel.getWorldsHandler().getActiveWorld().startWorld("World-0", seed, 0, voxel.getGameResources());
		gm.getCamera().setMouse();
		gm.getSoundSystem().rewind("menu1");
		gm.getSoundSystem().stop("menu1");
		gm.getSoundSystem().rewind("menu2");
		gm.getSoundSystem().stop("menu2");
		states.setState(GameState.GAME_SP);
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
	}

}
