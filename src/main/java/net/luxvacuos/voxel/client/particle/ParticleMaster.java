/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleRenderer;
import net.luxvacuos.voxel.client.resources.Loader;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.util.vector.Matrix4f;

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

	public void render(Camera camera, Matrix4f proj) {
		renderer.render(particles, camera, proj);
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
