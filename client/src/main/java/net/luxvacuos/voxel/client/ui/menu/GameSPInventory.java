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

package net.luxvacuos.voxel.client.ui.menu;

import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;

public class GameSPInventory {
	private int x, y;

	public GameSPInventory(GameResources gm) {
	}

	public void update(GameResources gm) {

		y += Keyboard.isKeyDown(Keyboard.KEY_UP) ? 1 : 0;
		y -= Keyboard.isKeyDown(Keyboard.KEY_DOWN) ? 1 : 0;
		x += Keyboard.isKeyDown(Keyboard.KEY_RIGHT) ? 1 : 0;
		x -= Keyboard.isKeyDown(Keyboard.KEY_LEFT) ? 1 : 0;

		((PlayerCamera) gm.getCamera()).getInventory().setXY(x, y);

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			((PlayerCamera) gm.getCamera()).getInventory().push(x, y);
		else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			((PlayerCamera) gm.getCamera()).getInventory().pop(x, y);

	}

}
