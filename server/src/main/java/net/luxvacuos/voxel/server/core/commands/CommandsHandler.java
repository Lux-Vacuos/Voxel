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

package net.luxvacuos.voxel.server.core.commands;

import java.util.LinkedList;
import java.util.Queue;

import net.luxvacuos.voxel.server.core.Voxel;

public class CommandsHandler {

	private static CommandsHandler instance = null;

	public static CommandsHandler getInstace() {
		if (instance == null)
			instance = new CommandsHandler();
		return instance;
	}

	private Queue<Command> commands;

	private CommandsHandler() {
		commands = new LinkedList<>();
	}

	public void update(Voxel voxel) {
		while (!commands.isEmpty()) {
			Command com = commands.poll();
			com.run(voxel);
		}
	}

	public void addCommand(Command com) {
		commands.add(com);
	}

}
