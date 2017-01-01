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

package net.luxvacuos.voxel.client.core.states;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.UIImage;
import net.luxvacuos.voxel.client.ui.UIPanel;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.TaskManager;

/**
 * Splash screen State, show only in the load.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class SplashScreenState extends AbstractFadeState {

	private UIPanel uIPanel;
	private UIImage luxVacuosLogo;
	private float wait = 0;

	public SplashScreenState() {
		super(StateNames.SPLASH_SCREEN);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uIPanel = new UIPanel(window.getWidth() / 2, window.getHeight() / 2, 0, 0);
		uIPanel.setBorderColor(0, 0, 0, 0);
		uIPanel.setFillColor(0, 0, 0, 0);
		uIPanel.setGradientColor(0, 0, 0, 0);

		luxVacuosLogo = new UIImage(-256, 256, 512, 512, ClientInternalSubsystem.getInstance().getGameWindow()
				.getResourceLoader().loadNVGTexture("LuxVacuos-Logo"));
		/*
		 * luxVacuosLogo.setOnUpdate(new OnAction() { private float speed = 0;
		 * 
		 * @Override public void onAction(UIComponent component, float delta) {
		 * UIImage img = (UIImage) component; if (img.getY() < 200 + 100 &&
		 * speed <= 1) speed += 1 * delta; else if (speed > 0) speed -= 1 *
		 * delta; else speed = 0; speed = Maths.clamp(speed, 0, 1);
		 * img.addPosition(0, speed); } });
		 */
		uIPanel.addChildren(luxVacuosLogo);
	}

	@Override
	public void start() {
		uIPanel.setFadeAlpha(0);
	}

	@Override
	public void end() {
		uIPanel.setFadeAlpha(1);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		window.beingNVGFrame();
		uIPanel.render(window.getID());
		window.endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		wait += 1 * delta;
		if (wait > 2)
			uIPanel.update(delta);

		if (wait > 3 && !this.switching && TaskManager.isEmpty())
			this.switchTo(StateNames.MAIN_MENU);

		super.update(voxel, delta);
	}

	@Override
	protected boolean fadeIn(float delta) {
		return this.uIPanel.fadeIn(4, delta);
	}

	@Override
	protected boolean fadeOut(float delta) {
		return this.uIPanel.fadeOut(4, delta);
	}

}
