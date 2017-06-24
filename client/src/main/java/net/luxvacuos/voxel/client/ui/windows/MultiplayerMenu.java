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

package net.luxvacuos.voxel.client.ui.windows;

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.LANG;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import net.luxvacuos.lightengine.client.rendering.api.glfw.Window;
import net.luxvacuos.lightengine.client.rendering.api.nanovg.NanoWindow;
import net.luxvacuos.lightengine.client.ui.Alignment;
import net.luxvacuos.lightengine.client.ui.Button;
import net.luxvacuos.lightengine.client.ui.ComponentWindow;
import net.luxvacuos.lightengine.client.ui.EditBox;
import net.luxvacuos.lightengine.client.ui.Text;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.core.states.StateMachine;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.StateNames;

public class MultiplayerMenu extends ComponentWindow {

	private NanoWindow root;

	public MultiplayerMenu(float x, float y, float w, float h, NanoWindow root) {
		super(x, y, w, h, LANG.getRegistryItem("voxel.mpwindow.name"));
		this.root = root;
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		super.setResizable(false);

		Button playButton = new Button(0, 40, 200, 40, LANG.getRegistryItem("voxel.mpwindow.btnplay"));
		playButton.setAlignment(Alignment.CENTER);
		playButton.setWindowAlignment(Alignment.BOTTOM);
		EditBox address = new EditBox(0, 0, 300, 30, "");
		address.setAlignment(Alignment.CENTER);
		address.setWindowAlignment(Alignment.CENTER);
		address.setFontSize(25);
		Text text = new Text(LANG.getRegistryItem("voxel.mpwindow.txtadd"), 0, 80);
		text.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		text.setWindowAlignment(Alignment.CENTER);

		playButton.setOnButtonPress(() -> {
			String ip = address.getText();
			if (!ip.equals("")) {
				ClientVariables.server = ip;
				address.setText("");
				super.closeWindow();
				root.setWindowClose(WindowClose.DISPOSE);
				root.closeWindow();
				TaskManager.addTask(() -> StateMachine.setCurrentState(StateNames.MP_WORLD));
			}
		});

		super.addComponent(address);
		super.addComponent(text);
		super.addComponent(playButton);
		super.initApp(window);
	}

}
