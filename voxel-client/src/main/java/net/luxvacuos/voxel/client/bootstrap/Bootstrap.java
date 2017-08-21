/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.client.bootstrap;

import net.luxvacuos.lightengine.universal.core.GlobalVariables;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.core.states.StateMachine;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.MainMenuState;
import net.luxvacuos.voxel.universal.remote.User;

public class Bootstrap extends net.luxvacuos.lightengine.client.bootstrap.Bootstrap {

	public Bootstrap(String[] args) {
		super(args);
	}

	@Override
	public void parseArgs(String[] args) {
		// Booleans to prevent setting a previously set value
		boolean gaveWidth = false, gaveHeight = false;
		// Iterate through array
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			// Check for window width
			case "-width":
				if (gaveWidth)
					throw new IllegalStateException("Width already given");
				// Convert and set the width
				ClientVariables.WIDTH = Integer.parseInt(args[++i]);
				if (ClientVariables.WIDTH <= 0)
					throw new IllegalArgumentException("Width must be positive");
				gaveWidth = true;
				break;
			// Check for height
			case "-height":
				if (gaveHeight)
					throw new IllegalStateException("Height already given");
				// Convert and set height
				ClientVariables.HEIGHT = Integer.parseInt(args[++i]);
				if (ClientVariables.HEIGHT <= 0)
					throw new IllegalArgumentException("Height must be positive");
				gaveHeight = true;
				break;
			// Check for username
			case "-username":
				// Setup user
				ClientVariables.user = new User(args[++i], args[++i]);
				break;
			default:
				// If there is an unknown arg throw exception
				if (args[i].startsWith("-")) {
					throw new IllegalArgumentException("Unknown argument: " + args[i].substring(1));
				} else {
					throw new IllegalArgumentException("Unknown token: " + args[i]);
				}
			}
		}
	}

	public static void main(String[] args) {
		GlobalVariables.PROJECT = "Voxel";
		TaskManager.addTask(() -> StateMachine.registerState(new MainMenuState()));
		new Bootstrap(args);
	}

}
