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

package net.luxvacuos.voxel.server.resources;

import java.io.File;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import net.luxvacuos.igl.CustomLog;
import net.luxvacuos.voxel.server.core.CoreUtils;
import net.luxvacuos.voxel.server.core.ServerGameSettings;
import net.luxvacuos.voxel.server.core.Voxel;
import net.luxvacuos.voxel.server.core.ServerVariables;
import net.luxvacuos.voxel.server.core.ServerWorldSimulation;
import net.luxvacuos.voxel.server.network.VoxelServer;
import net.luxvacuos.voxel.server.ui.UserInterface;
import net.luxvacuos.voxel.server.world.WorldsHandler;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.resources.AbstractGameResources;

public class ServerGameResources extends AbstractGameResources {

	private static ServerGameResources instance = null;

	public static ServerGameResources getInstance() {
		if (instance == null)
			instance = new ServerGameResources();
		return instance;
	}

	private VoxelServer voxelServer;
	private CoreUtils coreUtils;
	private UserInterface userInterface;
	private WorldsHandler worldsHandler;

	private int port;

	private ServerGameResources() {
	}

	public void construct(Voxel voxel, int port) throws InterruptedException {
		this.port = port;
		if (ServerVariables.useUI)
			userInterface = new UserInterface(voxel);
	}

	@Override
	public void preInit() {
		ServerVariables.SETTINGS_PATH = "settings.conf";
		voxelServer = new VoxelServer(port);
	}

	@Override
	public void init(AbstractVoxel voxel) {
		gameSettings = new ServerGameSettings();
		gameSettings.load(new File(ServerVariables.SETTINGS_PATH));
		gameSettings.read();

		kryo = new Kryo();
		kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
		voxelServer.init(this);
		coreUtils = new CoreUtils();
		CustomLog.getInstance();
		worldSimulation = new ServerWorldSimulation();
		worldsHandler = new WorldsHandler();
	}

	@Override
	public void dispose() {
		gameSettings.update();
		gameSettings.save();
		voxelServer.dispose();
		super.dispose();
	}

	@Override
	public ServerWorldSimulation getWorldSimulation() {
		return ((ServerWorldSimulation) this.worldSimulation);
	}

	@Override
	public ServerGameSettings getGameSettings() {
		return ((ServerGameSettings) this.gameSettings);
	}

	public CoreUtils getCoreUtils() {
		return coreUtils;
	}

	public VoxelServer getVoxelServer() {
		return voxelServer;
	}

	public UserInterface getUserInterface() {
		return userInterface;
	}

	public WorldsHandler getWorldsHandler() {
		return worldsHandler;
	}

}
