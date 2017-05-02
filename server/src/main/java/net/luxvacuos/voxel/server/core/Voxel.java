/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.server.core.states.MPWorldState;
import net.luxvacuos.voxel.server.core.states.StateNames;
import net.luxvacuos.voxel.universal.bootstrap.AbstractBootstrap;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.EngineType;
import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.util.PerRunLog;

public class Voxel extends AbstractVoxel {

	private double lastLoopTime;
	private float timeCount;
	private Sync sync;

	public Voxel(AbstractBootstrap bootstrap) {
		super(bootstrap);
		super.engineType = EngineType.SERVER;
		build();
	}

	@Override
	public void preInit() throws Exception {
		PerRunLog.setBootstrap(bootstrap);
		Logger.init();

		try {
			Manifest manifest = new Manifest(getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
			Attributes attr = manifest.getMainAttributes();
			String t = attr.getValue("Specification-Version");
			if (t != null)
				GlobalVariables.version = t;
		} catch (IOException E) {
			E.printStackTrace();
		}
		Logger.log("Starting Server");
		GlobalVariables.REGISTRY.register("/Voxel/Settings/file", bootstrap.getPrefix() + "/config/settings.conf");
		GlobalVariables.REGISTRY.register("/Voxel/Settings/World/directory", bootstrap.getPrefix() + "/");

		internalSubsystem = ServerInternalSubsystem.getInstance();
		internalSubsystem.preInit();
		CoreInfo.platform = bootstrap.getPlatform();
		Logger.log("Voxel Server Version: " + GlobalVariables.version);
		Logger.log("Running on: " + CoreInfo.platform);
	}

	@Override
	public void init() throws Exception {
		internalSubsystem.init();
		StateMachine.registerState(new MPWorldState());
	}

	@Override
	public void postInit() throws Exception {
		sync = new Sync();
		lastLoopTime = System.currentTimeMillis() / 1000l;
		internalSubsystem.postInit();
		StateMachine.setCurrentState(StateNames.MP_WORLD);
	}

	@Override
	public void build() {
		try {
			preInit();
			init();
			postInit();
			StateMachine.run();
			loop();
			dispose();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			handleError(t);
		}
	}

	@Override
	public void loop() {
		int ups = (int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/ups");
		float delta = 0;
		float accumulator = 0f;
		float interval = 1f / ups;
		while (StateMachine.isRunning()) {
			TaskManager.update();
			if (timeCount > 1f) {
				CoreInfo.ups = CoreInfo.upsCount;
				CoreInfo.upsCount = 0;
				timeCount--;
			}
			delta = getDelta();
			accumulator += delta;
			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}
			sync.sync(ups);
		}
	}

	@Override
	public void update(float delta) {
		CoreInfo.upsCount++;
		StateMachine.update(this, delta);
	}

	@Override
	public void handleError(Throwable e) {
		e.printStackTrace();
		dispose();
	}

	@Override
	public void dispose() {
		Logger.log("Cleaning Resources");
		internalSubsystem.dispose();
		StateMachine.dispose();
	}

	public float getDelta() {
		double time = System.currentTimeMillis() / 1000l;
		float delta = (float) (time - this.lastLoopTime);
		this.lastLoopTime = time;
		this.timeCount += delta;
		return delta;
	}

}
