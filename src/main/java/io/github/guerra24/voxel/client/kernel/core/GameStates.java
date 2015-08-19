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

package io.github.guerra24.voxel.client.kernel.core;

import io.github.guerra24.voxel.client.kernel.menu.Button;
import io.github.guerra24.voxel.client.kernel.util.Logger;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * Game States
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.2 Build-55
 * @since 0.0.1 Build-52
 * @category Kernel
 */
public class GameStates {
	/**
	 * The game main loop bool
	 */
	public boolean loop;
	/**
	 * If the player is in game
	 */
	public boolean isPlaying;
	/**
	 * Game State
	 */
	public State state;

	/**
	 * Game States Constructor, Initializes the loop and the default game state
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public GameStates() {
		loop = true;
		state = State.MAINMENU;
	}

	/**
	 * Contains the game states values
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public enum State {
		GAME, MAINMENU, IN_PAUSE, LOADING_WORLD;
	}

	/**
	 * Updates the game state
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void switchStates() {
		if (state == State.MAINMENU && Button.isInButtonPlay()) {
			// Kernel.gameResources.SoundSystem.pause("MainMenuMusic");
			state = State.LOADING_WORLD;
			isPlaying = true;
			Random seed;
			if (KernelConstants.isCustomSeed) {
				seed = new Random(KernelConstants.seed.hashCode());
			} else {
				seed = new Random();
			}
			Kernel.world.startWorld("Mundo-1", Kernel.gameResources.camera,
					seed, 0);
			Kernel.gameResources.camera.setMouse();
			state = State.GAME;
		}

		if (state == State.MAINMENU && Button.isInButtonExit()) {
			loop = false;
		}

		if (state == State.IN_PAUSE && Button.backToMainMenu()) {
			// Kernel.gameResources.SoundSystem.rewind("MainMenuMusic");
			// Kernel.gameResources.SoundSystem.play("MainMenuMusic");
			Logger.log(Thread.currentThread(), "Saving World");
			for (int zr = -KernelConstants.genRadius; zr <= KernelConstants.genRadius; zr++) {
				int zz = Kernel.world.getzPlayChunk() + zr;
				for (int xr = -KernelConstants.genRadius; xr <= KernelConstants.genRadius; xr++) {
					int xx = Kernel.world.getxPlayChunk() + xr;
					if (zr * zr + xr * xr <= KernelConstants.genRadius
							* KernelConstants.genRadius) {
						if (Kernel.world.hasChunk(Kernel.world.dim, xx, zz)) {
							Kernel.world.saveChunk(Kernel.world.dim, xx, zz);
						}
					}
				}
			}
			Kernel.world.removeAll();
			Kernel.gameResources.camera.setPosition(new Vector3f(0, 80, 0));
			state = State.MAINMENU;
		}

		if (state == State.GAME && !Display.isActive()
				&& !KernelConstants.debug) {
			Kernel.gameResources.camera.unlockMouse();
			state = State.IN_PAUSE;
		}
		if (Display.isCloseRequested())
			loop = false;

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
