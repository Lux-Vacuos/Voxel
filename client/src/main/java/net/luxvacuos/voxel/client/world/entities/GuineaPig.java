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

package net.luxvacuos.voxel.client.world.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.resources.EntityResources;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.entities.components.ArmourComponent;
import net.luxvacuos.voxel.client.world.entities.components.DropComponent;
import net.luxvacuos.voxel.client.world.entities.components.RendereableComponent;
import net.luxvacuos.voxel.client.world.items.EmptyArmour;
import net.luxvacuos.voxel.client.world.items.ItemDrop;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.Health;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Velocity;

public class GuineaPig extends AbstractEntity {

	public GuineaPig(Vector3d position) {
		super.add(new RendereableComponent()).getComponent(RendereableComponent.class).model = EntityResources
				.getGuineaPig();
		Vector3 min = new Vector3(-0.15f, -0.15f, -0.15f);
		Vector3 max = new Vector3(0.15f, 0.3f, 0.15f);
		super.add(new AABB(min, max));
		super.add(new Health(10));
		List<ItemDrop> drop = new ArrayList<>();
		drop.add(new ItemDrop(Block.Torch));
		super.add(new DropComponent(drop));
		super.add(new Position(position));
		super.add(new ArmourComponent()).getComponent(ArmourComponent.class).armour = new EmptyArmour();
		super.add(new Velocity());
	}

	@Override
	public void update(float delta) {

		// Test with rotation based movement
		/*
		 * Vector3d velocity =
		 * super.getComponent(VelocityComponent.class).velocity; velocity.x +=
		 * 0.4f; Vector4d tmp = new Vector4d();
		 * 
		 * Matrix4d.transform( Maths.createTransformationMatrix(new Vector3d(),
		 * getRotX(), getRotY(), getRotZ(), getScale()), new
		 * Vector4d(velocity.x, velocity.y, velocity.z, 1), tmp);
		 * super.getComponent(VelocityComponent.class).velocity.x = tmp.x;
		 * super.getComponent(VelocityComponent.class).velocity.y = tmp.y;
		 * super.getComponent(VelocityComponent.class).velocity.z = tmp.z;
		 */
	}

}
