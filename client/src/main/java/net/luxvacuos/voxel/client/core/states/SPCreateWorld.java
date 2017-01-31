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

package net.luxvacuos.voxel.client.core.states;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import java.io.File;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.UIButton;
import net.luxvacuos.voxel.client.ui.UIEditBox;
import net.luxvacuos.voxel.client.ui.UIText;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

public class SPCreateWorld extends AbstractFadeState {
	
	private UIWindow uiWindow;
	private String worldName = "";
	private UIText nameT;
	private UIText optionsT;
	private UIButton createButton;
	private UIButton backButton;
	private UIEditBox worldNam;

	public SPCreateWorld() {
		super(StateNames.SP_CREATE_WORLD);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40,
				"Create World");
		nameT = new UIText("World Name", uiWindow.getWidth() / 2, -uiWindow.getHeight() / 2 + 100);
		nameT.setAlign(NVG_ALIGN_CENTER);
		optionsT = new UIText("Options", uiWindow.getWidth() / 2, -uiWindow.getHeight() / 2);
		optionsT.setAlign(NVG_ALIGN_CENTER);
		createButton = new UIButton(uiWindow.getWidth() / 2 - 210, -uiWindow.getHeight() + 35, 200, 40, "Create World");
		createButton.setOnButtonPress(() -> {
			if (!worldName.equals("")) {
				window.getKeyboardHandler().disableTextInput();
				new File(ClientVariables.WORLD_PATH + worldName).mkdirs();
				this.switchTo(StateNames.SP_SELECTION);
				worldName = "";
			}
		});
		backButton = new UIButton(uiWindow.getWidth() / 2 + 10, -uiWindow.getHeight() + 35, 200, 40, "Back");
		backButton.setOnButtonPress(() -> {
			window.getKeyboardHandler().disableTextInput();
			this.switchTo(StateNames.SP_SELECTION);
		});
		worldNam = new UIEditBox(uiWindow.getWidth() / 2f - 150, -uiWindow.getHeight() / 2 + 80, 300, 30, "");
		worldNam.setFontSize(25);
		uiWindow.addChildren(nameT);
		uiWindow.addChildren(optionsT);
		uiWindow.addChildren(createButton);
		uiWindow.addChildren(backButton);
		uiWindow.addChildren(worldNam);
	}

	@Override
	public void start() {
		ClientInternalSubsystem.getInstance().getGameWindow().getKeyboardHandler().enableTextInput();
		uiWindow.setFadeAlpha(0);
	}

	@Override
	public void end() {
		this.worldName = "";
		uiWindow.setFadeAlpha(1);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		worldName = window.getKeyboardHandler().handleInput(worldName);
		worldNam.setText(worldName);
		uiWindow.update(delta);

		super.update(voxel, delta);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
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
