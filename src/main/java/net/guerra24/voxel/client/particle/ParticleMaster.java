package net.guerra24.voxel.client.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private Map<ParticleTexture, List<Particle>> particles;
	private ParticleRenderer renderer;

	private ParticleMaster() {
	}

	public void init(Loader loader, Matrix4f projectionMatrix) {
		particles = new HashMap<ParticleTexture, List<Particle>>();
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}

	public void update(float delta, Camera camera) {
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iterator = list.iterator();
			while (iterator.hasNext()) {
				Particle p = iterator.next();
				boolean stillAlive = p.update(delta, camera);
				if (!stillAlive) {
					iterator.remove();
					if (list.isEmpty())
						mapIterator.remove();
				}
			}
			InsertionSort.sortHighToLow(list);
		}
	}

	public void render(Camera camera) {
		renderer.render(particles, camera);
	}

	public void cleanUp() {
		renderer.cleanUp();
	}

	public void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getTexture());
		if (list == null) {
			list = new ArrayList<Particle>();
			particles.put(particle.getTexture(), list);

		}
		list.add(particle);
	}

}
