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
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.UIButton;
import net.luxvacuos.voxel.client.ui.UIEditBox;
import net.luxvacuos.voxel.client.ui.UIText;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

public class MPSelectionState extends AbstractFadeState {

	private UIButton exitButton;
	private UIButton playButton;
	private UIEditBox address;
	private UIText text;
	private UIWindow uiWindow;
	private String ip = "";

	public MPSelectionState() {
		super(StateNames.MP_SELECTION);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40,
				"Multiplayer");
		exitButton = new UIButton(uiWindow.getWidth() / 2f - 100, -uiWindow.getHeight() + 20, 200, 40, "Back");
		playButton = new UIButton(uiWindow.getWidth() / 2f - 100, -uiWindow.getHeight() + 80, 200, 40, "Play");
		address = new UIEditBox(uiWindow.getWidth() / 2f - 150, -uiWindow.getHeight() / 2 + 40, 300, 30, "");
		address.setFontSize(25);
		text = new UIText("Server Address", uiWindow.getWidth() / 2f, -uiWindow.getHeight() / 2 + 80);
		text.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);

		exitButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.MAIN_MENU);
		});

		playButton.setOnButtonPress((button, delta) -> {
			if (ip.equals(""))
				return;
			ClientVariables.server = ip;
			ip = "";
			this.switchTo(StateNames.MP_WORLD);
		});

		uiWindow.addChildren(exitButton);
		uiWindow.addChildren(address);
		uiWindow.addChildren(text);
		uiWindow.addChildren(playButton);

	}

	@Override
	public void start() {
		ClientInternalSubsystem.getInstance().getGameWindow().getKeyboardHandler().enableTextInput();
		uiWindow.setFadeAlpha(0);
	}

	@Override
	public void end() {
		uiWindow.setFadeAlpha(1);
	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
		window.endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		ip = window.getKeyboardHandler().handleInput(ip);
		address.setText(ip);
		uiWindow.update(delta);
		super.update(voxel, delta);
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
