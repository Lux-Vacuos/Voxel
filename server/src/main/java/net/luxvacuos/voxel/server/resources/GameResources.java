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

import net.luxvacuos.voxel.server.core.CoreUtils;
import net.luxvacuos.voxel.server.core.GlobalStates;
import net.luxvacuos.voxel.server.core.Voxel;
import net.luxvacuos.voxel.server.network.VoxelServer;
import net.luxvacuos.voxel.server.ui.WrapperUI;
import net.luxvacuos.voxel.server.util.CustomLog;

public class GameResources {

	private GlobalStates globalStates;
	private Kryo kryo;
	private VoxelServer voxelServer;
	private CoreUtils coreUtils;
	private WrapperUI wrapperUI;

	private int port;

	public GameResources(int port) {
		this.port = port;
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
	}

	public void postInit(Voxel voxel) {
		wrapperUI = new WrapperUI(voxel);
	}

	public void dispose() {
		voxelServer.dispose();
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

	public WrapperUI getWrapperUI() {
		return wrapperUI;
	}

}
