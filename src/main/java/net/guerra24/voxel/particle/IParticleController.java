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

package net.guerra24.voxel.particle;

import java.util.List;
import java.util.Map;

import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.GuiResources;
import net.guerra24.voxel.resources.Loader;
import net.guerra24.voxel.resources.models.TexturedModel;
import net.guerra24.voxel.world.DimensionalWorld;

/**
 * Particle Controller
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Particle
 */
public interface IParticleController {
	/**
	 * Initialize the Particle
	 * 
	 * @param loader
	 *            Loader
	 */
	public void init(Loader loader);

	/**
	 * Render Particles
	 * 
	 * @param gm
	 *            GameResources
	 */
	public void render(GameResources gm);

	/**
	 * Render Particles
	 * 
	 * @param particles
	 *            Batch of particles
	 * @param gm
	 *            GameResources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderParticles(Map<TexturedModel, List<Particle>> particles, GameResources gm);

	/**
	 * Update Particles
	 * 
	 * @param delta
	 *            Game Delta
	 * @param gm
	 *            GameResources
	 * @param gi
	 *            GuiResources
	 * @param world
	 *            Dimensional World
	 */
	public void update(float delta, GameResources gm, GuiResources gi, DimensionalWorld world);

	/**
	 * Dispose Particle Data
	 */
	public void dispose();
}
