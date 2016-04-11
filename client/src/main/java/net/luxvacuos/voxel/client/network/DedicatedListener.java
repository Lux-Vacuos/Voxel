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

package net.luxvacuos.voxel.client.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.universal.network.packets.UpdateNames;
import net.luxvacuos.voxel.universal.network.packets.Username;
import net.luxvacuos.voxel.universal.network.packets.WorldTime;

public class DedicatedListener extends Listener {

	private GameResources gm;

	public DedicatedListener(GameResources gm) {
		this.gm = gm;
	}

	@Override
	public void connected(Connection connection) {
		connection.sendTCP(new Username(connection.toString()));
	}

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof WorldTime) {
			WorldTime time = (WorldTime) object;
			gm.getWorldSimulation().setTime(time.getTime());
		} else if (object instanceof String) {
			Logger.log((String) object);
		} else if (object instanceof UpdateNames) {
		}
	}

}
