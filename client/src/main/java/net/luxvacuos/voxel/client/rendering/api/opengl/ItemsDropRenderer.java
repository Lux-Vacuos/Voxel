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

package net.luxvacuos.voxel.client.rendering.api.opengl;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.items.ItemDrop;

public class ItemsDropRenderer {

	private List<ItemDrop> items;
	private Tessellator tess;

	public ItemsDropRenderer(Matrix4d projectionMatrix, Matrix4d shadowProjectionMatrix) {
		items = new ArrayList<>();
		tess = new Tessellator(projectionMatrix, shadowProjectionMatrix);
	}

	public void render(ImmutableArray<Entity> immutableArray, Camera camera, Camera sunCamera,
			ClientWorldSimulation clientWorldSimulation, Matrix4d projectionMatrix, int shadowMap, int shadowData) {
		items.clear();
		for (Entity entity : immutableArray) {
			if (entity instanceof ItemDrop)
				items.add((ItemDrop) entity);
		}
		doRender(camera, sunCamera, clientWorldSimulation, projectionMatrix, shadowMap, shadowData);
	}

	private void doRender(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation,
			Matrix4d projectionMatrix, int shadowMap, int shadowData) {
		tess.begin(BlocksResources.getTessellatorTextureAtlas().getTexture(), BlocksResources.getNormalMap(),
				BlocksResources.getHeightMap(), BlocksResources.getPbrMap());
		for (ItemDrop itemDrop : items) {
			itemDrop.generateModel(tess);
		}
		tess.end();
		tess.draw(camera, sunCamera, clientWorldSimulation, projectionMatrix, shadowMap, shadowData, false);
	}

	public void cleanUp() {
		tess.cleanUp();
		items.clear();
	}

	public Tessellator getTess() {
		return tess;
	}
}
