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

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.server.resources.GameResources;
import net.luxvacuos.voxel.server.world.Dimension;
import net.luxvacuos.voxel.server.world.entities.components.ArmourComponent;
import net.luxvacuos.voxel.server.world.entities.components.LifeComponent;
import net.luxvacuos.voxel.server.world.items.EmptyArmour;

public class PlayerCamera extends Camera {

	private boolean underWater = false;
	private boolean hit;

	public PlayerCamera() {
		super(new Vector3f(-0.25f, -1.4f, -0.25f), new Vector3f(0.25f, 0.2f, 0.25f));
		super.add(new LifeComponent(20));
		super.add(new ArmourComponent());
		super.getComponent(ArmourComponent.class).armour = new EmptyArmour();
	}

	public void update(float delta, GameResources gm, Dimension world) {
	}

	public boolean isUnderWater() {
		return underWater;
	}

	public boolean isHit() {
		return hit;
	}

}
