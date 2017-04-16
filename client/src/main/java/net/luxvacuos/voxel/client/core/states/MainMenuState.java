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
import net.luxvacuos.voxel.client.rendering.api.nanovg.WM;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.Scripting;
import net.luxvacuos.voxel.universal.core.states.AbstractState;

/**
 * Main Menu State, this is the menu show after the splash screen fade out.
 * 
 * @author danirod
 */
public class MainMenuState extends AbstractState {

	private Scripting scripting;
	private CompiledScript script;
	private SimpleBindings bindings;

	public MainMenuState() {
		super(StateNames.MAIN_MENU);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		scripting = new Scripting();
		script = scripting.compile("test");
		bindings = new SimpleBindings();
		bindings.put("kb", window.getKeyboardHandler());
	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		WM.getWM().render();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		WM.getWM().update(delta);
		try {
			script.eval(bindings);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		if (ClientInternalSubsystem.getInstance().getGameWindow().getKeyboardHandler().isKeyPressed(GLFW.GLFW_KEY_T))
			script = scripting.compile("test");
	}

}
