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

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.lwjgl.glfw.GLFW;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.UIButton;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.Scripting;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * Main Menu State, this is the menu show after the splash screen fade out.
 * 
 * @author danirod
 */
public class MainMenuState extends AbstractFadeState {

	private UIButton playButton;
	private UIButton exitButton;
	private UIButton optionsButton;
	private UIButton aboutButton;
	private UIButton playMPButton;
	private UIWindow uiWindow;

	private Scripting scripting;
	private CompiledScript script;
	private SimpleBindings bindings;

	public MainMenuState() {
		super(StateNames.MAIN_MENU);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40,
				"Main Menu");

		playButton = new UIButton(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 + 120 - 20, 200, 40,
				"Singleplayer");
		playMPButton = new UIButton(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 + 60 - 20, 200, 40,
				"Multiplayer");
		optionsButton = new UIButton(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 - 20, 200, 40, "Options");
		aboutButton = new UIButton(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 - 60 - 20, 200, 40,
				"About");
		exitButton = new UIButton(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 - 120 - 20, 200, 40, "Exit");

		playButton.setPreicon(UIRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		playMPButton.setPreicon(UIRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		optionsButton.setPreicon(UIRendering.ICON_GEAR);
		aboutButton.setPreicon(UIRendering.ICON_INFORMATION_SOURCE);
		exitButton.setPreicon(UIRendering.ICON_LOGIN);

		playButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.SP_SELECTION);
		});

		playMPButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.MP_SELECTION);
		});

		optionsButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.OPTIONS);
		});

		aboutButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.ABOUT);
		});

		exitButton.setOnButtonPress((button, delta) -> {
			StateMachine.dispose();
		});

		playMPButton.setEnabled(false);

		uiWindow.addChildren(playButton);
		uiWindow.addChildren(playMPButton);
		uiWindow.addChildren(optionsButton);
		uiWindow.addChildren(aboutButton);
		uiWindow.addChildren(exitButton);

		scripting = new Scripting();
		script = scripting.compile("test");
		bindings = new SimpleBindings();
		bindings.put("window", uiWindow);
		bindings.put("kb", window.getKeyboardHandler());
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
		uiWindow.update(delta);
		try {
			script.eval(bindings);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		if (ClientInternalSubsystem.getInstance().getGameWindow().getKeyboardHandler().isKeyPressed(GLFW.GLFW_KEY_T))
			script = scripting.compile("test");

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
