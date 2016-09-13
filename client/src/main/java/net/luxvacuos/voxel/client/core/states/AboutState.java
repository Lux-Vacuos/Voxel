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
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Image;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

/**
 * About State, this shows all the information about Voxel and the system where
 * is running.
 * 
 * @author danirod
 */
public class AboutState extends AbstractFadeState {

	private UIWindow uiWindow;
	private Image voxelLogo;

	public AboutState() {
		super(StateNames.ABOUT);
		Window window = GameResources.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20,
				window.getWidth() - 40,
				window.getHeight() - 40, "About");
		Button backButton = new Button((int) (window.getWidth() / 2f - 100),
				40, 200, 40, "Back");
		backButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.MAIN_MENU);
		});

		voxelLogo = new Image(uiWindow.getWidth() / 2 - 200, -40, 400, 200,
				GameResources.getInstance().getResourceLoader().loadNVGTexture("Voxel-Logo"));
		uiWindow.addChildren(voxelLogo);

		Text versionL = new Text("Version", 30, -300);
		versionL.setFont("Roboto-Bold");
		Text versionR = new Text(" (" + ClientVariables.version + ")", uiWindow.getWidth() - 30, -300);
		versionR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text osL = new Text("Operative System", 30, -330);
		osL.setFont("Roboto-Bold");
		Text osR = new Text(CoreInfo.OS, uiWindow.getWidth() - 30, -330);
		osR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text lwjglL = new Text("LWJGL Version", 30, -360);
		lwjglL.setFont("Roboto-Bold");
		Text lwjglR = new Text(CoreInfo.LWJGLVer, uiWindow.getWidth() - 30, -360);
		lwjglR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text glfwL = new Text("GLFW Version", 30, -390);
		glfwL.setFont("Roboto-Bold");
		Text glfwR = new Text(CoreInfo.GLFWVer, uiWindow.getWidth() - 30, -390);
		glfwR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text openglL = new Text("OpenGL Version", 30, -420);
		openglL.setFont("Roboto-Bold");
		Text openglR = new Text(CoreInfo.OpenGLVer, uiWindow.getWidth() - 30, -420);
		openglR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text vkL = new Text("Vulkan Version", 30, -450);
		vkL.setFont("Roboto-Bold");
		Text vkR = new Text(CoreInfo.VkVersion, uiWindow.getWidth() - 30, -450);
		vkR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text vendorL = new Text("Vendor", 30, -480);
		vendorL.setFont("Roboto-Bold");
		Text vendorR = new Text(CoreInfo.Vendor, uiWindow.getWidth() - 30, -480);
		vendorR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		Text rendererL = new Text("Renderer", 30, -510);
		rendererL.setFont("Roboto-Bold");
		Text rendererR = new Text(CoreInfo.Renderer, uiWindow.getWidth() - 30, -510);
		rendererR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);

		backButton.setPositionRelativeToRoot(false);
		uiWindow.addChildren(backButton);
		uiWindow.addChildren(versionL);
		uiWindow.addChildren(versionR);
		uiWindow.addChildren(osL);
		uiWindow.addChildren(osR);
		uiWindow.addChildren(lwjglL);
		uiWindow.addChildren(lwjglR);
		uiWindow.addChildren(glfwL);
		uiWindow.addChildren(glfwR);
		uiWindow.addChildren(openglL);
		uiWindow.addChildren(openglR);
		uiWindow.addChildren(vkL);
		uiWindow.addChildren(vkR);
		uiWindow.addChildren(vendorL);
		uiWindow.addChildren(vendorR);
		uiWindow.addChildren(rendererL);
		uiWindow.addChildren(rendererR);
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
	public void render(AbstractVoxel voxel, float delta) {
		Window window = ((GameResources) voxel.getGameResources()).getGameWindow();
		MasterRenderer.prepare(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
		UIRendering.renderMouse(window.getID());
		window.endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		uiWindow.update(delta);
		super.update(voxel, delta);
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
