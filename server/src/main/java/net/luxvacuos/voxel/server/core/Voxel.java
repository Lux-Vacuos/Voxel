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

import net.luxvacuos.voxel.server.api.ModInitialization;
import net.luxvacuos.voxel.server.api.VersionException;
import net.luxvacuos.voxel.server.bootstrap.Bootstrap;
import net.luxvacuos.voxel.server.resources.GameResources;
import net.luxvacuos.voxel.server.util.Logger;
import net.luxvacuos.voxel.universal.network.packets.WorldTime;

public class Voxel {

	// private WorldsHandler worldsHandler;
	public float time = 6700;
	private int port;

	private GameResources gameResources;
	private ModInitialization api;

	public Voxel(int port) {
		this.port = port;
		mainLoop();
	}

	private void preInit() {
		Logger.log("Starting Server");
		gameResources = new GameResources(port);
		Logger.log("Voxel Server Version: " + VoxelVariables.version);
		Logger.log("Molten API Version: " + VoxelVariables.apiVersion);
		Logger.log("Build: " + VoxelVariables.build);
		Logger.log("Running on: " + Bootstrap.getPlatform());

		gameResources.preInit();
		api = new ModInitialization(gameResources);
		try {
			api.preInit();
		} catch (VersionException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		// worldsHandler = new WorldsHandler();
		// worldsHandler.registerWorld("Inifinty", new InfinityWorld());
		gameResources.init();

		try {
			api.init();
		} catch (VersionException e) {
			e.printStackTrace();
		}
	}

	private void postInit() {
		gameResources.postInit(this);
		try {
			api.postInit();
		} catch (VersionException e) {
			e.printStackTrace();
		}
		gameResources.getWrapperUI().getThreadUI().start();
		gameResources.getVoxelServer().connect();
	}

	private void mainLoop() {
		preInit();
		init();
		postInit();
		float delta = 0;
		float accumulator = 0f;
		float interval = 1f / VoxelVariables.UPS;
		while (gameResources.getGlobalStates().loop) {
			if (gameResources.getCoreUtils().getTimeCount() > 1f) {
				CoreInfo.ups = CoreInfo.upsCount;
				CoreInfo.upsCount = 0;
				gameResources.getCoreUtils().setTimeCount(gameResources.getCoreUtils().getTimeCount() - 1f);
			}
			delta = gameResources.getCoreUtils().getDelta();
			accumulator += delta;
			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}
			gameResources.getVoxelServer().getServer().sendToAllTCP(new WorldTime(time));
			gameResources.getCoreUtils().sync(VoxelVariables.UPS);
		}
		dispose();
	}

	private void update(float delta) {
		CoreInfo.upsCount++;
		gameResources.getGlobalStates().doUpdate(this, delta);
	}

	private void dispose() {
		Logger.log("Stopping Server");
		gameResources.dispose();
	}

	public ModInitialization getApi() {
		return api;
	}

	public GameResources getGameResources() {
		return gameResources;
	}

}
