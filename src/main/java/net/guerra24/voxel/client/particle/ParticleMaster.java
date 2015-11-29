package net.guerra24.voxel.client.particle;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.guerra24.voxel.client.graphics.ParticleRenderer;
import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

public class ParticleMaster {

	private static ParticleMaster instance = null;

	public static ParticleMaster getInstance() {
		if (instance == null) {
			instance = new ParticleMaster();
		}
		return instance;
	}

	private Queue<Particle> particles;
	private ParticleRenderer renderer;

	private ParticleMaster() {
	}

	public void init(Loader loader, Matrix4f projectionMatrix) {
		particles = new ConcurrentLinkedQueue<Particle>();
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}

	public void update(float delta) {
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle p = iterator.next();
			boolean stillAlive = p.update(delta);
			if (!stillAlive)
				iterator.remove();
		}

	}

	public void render(Camera camera) {
		renderer.render(particles, camera);
	}

	public void cleanUp() {
		renderer.cleanUp();
	}

	public void addParticle(Particle particle) {
		particles.add(particle);
	}

}
