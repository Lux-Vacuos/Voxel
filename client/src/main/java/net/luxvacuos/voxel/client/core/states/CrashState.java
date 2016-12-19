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
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.UIPanel;
import net.luxvacuos.voxel.client.ui.UIParagraph;
import net.luxvacuos.voxel.client.ui.UIText;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;

public class CrashState extends AbstractState {

	public static Throwable t;

	private UIPanel uIPanel;
	private UIPanel titleBack;
	private UIText title;
	private UIText line1;
	private UIText line2;
	private UIText line3;

	private UIText error;
	private UIParagraph errorMessage;

	public CrashState() {
		super(StateNames.CRASH);
	}

	@Override
	public void init() {

		int h = 150;

		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		window.getResourceLoader().loadNVGFont("Px437_IBM_VGA8", "Px437_IBM_VGA8");
		uIPanel = new UIPanel(window.getWidth() / 2, window.getHeight() / 2, 0, 0);
		uIPanel.setBorderColor(0, 0, 0, 0);
		uIPanel.setFillColor(0, 0, 0, 0);
		uIPanel.setGradientColor(0, 0, 0, 0);

		titleBack = new UIPanel(-45, -8 + h, 85, 20);
		titleBack.setBorderColor(255, 255, 255, 255);
		titleBack.setFillColor(255, 255, 255, 255);
		titleBack.setGradientColor(255, 255, 255, 255);
		titleBack.setRound(0);

		title = new UIText("Voxel", 0, h);
		title.setColor(0, 125, 255, 255);
		title.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		title.setFont("Px437_IBM_VGA8");

		line1 = new UIText("An error has ocurred and unfortunately Voxel is unable to", -370, 75);
		line1.setAlign(NVG_ALIGN_MIDDLE);
		line1.setFont("Px437_IBM_VGA8");

		line2 = new UIText("recover from it and continue. Some information might have", -370, 50);
		line2.setAlign(NVG_ALIGN_MIDDLE);
		line2.setFont("Px437_IBM_VGA8");

		line3 = new UIText("been lost.", -370, 25);
		line3.setAlign(NVG_ALIGN_MIDDLE);
		line3.setFont("Px437_IBM_VGA8");

		error = new UIText("Kernel Panic", -370, -25);
		error.setAlign(NVG_ALIGN_MIDDLE);
		error.setFont("Px437_IBM_VGA8");

		errorMessage = new UIParagraph(t.getMessage(), -370, -55, 740, 400, 740, 400);
		errorMessage.setFont("Px437_IBM_VGA8");

		uIPanel.addChildren(titleBack);
		uIPanel.addChildren(title);
		uIPanel.addChildren(line1);
		uIPanel.addChildren(line2);
		uIPanel.addChildren(line3);
		uIPanel.addChildren(error);
		uIPanel.addChildren(errorMessage);
	}

	@Override
	public void update(AbstractVoxel voxel, float deltaTime) {
		uIPanel.update(deltaTime);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(0, 0.5f, 1, 1);
		window.beingNVGFrame();
		uIPanel.render(window.getID());
		window.endNVGFrame();
	}

}
