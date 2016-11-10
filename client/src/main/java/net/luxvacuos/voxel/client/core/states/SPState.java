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

package net.luxvacuos.voxel.client.core.states;

import org.lwjgl.glfw.GLFW;

import com.badlogic.ashley.core.Engine;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
//import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.PhysicsSystem;
import net.luxvacuos.voxel.client.world.entities.Plane;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.Test;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * Single Player State, here the local world is updated and rendered.
 * 
 * @author danirod
 * @category Kernel
 */
public class SPState extends AbstractState {

	private PhysicsSystem physicsSystem;
	private Engine engine;
	private Test t;
	private Plane plane;

	public SPState() {
		super(StateNames.SINGLEPLAYER);
	}

	@Override
	public void init() {
		engine = new Engine();
		physicsSystem = new PhysicsSystem();
		engine.addSystem(physicsSystem);
		t = new Test(new Vector3d(0,1,0));
		plane = new Plane();
	}

	@Override
	public void start() {
		physicsSystem.getEngine().addEntity(GameResources.getInstance().getCamera());
		physicsSystem.getEngine().addEntity(t);
		physicsSystem.getEngine().addEntity(plane);
	}

	@Override
	public void end() {
		physicsSystem.getEngine().removeAllEntities();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		GameResources gm = (GameResources) voxel.getGameResources();

		engine.update(delta);
		gm.getSunCamera().setPosition(gm.getCamera().getPosition());

		gm.update(gm.getWorldSimulation().update(delta), delta);
		ParticleMaster.getInstance().update(delta, gm.getCamera());
		KeyboardHandler kbh = gm.getGameWindow().getKeyboardHandler();

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_F1))
			ClientVariables.debug = !ClientVariables.debug;
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_F2))
			ClientVariables.hideHud = !ClientVariables.hideHud;
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_R))
			ClientVariables.raining = !ClientVariables.raining;
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
			((PlayerCamera) gm.getCamera()).unlockMouse();
			StateMachine.setCurrentState(StateNames.SP_PAUSE);
		}

	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		GameResources gm = (GameResources) voxel.getGameResources();
		Window window = gm.getGameWindow();

		gm.getRenderer().render(null

		// TODO: Implement

				, physicsSystem.getEngine().getEntities(), gm.getCamera(), gm.getSunCamera(), gm.getWorldSimulation(),
				gm.getLightPos(), gm.getInvertedLightPosition(), alpha);

		window.beingNVGFrame();
		if (ClientVariables.debug) {
			UIRendering.renderText(window.getID(), "Voxel " + " (" + ClientVariables.version + ")", "Roboto-Bold", 5,
					12, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Used VRam: " + WindowManager.getUsedVRAM() + "KB " + " UPS: " + CoreInfo.ups, "Roboto-Bold", 5, 95,
					20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(), "Loaded Chunks: " + 0 + "   Rendered Chunks: " + 0, "Roboto-Bold", 5,
					115, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Position XYZ:  " + gm.getCamera().getPosition().getX() + "  " + gm.getCamera().getPosition().getY()
							+ "  " + gm.getCamera().getPosition().getZ(),
					"Roboto-Bold", 5, 135, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Pitch Yaw Roll: " + gm.getCamera().getRotation().getX() + " " + gm.getCamera().getRotation().getY()
							+ " " + gm.getCamera().getRotation().getZ(),
					"Roboto-Bold", 5, 155, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			Timers.renderDebugDisplay(5, 24, 200, 55);
		}
		window.endNVGFrame();
	}

}
