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

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.Window;

public class SPCreateWorld extends State {
	private Window window;
	private String name = "";
	private Text nameT;
	private Text optionsT;
	private Button createButton;
	private Button backButton;

	public SPCreateWorld() {
		window = new Window(20, GameResources.getInstance().getDisplay().getDisplayHeight() - 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40, "Create World");
		nameT = new Text("World Name", window.getWidth() / 2, -window.getHeight() / 2 + 100);
		nameT.setAlign(NVG_ALIGN_CENTER);
		optionsT = new Text("Options", window.getWidth() / 2, -window.getHeight() / 2);
		optionsT.setAlign(NVG_ALIGN_CENTER);
		createButton = new Button(window.getWidth() / 2 - 210, -window.getHeight() + 35, 200, 40, "Create World");
		createButton.setOnButtonPress((button, delta) -> {
			if (!name.equals("")) {
				new File(VoxelVariables.WORLD_PATH + name).mkdirs();
				switchTo(GameState.SP_SELECTION);
				name = "";
			}
		});
		backButton = new Button(window.getWidth() / 2 + 10, -window.getHeight() + 35, 200, 40, "Back");
		backButton.setOnButtonPress((button, delta) -> {
			switchTo(GameState.SP_SELECTION);
		});
		window.addChildren(nameT);
		window.addChildren(optionsT);
		window.addChildren(createButton);
		window.addChildren(backButton);
	}

	@Override
	public void start() {
		window.setFadeAlpha(0);
	}

	@Override
	public void end() {
		window.setFadeAlpha(1);
	}

	@Override
	public void update(Voxel voxel, float delta) {
		window.update(delta);
		if (!switching)
			window.fadeIn(4, delta);
		if (switching)
			if (window.fadeOut(4, delta)) {
				readyForSwitch = true;
			}
	}

	@Override
	public void render(Voxel voxel, float alpha) {
		GameResources gm = voxel.getGameResources();
		MasterRenderer.prepare(0, 0, 0, 1);
		gm.getDisplay().beingNVGFrame();
		window.render();
		while (Keyboard.next())
			name = Keyboard.keyWritten(name);
		UIRendering.renderSearchBox(name, "Roboto-Regular", "Entypo",
				GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 150f,
				GameResources.getInstance().getDisplay().getDisplayHeight() / 2f - 85, 300, 20);
		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

}
