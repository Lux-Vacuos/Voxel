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

package io.github.guerra24.voxel.client.kernel.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.world.World;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

import org.junit.Test;

public class KernelTest {
	/*@Test
	public void worldTest() {
		System.setProperty("org.lwjgl.librarypath", new File(
				"src/test/resources/natives").getAbsolutePath());
		Kernel kernel = new Kernel(true);
		World world = new World();
		assertEquals(world.chunks, Kernel.world.chunks);
		assertEquals(world.getClass(), kernel.world.getClass());
		kernel.dispose();
	}

	@Test
	public void cameraTest() {
		System.setProperty("org.lwjgl.librarypath", new File(
				"src/test/resources/natives").getAbsolutePath());
		Kernel kernel = new Kernel(true);
		Camera camera = new Camera();
		assertEquals(camera.getPosition(),
				kernel.gameResources.camera.getPosition());
		assertEquals(camera.getPitch(), kernel.gameResources.camera.getPitch(),
				0.0f);
		assertEquals(camera.getYaw(), kernel.gameResources.camera.getYaw(),
				0.0f);
		kernel.dispose();
	}

	@Test
	public void gameResourcesObjectTest() {
		System.setProperty("org.lwjgl.librarypath", new File(
				"src/test/resources/natives").getAbsolutePath());
		Kernel kernel = new Kernel(true);
		assertEquals(1, kernel.gameResources.allObjects.size());
		kernel.dispose();
	}
*/}
