package io.github.guerra24.voxel.client.kernel.particle;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.guerra24.voxel.client.kernel.graphics.ParticleRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.VoxelGL33;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.ParticleShader;
import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;

public class ParticleController implements IParticleController {

	private List<Particle> particles;
	private Map<TexturedModel, List<Particle>> particleBatcher;
	private ParticleRenderer renderer;
	private ParticleShader shader;
	private EmiterPoint emiter;

	public ParticleController(Loader loader) {
		init(loader);
	}

	@Override
	public void init(Loader loader) {
		particles = new ArrayList<Particle>();
		particleBatcher = new HashMap<TexturedModel, List<Particle>>();
		shader = new ParticleShader();
		renderer = new ParticleRenderer(shader);
		emiter = new EmiterPoint(new Vector3f(10, 70, 0), loader, 10, 70, 0);
	}

	@Override
	public void render(GameControllers gm) {
		for (Particle particle : particles) {
			if (gm.getFrustum().pointInFrustum(particle.getPosition().x, particle.getPosition().y,
					particle.getPosition().z))
				processParticle(particle);
		}
		renderParticles(particleBatcher, gm);
	}

	private void renderParticles(Map<TexturedModel, List<Particle>> particles, GameControllers gm) {
		VoxelGL33.glEnable(GL_BLEND);
		VoxelGL33.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		shader.start();
		shader.loadProjectionMatrix(gm.getRenderer().getProjectionMatrix());
		shader.loadviewMatrix(gm.getCamera());
		renderer.render(particles, gm);
		particleBatcher.clear();
		shader.stop();
		VoxelGL33.glDisable(GL_BLEND);
	}

	@Override
	public void update(float delta, GameControllers gm, GuiResources gi, DimensionalWorld world) {
		emiter.spawnParticles(this);
		for (int x = 0; x < particles.size(); x++) {
			particles.get(x).update(delta, gm, gi, world);
			if (particles.get(x).getLifeTime() <= 0)
				particles.remove(x);
		}
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

	public List<Particle> getParticles() {
		return particles;
	}

}
