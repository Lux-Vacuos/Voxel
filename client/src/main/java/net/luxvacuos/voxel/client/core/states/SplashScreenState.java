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

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Image;
import net.luxvacuos.voxel.client.ui.Panel;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

/**
 * Splash screen State, show only in the load.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class SplashScreenState extends AbstractFadeState {

	private Panel panel;
	private Image luxVacuosLogo;
	private float wait = 0;

	public SplashScreenState() {
		super(StateNames.SPLASH_SCREEN);
		Window window = GameResources.getInstance().getGameWindow();
		panel = new Panel(window.getWidth() / 2, window.getHeight() / 2, 0, 0);
		panel.setBorderColor(0, 0, 0, 0);
		panel.setFillColor(0, 0, 0, 0);
		panel.setGradientColor(0, 0, 0, 0);

		luxVacuosLogo = new Image(-256, 256, 512, 512,
				GameResources.getInstance().getResourceLoader().loadNVGTexture("LuxVacuos-Logo"));
		/*
		 * luxVacuosLogo.setOnUpdate(new OnAction() { private float speed = 0;
		 * 
		 * @Override public void onAction(Component component, float delta) {
		 * Image img = (Image) component; if (img.getY() < 200 + 100 && speed <=
		 * 1) speed += 1 * delta; else if (speed > 0) speed -= 1 * delta; else
		 * speed = 0; speed = Maths.clamp(speed, 0, 1); img.addPosition(0,
		 * speed); } });
		 */

		panel.addChildren(luxVacuosLogo);

	}

	@Override
	public void start() {
		panel.setFadeAlpha(0);
	}

	@Override
	public void end() {
		panel.setFadeAlpha(1);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ((GameResources)voxel.getGameResources()).getGameWindow();
		Renderer.prepare(1, 1, 1, 1);
		window.beingNVGFrame();
		panel.render(window.getID());
		window.endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		wait += 1 * delta;
		if (wait > 2) panel.update(delta);
		
		if (wait > 3 && !this.switching) this.switchTo(StateNames.MAIN_MENU);
		
		super.update(voxel, delta);
	}

	@Override
	protected boolean fadeIn(float delta) {
		return this.panel.fadeIn(4, delta);
	}

	@Override
	protected boolean fadeOut(float delta) {
		return this.panel.fadeOut(4, delta);
	}

}
