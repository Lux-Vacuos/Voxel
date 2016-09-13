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

//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_ESCAPE;
//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_F1;
//import static net.luxvacuos.voxel.client.input.Keyboard.KEY_F2;
//import static net.luxvacuos.voxel.client.input.Keyboard.isKeyDown;
//import static net.luxvacuos.voxel.client.input.Keyboard.next;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
//import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.PhysicsSystem;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * Single Player State, here the local world is updated and rendered.
 * 
 * @author danirod
 * @category Kernel
 */
public class SPState extends AbstractState {
	FloatBuffer p;
	FloatBuffer c;

	public SPState() {
		super(StateNames.SINGLEPLAYER);
		p = BufferUtils.createFloatBuffer(1);
		c = BufferUtils.createFloatBuffer(3);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		GameResources gm = (GameResources) voxel.getGameResources();

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksGeneration(gm, delta);
		((PlayerCamera) gm.getCamera()).update(delta, gm, gm.getWorldsHandler().getActiveWorld().getActiveDimension());

		for (Dimension dim : gm.getWorldsHandler().getActiveWorld().getDimensions().values()) {
			dim.getPhysicsEngine().update(delta);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processItems(gm);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processEntities(gm);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).doSpawn(gm);
		}

		gm.update(gm.getWorldSimulation().update(delta), delta);
		ParticleMaster.getInstance().update(delta, gm.getCamera());
		KeyboardHandler kbh = gm.getGameWindow().getKeyboardHandler();
		
		if(kbh.isKeyPressed(GLFW.GLFW_KEY_F1)) ClientVariables.debug = !ClientVariables.debug;
		if(kbh.isKeyPressed(GLFW.GLFW_KEY_F2)) ClientVariables.hideHud = !ClientVariables.hideHud;
		if(kbh.isKeyPressed(GLFW.GLFW_KEY_R)) ClientVariables.raining = !ClientVariables.raining;
		if(kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE, false)) {
			((PlayerCamera) gm.getCamera()).unlockMouse();
			// gm.getGlobalStates().setState(GameState.SP_PAUSE);
			StateMachine.setCurrentState(StateNames.SP_PAUSE);
		}
		
		/* while (next()) {
			if (isKeyDown(KEY_F1))
				ClientVariables.debug = !ClientVariables.debug;
			if (isKeyDown(KEY_F2))
				ClientVariables.hideHud = !ClientVariables.hideHud;
			if (isKeyDown(KEY_ESCAPE)) {
				((PlayerCamera) gm.getCamera()).unlockMouse();
				// gm.getGlobalStates().setState(GameState.SP_PAUSE);
				StateMachine.setCurrentState(StateNames.SP_PAUSE);
			}
			// TODO: Handle Inventory
			//
			// if (isKeyDown(KEY_E)) {
			// ((PlayerCamera) gm.getCamera()).unlockMouse();
			// gm.getGlobalStates().setState(GameState.GAME_SP_INVENTORY);
			// }

			if (Keyboard.isKeyDown(Keyboard.KEY_R))
				ClientVariables.raining = !ClientVariables.raining;
		} */

	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		GameResources gm = (GameResources) voxel.getGameResources();
		Window window = gm.getGameWindow();
		
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().lighting();
		gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
		gm.getFrustum().calculateFrustum(gm.getMasterShadowRenderer().getProjectionMatrix(), gm.getSun_Camera());
		if (ClientVariables.useShadows) {
			gm.getMasterShadowRenderer().being();
			MasterRenderer.prepare();
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksShadow(gm);
			gm.getItemsDropRenderer().getTess().drawShadow(gm.getSun_Camera());
			gm.getMasterShadowRenderer().renderEntity(
					gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
			gm.getMasterShadowRenderer().end();
		}
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		MasterRenderer.prepare();
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksOcclusion(gm);

		gm.getRenderingPipeline().begin();
		MasterRenderer.prepare();
		gm.getSkyboxRenderer().render(ClientVariables.RED, ClientVariables.GREEN, ClientVariables.BLUE, delta, gm);
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, false);
		gm.getRenderer().renderEntity(
				gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
		p.clear();
		glReadPixels(window.getWidth() / 2, window.getHeight() / 2, 1, 1,
				GL_DEPTH_COMPONENT, GL_FLOAT, p);
		c.clear();
		glReadBuffer(GL_COLOR_ATTACHMENT2);
		glReadPixels(window.getWidth() / 2, window.getHeight() / 2, 1, 1, GL_RGB,
				GL_FLOAT, c);
		gm.getCamera().depth = p.get(0);
		gm.getCamera().normal.x = c.get(0);
		gm.getCamera().normal.y = c.get(1);
		gm.getCamera().normal.z = c.get(2);
		gm.getItemsDropRenderer().render(gm);
		gm.getRenderingPipeline().end();

		MasterRenderer.prepare();
		gm.getRenderingPipeline().render(gm);

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, true);
		gm.getCamera().render();

		ParticleMaster.getInstance().render(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
		window.beingNVGFrame();
		if (ClientVariables.debug) {
			UIRendering.renderText(window.getID(), "Voxel " + " (" + ClientVariables.version + ")", "Roboto-Bold", 5, 12, 20,
					UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(), "Used VRam: " + WindowManager.getUsedVRAM() + "KB " + " UPS: " + CoreInfo.ups,
					"Roboto-Bold", 5, 95, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(), 
					"Loaded Chunks: " + gm.getWorldsHandler().getActiveWorld().getActiveDimension().getLoadedChunks()
							+ "   Rendered Chunks: "
							+ gm.getWorldsHandler().getActiveWorld().getActiveDimension().getRenderedChunks(),
					"Roboto-Bold", 5, 115, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(), 
					"Position XYZ:  " + gm.getCamera().getPosition().getX() + "  " + gm.getCamera().getPosition().getY()
							+ "  " + gm.getCamera().getPosition().getZ(),
					"Roboto-Bold", 5, 135, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(), 
					"Pitch:  " + gm.getCamera().getPitch() + "   Yaw: " + gm.getCamera().getYaw() + "   Roll: "
							+ gm.getCamera().getRoll(),
					"Roboto-Bold", 5, 155, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			Timers.renderDebugDisplay(5, 24, 200, 55);
		}
		window.endNVGFrame();
		gm.getItemsGuiRenderer().render(gm);

	}

}
