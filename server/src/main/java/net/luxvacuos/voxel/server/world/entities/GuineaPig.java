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

package net.luxvacuos.voxel.server.world.entities;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.server.world.block.Block;
import net.luxvacuos.voxel.server.world.entities.components.ArmourComponent;
import net.luxvacuos.voxel.server.world.entities.components.DropComponent;
import net.luxvacuos.voxel.server.world.entities.components.LifeComponent;
import net.luxvacuos.voxel.server.world.items.EmptyArmour;
import net.luxvacuos.voxel.server.world.items.ItemDrop;

public class GuineaPig extends GameEntity {

	public GuineaPig(Vector3f position, float rotX, float rotY, float rotZ) {
		super(position, rotX, rotY, rotZ, 1);
	}

	public GuineaPig(Vector3f position) {
		super(position, 0, 90, 0, 1);
	}

	@Override
	public void init() {
		setAABB(new Vector3f(-0.15f, -0.15f, -0.15f), new Vector3f(0.15f, 0.3f, 0.15f));
		super.add(new LifeComponent(10));
		List<ItemDrop> drop = new ArrayList<>();
		drop.add(new ItemDrop(Block.Glass));
		super.add(new DropComponent(drop));
		super.add(new ArmourComponent());
		super.getComponent(ArmourComponent.class).armour = new EmptyArmour();
	}

	@Override
	public void update(float delta) {

		// Test with rotation based movement
		/*
		 * Vector3f velocity =
		 * super.getComponent(VelocityComponent.class).velocity; velocity.x +=
		 * 0.4f; Vector4f tmp = new Vector4f();
		 * 
		 * Matrix4f.transform( Maths.createTransformationMatrix(new Vector3f(),
		 * getRotX(), getRotY(), getRotZ(), getScale()), new
		 * Vector4f(velocity.x, velocity.y, velocity.z, 1), tmp);
		 * super.getComponent(VelocityComponent.class).velocity.x = tmp.x;
		 * super.getComponent(VelocityComponent.class).velocity.y = tmp.y;
		 * super.getComponent(VelocityComponent.class).velocity.z = tmp.z;
		 */
	}

}
