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

package net.luxvacuos.voxel.server.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.luxvacuos.voxel.universal.commands.ICommandManager;

public class Console {

	private boolean running = true;
	private ICommandManager manager;

	public Console() {
	}

	public void start() {
		Thread thread = new Thread(() -> {
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
			String command;
			try {
				while (running && (command = bufferedreader.readLine()) != null) {
					this.manager.command(command);
				}
			} catch (IOException ioe) {
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	public void setCommandManager(ICommandManager manager) {
		this.manager = manager;
	}

	public void stop() {
		running = false;
	}

}
