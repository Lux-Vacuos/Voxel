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

import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.Window;

public class MPLoadingState extends State {

	private boolean trying = false;
	private Button exitButton;
	private Window window;
	private Text message;
	private float time = 0;
	private boolean fadeIn;

	public MPLoadingState() {
		window = new Window(20, GameResources.getInstance().getDisplay().getDisplayHeight() - 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40, "Multiplayer");
		exitButton = new Button(window.getWidth() / 2 - 100, -window.getHeight() + 35, 200, 40, "Cancel");
		exitButton.setOnButtonPress(() -> {
			if (time > 0.2f) {
				switchTo(GameState.MP_SELECTION);
			}
		});
		message = new Text("Connecting...", window.getWidth() / 2, -window.getHeight() / 2);
		message.setAlign(NanoVG.NVG_ALIGN_CENTER);
		window.addChildren(exitButton);
		window.addChildren(message);
	}

	@Override
	public void start() {
		time = 0;
		trying = false;
		fadeIn = false;
		window.setFadeAlpha(0);
	}

	@Override
	public void end() {
		message.setText("Connecting...");
		window.setFadeAlpha(1);
	}

	@Override
	public void update(Voxel voxel, float delta) {
		window.update();
		GameResources gm = voxel.getGameResources();
		if (!trying && fadeIn) {
			try {
				trying = true;
				gm.getVoxelClient().connect(4059);
				message.setText("Loading World");
			} catch (IOException e) {
				VoxelVariables.onServer = false;
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
		if (!switching)
			fadeIn = window.fadeIn(4, delta);
		if (switching)
			if (window.fadeOut(4, delta)) {
				readyForSwitch = true;
			}
	}

	@Override
	public void render(Voxel voxel, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		gm.getDisplay().beingNVGFrame();
		window.render();
		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

}
