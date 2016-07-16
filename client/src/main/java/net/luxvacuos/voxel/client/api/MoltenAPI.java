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

package net.luxvacuos.voxel.client.api;

import com.badlogic.ashley.core.Entity;

import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.chunks.Chunk;

/**
 * Client side API. Holds all the methods used by the mods API that are called
 * using reflection by the universal API to maintain consistency between server
 * and client allowing mods to run in anywhere. Mods should never access this
 * class directly.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class MoltenAPI {

	public MoltenAPI() {
	}

	public void testPrint() {
		System.out.println("TEST");
	}

	public Chunk getChunk(int cx, int cy, int cz) {
		return GameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().getChunk(cx, cy,
				cz);
	}

	public void addChunk(Chunk chunk) {
		GameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().addChunk(chunk);
	}

	public void addEntity(Entity entity) {
		GameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
				.addEntity(entity);
	}

}
