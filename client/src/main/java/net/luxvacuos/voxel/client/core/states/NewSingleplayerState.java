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

package net.luxvacuos.voxel.client.core.states;

import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.world.IWorld;

public class NewSingleplayerState extends AbstractState {
	private IWorld activeWorld;
	private Camera playerCamera;

	protected NewSingleplayerState() {
		super("New Singleplayer");
		
	}

	@Override
	public void update(AbstractVoxel voxel, float deltaTime) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void render(AbstractVoxel voxel, float delta) {
		
	}

}
