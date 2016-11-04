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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.client.ui.World;
import net.luxvacuos.voxel.client.world.DefaultWorld;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

/**
 * Singleplayer World Selection State, here all the worlds selection menu are
 * created from folders inside the world folder.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class SPSelectionState extends AbstractFadeState {

	private UIWindow uiWindow;
	private Button exitButton;
	private Button playButton;
	private Button createButton;
	private List<World> worlds;
	private String worldName;
	private int y = 0;
	private int ySize = 40;

	public SPSelectionState() {
		super("SP_Selection");
		Window window = GameResources.getInstance().getGameWindow();
		y = 0;
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40, "Singleplayer");
		exitButton = new Button(uiWindow.getWidth() - 230, -200, 200, 40, "Back");
		playButton = new Button(uiWindow.getWidth() - 230, -150, 200, 40, "Load World");
		createButton = new Button(uiWindow.getWidth() - 230, -100, 200, 40, "Create World");

		exitButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.MAIN_MENU);
		});
		playButton.setOnButtonPress((button, delta) -> {
			if (!worldName.equals("")) {
				GameResources.getInstance().getWorldsHandler().registerWorld(new DefaultWorld(worldName));
				GameResources.getInstance().getWorldsHandler().setActiveWorld(worldName);
				this.switchTo(StateNames.SP_LOADING);
			}
		});

		createButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.SP_CREATE_WORLD);
		});

		uiWindow.addChildren(exitButton);
		uiWindow.addChildren(playButton);
		uiWindow.addChildren(createButton);
		worlds = new ArrayList<>();
	}

	@Override
	public void start() {
		y = 0;
		File worldPath = new File(ClientVariables.WORLD_PATH);
		if (!worldPath.exists())
			worldPath.mkdirs();
		try {
			Files.walk(worldPath.toPath(), 1).forEach(filePath -> {
				if (Files.isDirectory(filePath) && !filePath.toFile().equals(worldPath)) {
					World world = new World(20, -ySize - 50 - (y * (ySize + 5)), 400, ySize,
							filePath.getFileName().toString());
					y++;
					worlds.add(world);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		uiWindow.getChildrens().addAll(worlds);
		uiWindow.setFadeAlpha(0);
		worldName = "";
	}

	@Override
	public void end() {
		uiWindow.getChildrens().removeAll(worlds);
		worlds.clear();
		uiWindow.setFadeAlpha(1);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		while (Mouse.next())
			uiWindow.update(delta);

		for (int i = 0; i < worlds.size(); i++) {
			if (worlds.get(i).isSelected()) {
				worldName = worlds.get(i).getName();
			}
			if (worlds.get(i).isPressed()) {
				for (int j = 0; j < worlds.size(); j++) {
					worlds.get(j).setSelected(false);
				}
				worlds.get(i).setSelected(true);
			}
		}

		super.update(voxel, delta);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ((GameResources)voxel.getGameResources()).getGameWindow();
		Renderer.prepare(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
		UIRendering.renderMouse(window.getID());
		window.endNVGFrame();
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
