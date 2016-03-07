/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.luxvacuos.voxel.client.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.universal.network.packets.UpdateNames;
import net.luxvacuos.voxel.universal.network.packets.Username;
import net.luxvacuos.voxel.universal.network.packets.WorldTime;
import net.luxvacuos.voxel.universal.util.vector.Vector2f;
import net.luxvacuos.voxel.universal.util.vector.Vector3f;

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
