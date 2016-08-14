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

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT;

import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.core.exception.LoadTextureException;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Image;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.Window;
import net.luxvacuos.voxel.universal.api.MoltenAPI;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * About State, this shows all the information about Voxel and the system where is running.
 * 
 * @author danirod
 */
public class AboutState extends AbstractState {

	private Window window;
	private Button backButton;
	private Image voxelLogo;

	private int globalY;

	public AboutState() {
		super("About");
		window = new Window(20, GameResources.getInstance().getDisplay().getDisplayHeight() - 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40, 2200, "About");
		backButton = new Button((int) (GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100), 40, 200,
				40, "Back");
		backButton.setOnButtonPress((button, delta) -> {
			//switchTo(GameState.MAINMENU);
			StateMachine.setCurrentState("MainMenu");
		});

		try {
			voxelLogo = new Image(window.getWidth() / 2 - 200, -40, 400, 200,
					GameResources.getInstance().getLoader().loadNVGTexture("Voxel-Logo"));
		} catch (LoadTextureException e) {
			e.printStackTrace();
		}
		window.addChildren(voxelLogo);

		Text versionL = new Text("Version", 30, -300);
		versionL.setFont("Roboto-Bold");
		Text versionR = new Text(" (" + VoxelVariables.version + ")" + " Molten API" + " (" + MoltenAPI.apiVersion + "/"
				+ MoltenAPI.build + ")", window.getWidth() - 30, -300);
		versionR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text osL = new Text("Operative System", 30, -330);
		osL.setFont("Roboto-Bold");
		Text osR = new Text(CoreInfo.OS, window.getWidth() - 30, -330);
		osR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text lwjglL = new Text("LWJGL Version", 30, -360);
		lwjglL.setFont("Roboto-Bold");
		Text lwjglR = new Text(CoreInfo.LWJGLVer, window.getWidth() - 30, -360);
		lwjglR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text glfwL = new Text("GLFW Version", 30, -390);
		glfwL.setFont("Roboto-Bold");
		Text glfwR = new Text(CoreInfo.GLFWVer, window.getWidth() - 30, -390);
		glfwR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text openglL = new Text("OpenGL Version", 30, -420);
		openglL.setFont("Roboto-Bold");
		Text openglR = new Text(CoreInfo.OpenGLVer, window.getWidth() - 30, -420);
		openglR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text vkL = new Text("Vulkan Version", 30, -450);
		vkL.setFont("Roboto-Bold");
		Text vkR = new Text(CoreInfo.VkVersion, window.getWidth() - 30, -450);
		vkR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text vendorL = new Text("Vendor", 30, -480);
		vendorL.setFont("Roboto-Bold");
		Text vendorR = new Text(CoreInfo.Vendor, window.getWidth() - 30, -480);
		vendorR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text rendererL = new Text("Renderer", 30, -510);
		rendererL.setFont("Roboto-Bold");
		Text rendererR = new Text(CoreInfo.Renderer, window.getWidth() - 30, -510);
		rendererR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		backButton.setPositionRelativeToRoot(false);
		window.addChildren(backButton);
		window.addChildren(versionL);
		window.addChildren(versionR);
		window.addChildren(osL);
		window.addChildren(osR);
		window.addChildren(lwjglL);
		window.addChildren(lwjglR);
		window.addChildren(glfwL);
		window.addChildren(glfwR);
		window.addChildren(openglL);
		window.addChildren(openglR);
		window.addChildren(vkL);
		window.addChildren(vkR);
		window.addChildren(vendorL);
		window.addChildren(vendorR);
		window.addChildren(rendererL);
		window.addChildren(rendererR);
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
	public void render(AbstractVoxel voxel, float delta) {
		GameResources gm = (GameResources)voxel.getGameResources();
		MasterRenderer.prepare(0, 0, 0, 1);
		gm.getDisplay().beingNVGFrame();
		window.render();
		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		globalY += (int) (Mouse.getDWheel() * 60f);
		if (globalY > 0)
			globalY = 0;
		else if (globalY < -1520)
			globalY = -1520;
		window.setPosition(20, ((GameResources)voxel.getGameResources()).getDisplay().getDisplayHeight() - 20 - globalY);
		window.update(delta);
		/* TODO: Need to find a way to reimplement this with the new StateMachine
		if (!switching)
			window.fadeIn(4, delta);
		if (switching) {
			if (window.fadeOut(4, delta)) {
				readyForSwitch = true;
			}
		}
		 */
	}

}
