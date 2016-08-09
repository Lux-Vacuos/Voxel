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

package net.luxvacuos.voxel.client.world.items;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.entities.components.CollisionComponent;
import net.luxvacuos.voxel.client.world.entities.components.VelocityComponent;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Scale;

public class ItemDrop extends Entity {

	private BlockBase block;
	private VelocityComponent velocityComponent;
	private CollisionComponent collisionComponent;

	public ItemDrop(Vector3f pos, BlockBase block, float scale) {
		velocityComponent = new VelocityComponent();
		collisionComponent = new CollisionComponent();
		this.add(new Position(pos));
		this.add(new Scale(scale));
		this.add(velocityComponent);
		this.add(collisionComponent);
		this.block = block;
		collisionComponent.min = new Vector3(-0.2f, -0.2f, -0.2f);
		collisionComponent.max = new Vector3(0.2f, 0.2f, 0.2f);
		collisionComponent.boundingBox.set(collisionComponent.min, collisionComponent.max);
	}

	public ItemDrop(BlockBase block) {
		this(new Vector3f(), block, 1);
	}

	public void generateModel(Tessellator tess) {
		Position pos = Components.POSITION.get(this);
		if (block.isCustomModel())
			block.generateCustomModel(tess, pos.getX() - 0.15f, pos.getY() - 0.15f,
					pos.getZ() - 0.15f, 0.3f, true, true, true, true, true, true, 0, 0, 0, 0, 0, 0, 0, 0);
		else
			tess.generateCube(pos.getX() - 0.15f, pos.getY() - 0.15f, pos.getZ() - 0.15f,
					0.3f, true, true, true, true, true, true, block, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public BlockBase getBlock() {
		return block;
	}

}
