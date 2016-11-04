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

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
//import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

/**
 * Multiplayer Selection State, here the user inserts the server IP.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class MPSelectionState extends AbstractFadeState {

	private Button exitButton;
	private Button playButton;
	private UIWindow uiWindow;
	private String ip = "";
	private Text ipText;

	private float time = 0;

	public MPSelectionState() {
		super(StateNames.MP_SELECTION);
		Window window = GameResources.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40,
				"Multiplayer");
		exitButton = new Button(uiWindow.getWidth() / 2 + 10, -uiWindow.getHeight() + 35, 200, 40, "Back");
		playButton = new Button(uiWindow.getWidth() / 2 - 210, -uiWindow.getHeight() + 35, 200, 40, "Enter");

		exitButton.setOnButtonPress((button, delta) -> {
			if (time > 0.2f) {
				window.getKeyboardHandler().disableTextInput();
				this.switchTo(StateNames.MAIN_MENU);
			}
		});

		playButton.setOnButtonPress((button, delta) -> {
			if (time > 0.2f) {
				window.getKeyboardHandler().disableTextInput();
				GameResources.getInstance().getVoxelClient().setUrl(ip);
				this.switchTo(StateNames.MP_LOADING);
			}
		});
		ipText = new Text("IP:", uiWindow.getWidth() / 2 - 170, -uiWindow.getHeight() / 2);

		uiWindow.addChildren(exitButton);
		uiWindow.addChildren(playButton);
		uiWindow.addChildren(ipText);
	}

	@Override
	public void start() {
		GameResources.getInstance().getGameWindow().getKeyboardHandler().enableTextInput();
		time = 0;
		this.uiWindow.setFadeAlpha(0);
	}

	@Override
	public void end() {
		this.ip = "";
		this.uiWindow.setFadeAlpha(1);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		uiWindow.update(delta);
		if (time <= 0.2f) {
			time += 1 * delta;
		}

		super.update(voxel, delta);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ((GameResources) voxel.getGameResources()).getGameWindow();
		Renderer.prepare(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
		// while (Keyboard.next())
		// ip = Keyboard.keyWritten(ip);
		ip = window.getKeyboardHandler().handleInput(ip);
		UIRendering.renderSearchBox(window.getID(), ip, "Roboto-Regular", "Entypo", window.getWidth() / 2f - 150f,
				window.getHeight() / 2f - 10, 300, 20);
		UIRendering.renderMouse(window.getID());
		window.endNVGFrame();
	}

	public String getIp() {
		return ip;
	}

	@Override
	protected boolean fadeIn(float delta) {
		return this.uiWindow.fadeIn(4, delta);
	}

	@Override
	protected boolean fadeOut(float delta) {
		return this.uiWindow.fadeOut(4, delta);
	}

}
