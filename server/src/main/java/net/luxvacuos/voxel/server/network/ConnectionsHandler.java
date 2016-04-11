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

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

public class ConnectionsHandler {

	private static ConnectionsHandler instance;

	public static ConnectionsHandler getInstace() {
		if (instance == null)
			instance = new ConnectionsHandler();
		return instance;
	}

	private List<Connection> connections;

	private ConnectionsHandler() {
		init();
	}

	private void init() {
		connections = new ArrayList<>();
	}

	public void addConnection(Connection con) {
		connections.add(con);
	}

	public void deleteConnection(Connection con) {
		connections.remove(con);
	}

	public Connection getById(int id) {
		for (Connection connection : connections) {
			if (connection.getID() == id)
				return connection;
		}
		return null;
	}

	public Connection getByName(String name) {
		for (Connection connection : connections) {
			if (connection.toString().equals(name))
				return connection;
		}
		return null;
	}

	public List<Connection> getConnections() {
		return connections;
	}

}
