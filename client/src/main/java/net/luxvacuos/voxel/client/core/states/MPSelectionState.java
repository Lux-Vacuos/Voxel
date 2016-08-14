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

import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.Window;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * Multiplayer Selection State, here the user inserts the server IP.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class MPSelectionState extends AbstractState {

	private Button exitButton;
	private Button playButton;
	private Window window;
	private String ip = "";
	private Text ipText;

	private float time = 0;

	public MPSelectionState() {
		super("MP_Selection");
		window = new Window(20, GameResources.getInstance().getDisplay().getDisplayHeight() - 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40, "Multiplayer");
		exitButton = new Button(window.getWidth() / 2 + 10, -window.getHeight() + 35, 200, 40, "Back");
		playButton = new Button(window.getWidth() / 2 - 210, -window.getHeight() + 35, 200, 40, "Enter");

		exitButton.setOnButtonPress((button, delta) -> {
			if (time > 0.2f)
				//switchTo(GameState.MAINMENU);
				StateMachine.setCurrentState("MainMenu");
		});

		playButton.setOnButtonPress((button, delta) -> {
			if (time > 0.2f) {
				GameResources.getInstance().getVoxelClient().setUrl(ip);
				//switchTo(GameState.LOADING_MP_WORLD);
				StateMachine.setCurrentState("MP_Loading");
			}
		});
		ipText = new Text("IP:", window.getWidth() / 2 - 170, -window.getHeight() / 2);

		window.addChildren(exitButton);
		window.addChildren(playButton);
		window.addChildren(ipText);
	}

	@Override
	public void start() {
		time = 0;
		window.setFadeAlpha(0);
	}

	@Override
	public void end() {
		window.setFadeAlpha(1);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		window.update(delta);
		if (time <= 0.2f) {
			time += 1 * delta;
		}
		/*if (!switching)
			window.fadeIn(4, delta);
		if (switching)
			if (window.fadeOut(4, delta)) {
				readyForSwitch = true;
			} */
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		GameResources gm = (GameResources) voxel.getGameResources();
		MasterRenderer.prepare(0, 0, 0, 1);
		gm.getDisplay().beingNVGFrame();
		window.render();
		while (Keyboard.next())
			ip = Keyboard.keyWritten(ip);
		UIRendering.renderSearchBox(ip, "Roboto-Regular", "Entypo",
				GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 150f,
				GameResources.getInstance().getDisplay().getDisplayHeight() / 2f - 10, 300, 20);
		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

	public String getIp() {
		return ip;
	}

}
