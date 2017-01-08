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

package net.luxvacuos.voxel.server.commands;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.server.core.ServerWorldSimulation;
import net.luxvacuos.voxel.universal.commands.SimpleCommand;

public class TimeCommand extends SimpleCommand {

	private ServerWorldSimulation serverWorldSimulation;

	public TimeCommand(ServerWorldSimulation serverWorldSimulation) {
		super("/time");
		this.serverWorldSimulation = serverWorldSimulation;
	}

	@Override
	public void execute(Object... data) {
		if (data.length == 0) {
			Logger.log("No Option selected");
			return;
		}
		String param = (String) data[0];
		switch (param) {
		case "set":
			serverWorldSimulation.setTime(Integer.parseInt((String) data[1]));
			Logger.log("Time set to: " + data[1]);
			break;
		case "time":
			Logger.log("Time is: " + serverWorldSimulation.getTime());
			break;
		}
	}

}
