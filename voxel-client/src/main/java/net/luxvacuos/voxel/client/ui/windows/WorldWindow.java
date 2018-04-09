/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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
import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.rendering.glfw.Window;
import net.luxvacuos.lightengine.client.rendering.nanovg.IWindow;
import net.luxvacuos.lightengine.client.rendering.nanovg.themes.Theme.ButtonStyle;
import net.luxvacuos.lightengine.client.ui.Alignment;
import net.luxvacuos.lightengine.client.ui.Button;
import net.luxvacuos.lightengine.client.ui.ComponentWindow;
import net.luxvacuos.lightengine.client.ui.Direction;
import net.luxvacuos.lightengine.client.ui.EditBox;
import net.luxvacuos.lightengine.client.ui.FlowLayout;
import net.luxvacuos.lightengine.client.ui.ScrollArea;
import net.luxvacuos.lightengine.client.ui.Text;
import net.luxvacuos.lightengine.client.ui.TitleBarButton;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.core.states.StateMachine;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.StateNames;
import net.luxvacuos.voxel.client.ui.WorldElement;

public class WorldWindow extends ComponentWindow {

	public Text worldName;
	private TitleBarButton backButton;

	public WorldWindow(int x, int y, int w, int h) {
		super(x, y, w, h, LANG.getRegistryItem("voxel.worldwindow.name"));
	}

	@Override
	public void initApp() {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		super.setResizable(false);

		backButton = new TitleBarButton(0, 0, 28, 28);
		backButton.setWindowAlignment(Alignment.LEFT_TOP);
		backButton.setAlignment(Alignment.RIGHT_BOTTOM);
		backButton.setStyle(ButtonStyle.LEFT_ARROW);
		backButton.setEnabled(false);

		super.getTitleBar().getLeft().addComponent(backButton);

		worldName = new Text(LANG.getRegistryItem("voxel.worldwindow.txtname"), 40, -40);
		worldName.setWindowAlignment(Alignment.TOP);
		createList(window);

		super.initApp();
	}

	private void createWorld(Window window) {
		super.extendFrame(0, 0, 0, 0);
		Text error = new Text("", 0, 50);
		error.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		error.setWindowAlignment(Alignment.CENTER);

		EditBox nameB = new EditBox(0, 0, 300, 30, "");
		nameB.setAlignment(Alignment.CENTER);
		nameB.setWindowAlignment(Alignment.CENTER);

		Button create = new Button(0, 100, 200, 40, LANG.getRegistryItem("voxel.worldwindow.create.btncreate"));
		create.setAlignment(Alignment.CENTER);
		create.setWindowAlignment(Alignment.BOTTOM);
		create.setOnButtonPress(() -> {
			File f = new File(REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/directory")) + nameB.getText());
			if (!f.exists()) {
				f.mkdirs();
				TaskManager.tm.addTaskRenderThread(() -> {
					backButton.setEnabled(false);
					super.disposeApp();
					createList(window);
				});
			}
		});

		Text text = new Text("Name", 0, 80);
		text.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		text.setWindowAlignment(Alignment.CENTER);

		super.addComponent(nameB);
		super.addComponent(create);
		super.addComponent(text);
		super.addComponent(error);
	}

	private void createList(Window window) {
		backButton.setEnabled(false);
		super.extendFrame(0, 0, 0, w / 2);
		ScrollArea area = new ScrollArea(0, 0, w / 2, h, 0, 0);
		area.setLayout(new FlowLayout(Direction.DOWN, 0, 0));
		area.setResizeH(false);

		File worldPath = new File((String) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/directory")));
		if (!worldPath.exists())
			worldPath.mkdirs();
		try {
			Files.walk(worldPath.toPath(), 1).forEach(filePath -> {
				if (Files.isDirectory(filePath) && !filePath.toFile().equals(worldPath)) {
					WorldElement el = new WorldElement(
							w / 2 - (int) REGISTRY
									.getRegistryItem(new Key("/Light Engine/Settings/WindowManager/scrollBarSize")),
							50, filePath.getFileName().toString());
					el.setWindowAlignment(Alignment.LEFT_TOP);
					el.setAlignment(Alignment.RIGHT_BOTTOM);
					area.addComponent(el);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		Button loadButton = new Button(-210, 100, 200, 40, LANG.getRegistryItem("voxel.worldwindow.list.btnload"));
		loadButton.setAlignment(Alignment.CENTER);
		loadButton.setWindowAlignment(Alignment.RIGHT_BOTTOM);
		loadButton.setOnButtonPress(() -> {
			if (ClientVariables.worldNameToLoad != "") {
				GraphicalSubsystem.getWindowManager().toggleShell();
				super.closeWindow();
				IWindow root = GraphicalSubsystem.getWindowManager().getWindowByClass("MainWindow");
				root.setWindowClose(WindowClose.DISPOSE);
				root.closeWindow();
				TaskManager.tm.addTaskBackgroundThread(() -> StateMachine.setCurrentState(StateNames.SP_WORLD));
			}
		});

		Button createButton = new Button(-210, 40, 200, 40, LANG.getRegistryItem("voxel.worldwindow.list.btncreate"));
		createButton.setAlignment(Alignment.CENTER);
		createButton.setWindowAlignment(Alignment.RIGHT_BOTTOM);
		createButton.setOnButtonPress(() -> {
			TaskManager.tm.addTaskRenderThread(() -> {
				super.disposeApp();
				createWorld(window);
				backButton.setEnabled(true);
				backButton.setOnButtonPress(() -> {
					TaskManager.tm.addTaskRenderThread(() -> {
						super.disposeApp();
						createList(window);
					});
				});
			});
		});

		super.addComponent(area);
		super.addComponent(loadButton);
		super.addComponent(createButton);

		super.addComponent(worldName);
	}

}
