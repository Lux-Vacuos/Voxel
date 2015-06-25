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

package io.github.guerra24.voxel.client.menu;

import io.github.guerra24.voxel.client.kernel.GameStates.State;
import io.github.guerra24.voxel.client.kernel.Kernel;

import org.lwjgl.input.Mouse;

public class MenuScreen {

	public static boolean selected = false;
	public static boolean isPlaying = false;

	public static void worldSelected() {
		while (Mouse.next()) {
			if (Button.isWorldSelected() && selected) {
				Kernel.gameResources.gameStates.state = State.GAME;
				//Kernel.gameResources.SoundSystem.pause("MainMenuMusic");

				if (Kernel.isLoading && !isPlaying) {
					isPlaying = true;
				} else if (!Kernel.isLoading && !isPlaying) {
					Kernel.world.startWorld();
					isPlaying = true;
				}
				Kernel.gameResources.camera.setMouse();
			}
			if (Button.isWorldSelected() && !selected) {
				Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
				Kernel.gameResources.guis3
						.remove(Kernel.guiResources.wnoselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.wselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.add(Kernel.guiResources.world);
				selected = true;
			}
			if (Button.isWorldNotSelected() && selected) {
				Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.wselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.wnoselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.add(Kernel.guiResources.world);
				selected = false;
			}
		}
	}

}
