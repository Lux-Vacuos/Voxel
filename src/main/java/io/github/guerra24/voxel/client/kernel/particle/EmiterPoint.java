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

package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.resources.models.ModelTexture;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;

/**
 * Particle Emiter Point
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Particle
 */
public class EmiterPoint {

	/**
	 * Emiter Point Data
	 */
	private Vector3f origin;
	private TexturedModel model;
	private float spawnRationPerTick = 0;
	private int max;
	private float x, y, z;
	private RawModel rModel;
	private ModelTexture texture;

	/**
	 * Constructor
	 * 
	 * @param origin
	 *            Origin of particles
	 * @param loader
	 *            Loader
	 * @param x
	 *            Origin X
	 * @param y
	 *            Origin Y
	 * @param z
	 *            Origin Z
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public EmiterPoint(Vector3f origin, Loader loader, float x, float y, float z) {
		this.origin = origin;
		this.x = x;
		this.y = y;
		this.z = z;
		rModel = loader.getObjLoader().loadObjModel("particle", loader);
		texture = new ModelTexture(loader.loadTextureBlocks("particleTest"));
		model = new TexturedModel(rModel, texture);
		max = 40;
	}

	/**
	 * Spaw Particles
	 * 
	 * @param controller
	 *            ParticleController
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void spawnParticles(ParticleController controller) {
		origin = new Vector3f(x, y, z);
		spawnRationPerTick++;
		if (spawnRationPerTick >= max) {
			controller.getParticles()
					.add(new Particle(model, origin, 0, 0, 0, 2000, 0.4f, Maths.randFloat(), Maths.randFloat()));
			spawnRationPerTick = 0;
		}
	}
}
