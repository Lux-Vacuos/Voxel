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

import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.vector.Vector3d;
//import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

/**
 * Singleplayer Pause State.
 * 
 * @author danirod
 */
public class SPPauseState extends AbstractFadeState {

	private UIWindow uiWindow;
	private Button exitButton;
	private Button optionsButton;

	public SPPauseState() {
		super(StateNames.SP_PAUSE);
		Window window = GameResources.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40, "Pause");
		exitButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() + 35, 200, 40,
				"Back to Main Menu");
		optionsButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() + 85, 200, 40, "Options");
		exitButton.setOnButtonPress((button, delta) -> {
			GameResources.getInstance().getWorldsHandler().getActiveWorld().dispose();
			GameResources.getInstance().getCamera().setPosition(new Vector3d(0, 0, 1));
			GameResources.getInstance().getCamera().setPitch(0);
			GameResources.getInstance().getCamera().setYaw(0);
			this.switchTo(StateNames.MAIN_MENU);
		});
		optionsButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.OPTIONS);
		});
		uiWindow.addChildren(exitButton);
		uiWindow.addChildren(optionsButton);
	}

	@Override
	public void start() {
		uiWindow.setFadeAlpha(0);
	}

	@Override
	public void end() {
		uiWindow.setFadeAlpha(1);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		GameResources gm = ((GameResources) voxel.getGameResources());
		Window window = gm.getGameWindow();
		while (Mouse.next())
			uiWindow.update(delta);

		super.update(voxel, delta);

		// while (Keyboard.next()) {
		// if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
		if (window.getKeyboardHandler().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			window.getKeyboardHandler().ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
			((PlayerCamera) gm.getCamera()).setMouse();
			this.switchTo(StateNames.SINGLEPLAYER);
		}
		// }
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		GameResources gm = (GameResources) voxel.getGameResources();
		Window window = gm.getGameWindow();

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().lighting();
		gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
		gm.getRenderer().render(gm.getWorldsHandler().getActiveWorld().getActiveDimension(), gm.getCamera(),
				gm.getSun_Camera(), gm.getWorldSimulation(), gm.getLightPos(), gm.getInvertedLightPosition(), alpha);

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
