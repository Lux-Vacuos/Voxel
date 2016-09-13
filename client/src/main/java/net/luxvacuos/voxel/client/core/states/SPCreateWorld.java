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

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;

import java.io.File;

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
//import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

public class SPCreateWorld extends AbstractFadeState {
	private UIWindow uiWindow;
	private String worldName = "";
	private Text nameT;
	private Text optionsT;
	private Button createButton;
	private Button backButton;

	public SPCreateWorld() {
		super(StateNames.SP_CREATE_WORLD);
		Window window = GameResources.getInstance().getGameWindow();
		window.getKeyboardHandler().enableTextInput();
		uiWindow = new UIWindow(20, window.getHeight() - 20,
				window.getWidth() - 40, window.getHeight() - 40, "Create World");
		nameT = new Text("World Name", uiWindow.getWidth() / 2, -uiWindow.getHeight() / 2 + 100);
		nameT.setAlign(NVG_ALIGN_CENTER);
		optionsT = new Text("Options", uiWindow.getWidth() / 2, -uiWindow.getHeight() / 2);
		optionsT.setAlign(NVG_ALIGN_CENTER);
		createButton = new Button(uiWindow.getWidth() / 2 - 210, -uiWindow.getHeight() + 35, 200, 40, "Create World");
		createButton.setOnButtonPress((button, delta) -> {
			if (!worldName.equals("")) {
				window.getKeyboardHandler().disableTextInput();
				new File(ClientVariables.WORLD_PATH + worldName).mkdirs();
				this.switchTo(StateNames.SP_SELECTION);
				worldName = "";
			}
		});
		backButton = new Button(uiWindow.getWidth() / 2 + 10, -uiWindow.getHeight() + 35, 200, 40, "Back");
		backButton.setOnButtonPress((button, delta) -> {
			window.getKeyboardHandler().disableTextInput();
			this.switchTo(StateNames.SP_SELECTION);
		});
		uiWindow.addChildren(nameT);
		uiWindow.addChildren(optionsT);
		uiWindow.addChildren(createButton);
		uiWindow.addChildren(backButton);
	}

	@Override
	public void start() {
		uiWindow.setFadeAlpha(0);
	}

	@Override
	public void end() {
		uiWindow.setFadeAlpha(1);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		uiWindow.update(delta);
		
		super.update(voxel, delta);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ((GameResources) voxel.getGameResources()).getGameWindow();
		MasterRenderer.prepare(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
		worldName = window.getKeyboardHandler().handleInput(worldName);
		//while (Keyboard.next())
			//worldName = Keyboard.keyWritten(worldName);
		UIRendering.renderSearchBox(window.getID(), worldName, "Roboto-Regular", "Entypo",
				window.getWidth() / 2f - 150f,
				window.getHeight() / 2f - 85, 300, 20);
		UIRendering.renderMouse(window.getID());
		window.endNVGFrame();
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
