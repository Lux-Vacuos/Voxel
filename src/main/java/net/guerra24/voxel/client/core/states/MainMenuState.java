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

package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.graphics.VectorsRendering;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.resources.GameResources;

/**
 * Main Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class MainMenuState implements State {

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		Display.beingNVGFrame();
		gm.getMenuSystem().mainMenu.render();
		VectorsRendering.renderMouse();
		Display.endNVGFrame();
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		if (gm.getMenuSystem().mainMenu.getPlayButton().pressed()) {
			states.setState(GameState.LOADING_WORLD);
		} else if (gm.getMenuSystem().mainMenu.getExitButton().pressed()) {
			states.loop = false;
		} else if (gm.getMenuSystem().mainMenu.getOptionsButton().pressed()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			states.setState(GameState.OPTIONS);
		}
		gm.getMenuSystem().mainMenu.update();
	}

}
