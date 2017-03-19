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

package net.luxvacuos.voxel.client.ui.menus;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.StateNames;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NanoWindow;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.EditBox;
import net.luxvacuos.voxel.client.ui.RootComponent;
import net.luxvacuos.voxel.client.ui.ScrollPane;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.WorldElement;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

public class WorldMenu extends RootComponent {

	public Text worldName;
	private NanoWindow root;

	public WorldMenu(float x, float y, float w, float h, NanoWindow root) {
		super(x, y, w, h, "Worlds");
		this.root = root;
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		super.setResizable(false);

		worldName = new Text("Name: ", 40, -40);
		worldName.setWindowAlignment(Alignment.TOP);
		createList(window);

		super.initApp(window);
	}

	private void createWorld(Window window) {
		EditBox nameB = new EditBox(0, 0, 300, 30, "");
		nameB.setAlignment(Alignment.CENTER);
		nameB.setWindowAlignment(Alignment.CENTER);

		Button create = new Button(0, 100, 200, 40, "Create");
		create.setAlignment(Alignment.CENTER);
		create.setWindowAlignment(Alignment.BOTTOM);
		create.setOnButtonPress(() -> {
			new File(ClientVariables.WORLD_PATH + nameB.getText()).mkdirs();
			TaskManager.addTask(() -> {
				super.disposeApp(window);
				createList(window);
			});
		});

		Button back = new Button(0, 40, 200, 40, "Back");
		back.setAlignment(Alignment.CENTER);
		back.setWindowAlignment(Alignment.BOTTOM);
		back.setOnButtonPress(() -> {
			TaskManager.addTask(() -> {
				super.disposeApp(window);
				createList(window);
			});
		});

		Text text = new Text("Name", 0, 80);
		text.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		text.setWindowAlignment(Alignment.CENTER);

		super.addComponent(nameB);
		super.addComponent(create);
		super.addComponent(back);
		super.addComponent(text);
	}

	private void createList(Window window) {
		ScrollPane pane = new ScrollPane(0, 0, appW / 2, appH, appW / 2 - 35, 60f);
		pane.setColls(1);

		File worldPath = new File(ClientVariables.WORLD_PATH);
		if (!worldPath.exists())
			worldPath.mkdirs();
		try {
			Files.walk(worldPath.toPath(), 1).forEach(filePath -> {
				if (Files.isDirectory(filePath) && !filePath.toFile().equals(worldPath)) {
					pane.addElement(new WorldElement(appW / 2 - 35, 60, filePath.getFileName().toString(), this));
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		Button loadButton = new Button(-210, 100, 200, 40, "Load World");
		loadButton.setAlignment(Alignment.CENTER);
		loadButton.setWindowAlignment(Alignment.RIGHT_BOTTOM);
		loadButton.setOnButtonPress(() -> {
			if (ClientVariables.worldNameToLoad != "") {
				super.closeWindow();
				root.setWindowClose(WindowClose.DISPOSE);
				root.closeWindow();
				StateMachine.setCurrentState(StateNames.SP_WORLD);
			}
		});

		Button createButton = new Button(-210, 40, 200, 40, "Create World");
		createButton.setAlignment(Alignment.CENTER);
		createButton.setWindowAlignment(Alignment.RIGHT_BOTTOM);
		createButton.setOnButtonPress(() -> {
			TaskManager.addTask(() -> {
				super.disposeApp(window);
				createWorld(window);
			});
		});

		super.addComponent(pane);
		super.addComponent(loadButton);
		super.addComponent(createButton);

		super.addComponent(worldName);
	}

}
