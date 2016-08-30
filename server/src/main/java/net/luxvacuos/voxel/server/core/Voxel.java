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

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.server.api.MoltenAPI;
import net.luxvacuos.voxel.server.core.states.MPState;
import net.luxvacuos.voxel.server.resources.ServerGameResources;
import net.luxvacuos.voxel.server.ui.UserInterface;
import net.luxvacuos.voxel.server.world.DefaultWorld;
import net.luxvacuos.voxel.universal.api.ModsHandler;
import net.luxvacuos.voxel.universal.bootstrap.AbstractBootstrap;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.EngineType;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.network.packets.WorldTime;
import net.luxvacuos.voxel.universal.util.PerRunLog;

public class Voxel extends AbstractVoxel {

	public Voxel(AbstractBootstrap bootstrap) {
		super(bootstrap);
		super.engineType = EngineType.SERVER;
		loop();
	}

	@Override
	public void preInit() throws Exception {

		try {
			Manifest manifest = new Manifest(getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
			Attributes attr = manifest.getMainAttributes();
			String t = attr.getValue("Specification-Version");
			if (t != null)
				ServerVariables.version = t;
		} catch (IOException E) {
			E.printStackTrace();
		}

		gameResources = ServerGameResources.getInstance();
		getGameResources().construct(this, ServerVariables.port);
		if (ServerVariables.useUI) {
			getGameResources().getUserInterface().getThreadUI().start();
			while (!getGameResources().getUserInterface().isStarted())
				Thread.sleep(100);
		}
		PerRunLog.setBootstrap(bootstrap);
		Logger.init();
		Logger.log("Starting Server");
		Logger.log("Voxel Server Version: " + ServerVariables.version);
		Logger.log("Running on: " + bootstrap.getPlatform());

		StateMachine.registerState(new MPState());
		getGameResources().preInit();
		modsHandler = new ModsHandler(this);
		modsHandler.setMoltenAPI(new MoltenAPI());
		modsHandler.preInit();
	}

	@Override
	public void init() throws Exception {
		getGameResources().init(this);
		modsHandler.init();
	}

	@Override
	public void postInit() throws Exception {
		this.gameResources.postInit();
		modsHandler.postInit();
		getGameResources().getVoxelServer().connect();
		UserInterface.setReady(true);
		ServerGameResources.getInstance().getWorldsHandler().registerWorld(new DefaultWorld("world"));
		ServerGameResources.getInstance().getWorldsHandler().setActiveWorld("world");
		ServerGameResources.getInstance().getWorldsHandler().getActiveWorld().init();
		StateMachine.setCurrentState("MPState");
	}

	@Override
	public void loop() {
		try {
			preInit();
			init();
			postInit();
			float delta = 0;
			float accumulator = 0f;
			float interval = 1f / ServerVariables.UPS;
			StateMachine.run();
			while (StateMachine.isRunning()) {
				if (getGameResources().getCoreUtils().getTimeCount() > 1f) {
					CoreInfo.ups = CoreInfo.upsCount;
					CoreInfo.upsCount = 0;
					getGameResources().getCoreUtils()
							.setTimeCount(getGameResources().getCoreUtils().getTimeCount() - 1f);
				}
				delta = getGameResources().getCoreUtils().getDelta();
				accumulator += delta;
				while (accumulator >= interval) {
					update(interval);
					accumulator -= interval;
				}
				getGameResources().getVoxelServer().getServer()
						.sendToAllTCP(new WorldTime(getGameResources().getWorldSimulation().getTime()));
				getGameResources().getCoreUtils().sync(ServerVariables.UPS);
			}
			dispose();
		} catch (Throwable t) {
			handleError(t);
		}
	}

	@Override
	public void handleError(Throwable e) {
		// TODO: Implement This
		e.printStackTrace();
	}

	@Override
	public void update(float delta) {
		CoreInfo.upsCount++;
		StateMachine.update(this, delta);
	}

	@Override
	public void dispose() {
		Logger.log("Stopping Server");
		this.getGameResources().getWorldsHandler().getActiveWorld().dispose();
		this.gameResources.dispose();
		StateMachine.dispose();
	}

	@Override
	public ServerGameResources getGameResources() {
		return ((ServerGameResources) gameResources);
	}

}
