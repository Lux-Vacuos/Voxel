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

package net.luxvacuos.voxel.server.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.server.resources.GameResources;
import net.luxvacuos.voxel.universal.network.packets.Username;

public class DedicatedListener extends Listener {

	private GameResources gm;

	public DedicatedListener(GameResources gm) {
		this.gm = gm;
	}

	@Override
	public void connected(Connection connection) {
		ConnectionsHandler.getInstace().addConnection(connection);
		connection.sendTCP("Welcome to Voxel Server");
	}

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof Username) {
			connection.setName(((Username) object).getUser());
			gm.getVoxelServer().updateNames();
			Logger.log(((Username) object).getUser() + " has join the server.");
			gm.getVoxelServer().getServer().sendToAllExceptTCP(connection.getID(),
					((Username) object).getUser() + " has join the server.");
		}
	}

	@Override
	public void disconnected(Connection connection) {
		ConnectionsHandler.getInstace().deleteConnection(connection);
		gm.getVoxelServer().updateNames();
	}

}
