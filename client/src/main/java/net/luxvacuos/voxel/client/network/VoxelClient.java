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
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.block.BlockEntity;
import net.luxvacuos.voxel.client.world.block.types.BlockAir;
import net.luxvacuos.voxel.client.world.block.types.BlockCobblestone;
import net.luxvacuos.voxel.client.world.block.types.BlockDiamondOre;
import net.luxvacuos.voxel.client.world.block.types.BlockDirt;
import net.luxvacuos.voxel.client.world.block.types.BlockGlass;
import net.luxvacuos.voxel.client.world.block.types.BlockGoldOre;
import net.luxvacuos.voxel.client.world.block.types.BlockGrass;
import net.luxvacuos.voxel.client.world.block.types.BlockIce;
import net.luxvacuos.voxel.client.world.block.types.BlockIndes;
import net.luxvacuos.voxel.client.world.block.types.BlockLava;
import net.luxvacuos.voxel.client.world.block.types.BlockNode;
import net.luxvacuos.voxel.client.world.block.types.BlockPedestal;
import net.luxvacuos.voxel.client.world.block.types.BlockSand;
import net.luxvacuos.voxel.client.world.block.types.BlockStone;
import net.luxvacuos.voxel.client.world.block.types.BlockTorch;
import net.luxvacuos.voxel.client.world.block.types.BlockWater;
import net.luxvacuos.voxel.client.world.block.types.BlockWood;
import net.luxvacuos.voxel.client.world.chunks.Chunk;
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
		client = new Client(16384, 10240);
		client.start();
		client.addListener(new DedicatedListener(gm));
		client.setName(VoxelVariables.username);
		Kryo kryo = client.getKryo();
		kryo.register(Vector3f.class);
		kryo.register(Vector2f.class);
		kryo.register(WorldTime.class);
		kryo.register(Username.class);
		kryo.register(UpdateNames.class);
		kryo.register(List.class);
		kryo.register(ArrayList.class);
		kryo.register(Chunk.class);
		kryo.register(BlockBase[][][].class);
		kryo.register(BlockBase[][].class);
		kryo.register(BlockBase[].class);
		kryo.register(BlockEntity.class);
		kryo.register(BlockAir.class);
		kryo.register(BlockCobblestone.class);
		kryo.register(BlockDiamondOre.class);
		kryo.register(BlockDirt.class);
		kryo.register(BlockGlass.class);
		kryo.register(BlockGoldOre.class);
		kryo.register(BlockGrass.class);
		kryo.register(BlockIce.class);
		kryo.register(BlockIndes.class);
		kryo.register(BlockLava.class);
		kryo.register(BlockNode.class);
		kryo.register(BlockPedestal.class);
		kryo.register(BlockSand.class);
		kryo.register(BlockStone.class);
		kryo.register(BlockTorch.class);
		kryo.register(BlockWater.class);
		kryo.register(BlockWood.class);
		kryo.register(byte[][][].class);
		kryo.register(byte[][].class);
		kryo.register(byte[].class);
	}

	public void connect(int port) throws IOException {
		this.port = port;
		VoxelVariables.onServer = true;
		client.connect(1000, this.url, this.port, this.port);
	}

	public void dispose() {
		client.stop();
	}

	public Client getClient() {
		return client;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
