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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.universal.network.packets.UpdateNames;
import net.luxvacuos.voxel.universal.network.packets.Username;
import net.luxvacuos.voxel.universal.network.packets.WorldTime;

public class VoxelClient {

	private int port;
	private String url;
	private Client client;

	public VoxelClient(GameResources gm) {
		init(gm);
	}

	public void init(GameResources gm) {
		client = new Client();
		client.start();
		client.addListener(new DedicatedListener(gm));
		client.setName("Guerra24");
		Kryo kryo = client.getKryo();
		kryo.register(Vector3f.class);
		kryo.register(Vector2f.class);
		kryo.register(WorldTime.class);
		kryo.register(Username.class);
		kryo.register(UpdateNames.class);
		kryo.register(List.class);
		kryo.register(ArrayList.class);
	}

	public void connect(int port, String url) throws IOException {
		this.port = port;
		this.url = url;
		VoxelVariables.onServer = true;
		client.connect(1000, this.url, this.port, this.port);
	}

	public void dispose() {
		client.stop();
	}

	public Client getClient() {
		return client;
	}

}
