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

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Window;
import net.luxvacuos.voxel.client.ui.World;
import net.luxvacuos.voxel.client.world.DefaultWorld;

/**
 * Singleplayer World Selection State, here all the worlds selection menu are
 * created from folders inside the world folder.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class SPSelectionState extends State {

	private Window window;
	private Button exitButton;
	private Button playButton;
	private Button createButton;
	private List<World> worlds;
	private String worldName;
	private int y = 0;
	private int ySize = 40;

	public SPSelectionState() {
		y = 0;
		window = new Window(20, GameResources.getInstance().getDisplay().getDisplayHeight() - 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40, "Singleplayer");
		exitButton = new Button(window.getWidth() - 230, -200, 200, 40, "Back");
		playButton = new Button(window.getWidth() - 230, -150, 200, 40, "Load World");
		createButton = new Button(window.getWidth() - 230, -100, 200, 40, "Create World");

		exitButton.setOnButtonPress((button, delta) -> {
			switchTo(GameState.MAINMENU);
		});
		playButton.setOnButtonPress((button, delta) -> {
			if (!worldName.equals("")) {
				GameResources.getInstance().getWorldsHandler().registerWorld(new DefaultWorld(worldName));
				GameResources.getInstance().getWorldsHandler().setActiveWorld(worldName);
				switchTo(GameState.SP_LOADING_WORLD);
			}
		});

		createButton.setOnButtonPress((button, delta) -> {
			switchTo(GameState.SP_CREATE_WORLD);
		});

		window.addChildren(exitButton);
		window.addChildren(playButton);
		window.addChildren(createButton);
		worlds = new ArrayList<>();
	}

	@Override
	public void start() {
		File file = new File(VoxelVariables.WORLD_PATH);
		if (!file.exists())
			file.mkdirs();
		y = 0;
		try {
			Files.walk(file.toPath(), 1).forEach(filePath -> {
				if (Files.isDirectory(filePath) && !filePath.toFile().equals(new File(VoxelVariables.WORLD_PATH))) {
					World world = new World(20, -ySize - 50 - (y * (ySize + 5)), 400, ySize,
							filePath.getFileName().toString());
					y++;
					worlds.add(world);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		window.getChildrens().addAll(worlds);
		window.setFadeAlpha(0);
		worldName = "";
	}

	@Override
	public void end() {
		window.getChildrens().removeAll(worlds);
		worlds.clear();
		window.setFadeAlpha(1);
	}

	@Override
	public void update(Voxel voxel, float delta) {
		while (Mouse.next())
			window.update(delta);

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
		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

}
