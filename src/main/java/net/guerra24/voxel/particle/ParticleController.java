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

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.guerra24.voxel.graphics.ParticleRenderer;
import net.guerra24.voxel.graphics.shaders.ParticleShader;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.GuiResources;
import net.guerra24.voxel.resources.Loader;
import net.guerra24.voxel.resources.models.TexturedModel;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.world.IWorld;

/**
 * Particle Controller
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class ParticleController implements IParticleController {

	/**
	 * Particle Controller Data
	 */
	private Queue<Particle> particles;
	private Map<TexturedModel, List<Particle>> particleBatcher;
	private ParticleRenderer renderer;
	private ParticleShader shader;
	private EmiterPoint emiter;

	/**
	 * Constructor
	 * 
	 * @param loader
	 */
	public ParticleController(Loader loader) {
		init(loader);
	}

	@Override
	public void init(Loader loader) {
		particles = new ConcurrentLinkedQueue<Particle>();
		particleBatcher = new HashMap<TexturedModel, List<Particle>>();
		shader = new ParticleShader();
		renderer = new ParticleRenderer(shader);
		emiter = new EmiterPoint(new Vector3f(10, 70, 0), loader, 10, 70, 0);
	}

	@Override
	public void render(GameResources gm) {
		for (Particle particle : particles) {
			if (gm.getFrustum().pointInFrustum(particle.getPosition().x, particle.getPosition().y,
					particle.getPosition().z))
				processParticle(particle);
		}
		renderParticles(particleBatcher, gm);
	}

	@Override
	public void renderParticles(Map<TexturedModel, List<Particle>> particles, GameResources gm) {
		glEnable(GL_BLEND);
		shader.start();
		shader.loadProjectionMatrix(gm.getRenderer().getProjectionMatrix());
		shader.loadviewMatrix(gm.getCamera());
		renderer.render(particles, gm);
		particleBatcher.clear();
		shader.stop();
		glDisable(GL_BLEND);
	}

	@Override
	public void update(float delta, GameResources gm, GuiResources gi, IWorld world) {
		emiter.spawnParticles(this);
		List<Particle> tempParticles = new ArrayList<Particle>(particles);
		for (int x = 0; x < tempParticles.size(); x++) {
			tempParticles.get(x).update(delta, gm, gi, world);
			if (tempParticles.get(x).getLifeTime() <= 0)
				tempParticles.remove(x);
		}
		particles.clear();
		particles.addAll(tempParticles);
	}

	@Override
	public void dispose() {
		particles.clear();
		particleBatcher.clear();
		shader.cleanUp();
	}

	/**
	 * Add the Entity to the batcher map
	 * 
	 * @param entity
	 *            An Entity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void processParticle(Particle particle) {
		TexturedModel entityModel = particle.getModel();
		List<Particle> batch = particleBatcher.get(entityModel);
		if (batch != null) {
			batch.add(particle);
		} else {
			List<Particle> newBatch = new ArrayList<Particle>();
			newBatch.add(particle);
			particleBatcher.put(entityModel, newBatch);
		}
	}

	public Queue<Particle> getParticles() {
		return particles;
	}

}
