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

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glReadPixels;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.ClientVariables;
//import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
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
		exitButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() + 35, 200, 40, "Back to Main Menu");
		optionsButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() + 85, 200, 40, "Options");
		exitButton.setOnButtonPress((button, delta) -> {
			GameResources.getInstance().getWorldsHandler().getActiveWorld().dispose();
			GameResources.getInstance().getCamera().setPosition(new Vector3f(0, 0, 1));
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
		GameResources gm = ((GameResources)voxel.getGameResources());
		Window window = gm.getGameWindow();
		while (Mouse.next())
			uiWindow.update(delta);
		
		super.update(voxel, delta);

		//while (Keyboard.next()) {
		//	if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
		if(window.getKeyboardHandler().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			window.getKeyboardHandler().ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
			((PlayerCamera) gm.getCamera()).setMouse();
			this.switchTo(StateNames.SINGLEPLAYER);
		}
		//}
	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		GameResources gm = (GameResources)voxel.getGameResources();
		Window window = gm.getGameWindow();

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().lighting();
		gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
		gm.getFrustum().calculateFrustum(gm.getMasterShadowRenderer().getProjectionMatrix(), gm.getSun_Camera());
		if (ClientVariables.useShadows) {
			gm.getMasterShadowRenderer().being();
			MasterRenderer.prepare(0, 0, 0, 1);
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksShadow(gm);
			gm.getItemsDropRenderer().getTess().drawShadow(gm.getSun_Camera());
			gm.getMasterShadowRenderer().renderEntity(
					gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
			gm.getMasterShadowRenderer().end();
		}
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		MasterRenderer.prepare(0, 0, 0, 1);
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksOcclusion(gm);

		gm.getRenderingPipeline().begin();
		MasterRenderer.prepare(0, 0, 0, 1);
		gm.getSkyboxRenderer().render(ClientVariables.RED, ClientVariables.GREEN, ClientVariables.BLUE, delta, gm);
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, false);
		FloatBuffer p = BufferUtils.createFloatBuffer(1);
		glReadPixels(window.getWidth() / 2, window.getHeight() / 2, 1, 1,
				GL_DEPTH_COMPONENT, GL_FLOAT, p);
		gm.getCamera().depth = p.get(0);
		gm.getRenderer().renderEntity(
				gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
		gm.getItemsDropRenderer().render(gm);
		gm.getRenderingPipeline().end();

		MasterRenderer.prepare(0, 0, 0, 1);
		gm.getRenderingPipeline().render(gm);
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, true);
		ParticleMaster.getInstance().render(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
		
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
