/*
S * The MIT License (MIT)
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

package net.guerra24.voxel.client.core;

import net.guerra24.voxel.client.graphics.opengl.Display;

public class WorldThread1 extends Thread {
	private final Voxel voxel;
	private long variableYieldTime, lastTime;
	private int fps = 20;

	public WorldThread1(Voxel voxel) {
		this.voxel = voxel;
	}

	@Override
	public void run() {
		float delta = 0;
		float accumulator = 0f;
		float interval = 1f / fps;
		while (voxel.getGameResources().getGlobalStates().loop) {
			delta = Display.getDeltaUpdate();
			accumulator += delta;
			while (accumulator >= interval) {
				voxel.update(interval);
				accumulator -= interval;
			}
			sync(fps);
		}
	}

	private void sync(int fps) {
		if (fps <= 0)
			return;
		long sleepTime = 1000000000 / fps;
		long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000 * 1000));
		long overSleep = 0;

		try {
			while (true) {
				long t = System.nanoTime() - lastTime;

				if (t < sleepTime - yieldTime) {
					Thread.sleep(1);
				} else if (t < sleepTime) {
					Thread.yield();
				} else {
					overSleep = t - sleepTime;
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);
			if (overSleep > variableYieldTime) {
				variableYieldTime = Math.min(variableYieldTime + 200 * 1000, sleepTime);
			} else if (overSleep < variableYieldTime - 200 * 1000) {
				variableYieldTime = Math.max(variableYieldTime - 2 * 1000, 0);
			}
		}
	}

}
