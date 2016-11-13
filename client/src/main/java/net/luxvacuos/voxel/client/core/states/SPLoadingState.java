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

import org.lwjgl.nanovg.NanoVG;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * Singleplayer World Loading State, this is ran when loading a world.
 * 
 * @author danirod
 */
public class SPLoadingState extends AbstractState {

	private UIWindow uiWindow;

	public SPLoadingState() {
		super(StateNames.SP_LOADING);
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40,
				"Loading World");

		Text text = new Text("Loading World", uiWindow.getWidth() / 2, uiWindow.getHeight());
		text.setAlign(NanoVG.NVG_ALIGN_CENTER);
		uiWindow.addChildren(text);
	}

	@Override
	public void start() {
		StateMachine.setCurrentState(StateNames.SINGLEPLAYER);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		uiWindow.update(delta);
	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		Window window =  ClientInternalSubsystem.getInstance().getGameWindow();
		Renderer.prepare(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
		window.endNVGFrame();
	}

}
