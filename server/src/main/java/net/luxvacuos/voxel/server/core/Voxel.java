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

package net.luxvacuos.voxel.server.core;

import java.util.Map;

import net.luxvacuos.voxel.server.bootstrap.Bootstrap;
import net.luxvacuos.voxel.server.resources.GameResources;
import net.luxvacuos.voxel.server.util.Logger;
import net.luxvacuos.voxel.universal.api.APIMethod;
import net.luxvacuos.voxel.universal.api.ModInitialization;
import net.luxvacuos.voxel.universal.api.MoltenAPI;
import net.luxvacuos.voxel.universal.api.VersionException;
import net.luxvacuos.voxel.universal.core.UVoxel;
import net.luxvacuos.voxel.universal.network.packets.WorldTime;

public class Voxel extends UVoxel {

	// private WorldsHandler worldsHandler;
	public float time = 6700;
	private int port;

	private ModInitialization api;

	public Voxel(int port) {
		this.port = port;
		super.prefix = "";
		super.server = true;
		mainLoop();
	}

	private void preInit() {
		Logger.log("Starting Server");
		gameResources = new GameResources(port);
		Logger.log("Voxel Server Version: " + VoxelVariables.version);
		Logger.log("Molten API Version: " + MoltenAPI.apiVersion);
		Logger.log("Build: " + VoxelVariables.build);
		Logger.log("Running on: " + Bootstrap.getPlatform());

		getGameResources().preInit();
		api = new ModInitialization(this);
		try {
			api.preInit();
		} catch (VersionException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		getGameResources().init();
		try {
			api.init();
		} catch (VersionException e) {
			e.printStackTrace();
		}
	}

	private void postInit() {
		getGameResources().postInit(this);
		try {
			api.postInit();
		} catch (VersionException e) {
			e.printStackTrace();
		}
		getGameResources().getWrapperUI().getThreadUI().start();
		getGameResources().getVoxelServer().connect();
	}

	@Override
	public void registerAPIMethods(MoltenAPI api, Map<String, APIMethod<Object>> methods) {
	}

	private void mainLoop() {
		preInit();
		init();
		postInit();
		float delta = 0;
		float accumulator = 0f;
		float interval = 1f / VoxelVariables.UPS;
		while (getGameResources().getGlobalStates().loop) {
			if (getGameResources().getCoreUtils().getTimeCount() > 1f) {
				CoreInfo.ups = CoreInfo.upsCount;
				CoreInfo.upsCount = 0;
				getGameResources().getCoreUtils().setTimeCount(getGameResources().getCoreUtils().getTimeCount() - 1f);
			}
			delta = getGameResources().getCoreUtils().getDelta();
			accumulator += delta;
			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}
			getGameResources().getVoxelServer().getServer().sendToAllTCP(new WorldTime(time));
			getGameResources().getCoreUtils().sync(VoxelVariables.UPS);
		}
		dispose();
	}

	private void update(float delta) {
		CoreInfo.upsCount++;
		getGameResources().getGlobalStates().doUpdate(this, delta);
	}

	private void dispose() {
		Logger.log("Stopping Server");
		getGameResources().dispose();
	}

	public ModInitialization getApi() {
		return api;
	}

	public GameResources getGameResources() {
		return ((GameResources) gameResources);
	}

}
