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

package io.github.guerra24.voxel.client.kernel;

import io.github.guerra24.voxel.client.menu.Button;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class GameStates {

	public boolean loop;
	public boolean isPlaying;
	public State state;

	public GameStates() {
		loop = true;
		state = State.MAINMENU;
	}

	public enum State {
		GAME, MAINMENU, IN_PAUSE;
	}

	public void switchStates() {
		if (state == State.MAINMENU && Button.isInButtonPlay()) {
			// Kernel.gameResources.SoundSystem.pause("MainMenuMusic");
			isPlaying = true;
			Kernel.world.startWorld();
			Kernel.gameResources.camera.setMouse();
			state = State.GAME;
		}

		if (state == State.MAINMENU && Button.isInButtonExit()) {
			loop = false;
		}

		if (state == State.IN_PAUSE && Button.backToMainMenu()) {
			// Kernel.gameResources.SoundSystem.rewind("MainMenuMusic");
			// Kernel.gameResources.SoundSystem.play("MainMenuMusic");
			Kernel.gameResources.waters.clear();
			Kernel.gameResources.cubes.clear();

			for (int x = 0; x < Kernel.world.chunks.length; x++) {
				for (int z = 0; z < Kernel.world.chunks.length; z++) {
					if (Kernel.world.chunks[x][z] != null) {
						Kernel.world.chunks[x][z].dispose();
						Kernel.world.chunks[x][z] = null;
					}
				}
				Kernel.gameResources.camera.setPosition(new Vector3f(0, 80, 0));
				state = State.MAINMENU;
			}
		}

		if (state == State.GAME && !Display.isActive()) {
			Kernel.gameResources.camera.unlockMouse();
			state = State.IN_PAUSE;
		}
		while (Keyboard.next()) {
			if (state == State.GAME && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Kernel.gameResources.camera.unlockMouse();
				state = State.IN_PAUSE;
			} else if (state == State.IN_PAUSE
					&& Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Kernel.gameResources.camera.setMouse();
				state = State.GAME;
			}
		}
	}
}
