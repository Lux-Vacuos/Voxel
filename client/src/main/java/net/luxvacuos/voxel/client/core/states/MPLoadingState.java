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

import java.io.IOException;

import org.lwjgl.nanovg.NanoVG;

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.client.world.ClientWorld;
import net.luxvacuos.voxel.client.world.WorldsHandler;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * 
 * Multiplayer Loading state, this is runned when loading a multiplayer session.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class MPLoadingState extends AbstractState {

	private boolean trying = false;
	private Button exitButton;
	private UIWindow uiWindow;
	private Text message;
	private float time = 0;

	public MPLoadingState() {
		super(StateNames.MP_LOADING);
		Window window = GameResources.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20,
				window.getWidth() - 40, window.getHeight() - 40, "Multiplayer");
		exitButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() + 35, 200, 40, "Cancel");
		exitButton.setOnButtonPress((button, delta) -> {
			if (time > 0.2f) {
				// switchTo(GameState.MP_SELECTION);
				StateMachine.setCurrentState(StateNames.MP_SELECTION);
			}
		});
		message = new Text("Connecting...", uiWindow.getWidth() / 2, -uiWindow.getHeight() / 2);
		message.setAlign(NanoVG.NVG_ALIGN_CENTER);
		uiWindow.addChildren(exitButton);
		uiWindow.addChildren(message);
	}

	@Override
	public void start() {
		time = 0;
		trying = false;
		// window.setFadeAlpha(0);
	}

	@Override
	public void end() {
		message.setText("Connecting...");
		// window.setFadeAlpha(1);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		uiWindow.update(delta);
		GameResources gm = (GameResources) voxel.getGameResources();
		if (!trying) {
			try {
				trying = true;
				gm.getVoxelClient().connect(4059);
				message.setText("Loading World");
				WorldsHandler wm = gm.getWorldsHandler();
				wm.registerWorld(new ClientWorld());
				wm.setActiveWorld("server");
				wm.getActiveWorld().init();
				wm.getActiveWorld().getActiveDimension().getPhysicsEngine()
						.addEntity(GameResources.getInstance().getCamera());
				((PlayerCamera) GameResources.getInstance().getCamera()).setMouse();
				// switchTo(GameState.MP);
				StateMachine.setCurrentState(StateNames.MULTIPLAYER);
			} catch (IOException e) {
				ClientVariables.onServer = false;
				message.setText(e.getMessage());
				e.printStackTrace();
			}
		}
		if (time <= 0.2f) {
			time += 1 * delta;
		}
		if (time <= 0.2f) {
			time += 1 * delta;
		}
		/*
		 * if (!switching) fadeIn = window.fadeIn(4, delta); if (switching) if
		 * (window.fadeOut(4, delta)) { readyForSwitch = true; }
		 */
	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		Window window = ((GameResources)voxel.getGameResources()).getGameWindow();
		MasterRenderer.prepare(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
		UIRendering.renderMouse(window.getID());
		window.endNVGFrame();
	}

}
