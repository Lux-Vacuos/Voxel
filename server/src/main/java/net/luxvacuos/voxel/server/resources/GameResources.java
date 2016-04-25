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

import com.esotericsoftware.kryo.Kryo;

import net.luxvacuos.igl.CustomLog;
import net.luxvacuos.voxel.server.core.CoreUtils;
import net.luxvacuos.voxel.server.core.GameSettings;
import net.luxvacuos.voxel.server.core.GlobalStates;
import net.luxvacuos.voxel.server.core.Voxel;
import net.luxvacuos.voxel.server.core.WorldSimulation;
import net.luxvacuos.voxel.server.network.VoxelServer;
import net.luxvacuos.voxel.server.ui.UserInterface;
import net.luxvacuos.voxel.universal.resources.UGameResources;

public class GameResources extends UGameResources {

	private GlobalStates globalStates;
	private Kryo kryo;
	private VoxelServer voxelServer;
	private CoreUtils coreUtils;
	private UserInterface userInterface;
	private WorldSimulation worldSimulation;
	private GameSettings gameSettings;

	private int port;

	public GameResources(int port, Voxel voxel) {
		this.port = port;
		userInterface = new UserInterface(voxel);
	}

	public void preInit() {
		voxelServer = new VoxelServer(port);
	}

	public void init() {
		kryo = new Kryo();
		globalStates = new GlobalStates();
		voxelServer.init(this);
		coreUtils = new CoreUtils();
		CustomLog.getInstance();
		worldSimulation = new WorldSimulation();
		gameSettings = new GameSettings();
	}

	public void postInit() {
	}

	public void dispose() {
		gameSettings.updateSetting();
		gameSettings.save();
		voxelServer.dispose();
	}

	public WorldSimulation getWorldSimulation() {
		return worldSimulation;
	}

	public GlobalStates getGlobalStates() {
		return globalStates;
	}

	public Kryo getKryo() {
		return kryo;
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

}
