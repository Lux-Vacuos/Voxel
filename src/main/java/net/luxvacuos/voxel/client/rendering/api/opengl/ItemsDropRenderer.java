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

import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.items.ItemDropBase;

public class ItemsDropRenderer {

	private List<ItemDropBase> items;
	private Tessellator tess;

	public ItemsDropRenderer(GameResources gm) throws Exception {
		items = new ArrayList<>();
		tess = new Tessellator(gm);
	}

	public void render(GameResources gm) {
		items.clear();
		for (Entity entity : gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine()
				.getEntities()) {
			if (entity instanceof ItemDropBase)
				items.add((ItemDropBase) entity);
		}
		doRender(gm);
	}

	private void doRender(GameResources gm) {
		tess.begin(BlocksResources.getTessellatorTextureAtlas().getTexture(), BlocksResources.getNormalMap(),
				BlocksResources.getHeightMap(), BlocksResources.getSpecularMap());
		for (ItemDropBase itemDropBase : items) {
			itemDropBase.generateModel(tess);
		}
		tess.end();
		tess.draw(gm);
	}

	public void cleanUp() {
		tess.cleanUp();
		items.clear();
	}

	public Tessellator getTess() {
		return tess;
	}
}
