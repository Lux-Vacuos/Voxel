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

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
//import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.PhysicsSystem;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;

/**
 * Multiplayer State, like {@link SPState}, here the remote world is updated and
 * rendered.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class MPState extends AbstractState {

	public MPState() {
		super(StateNames.MULTIPLAYER);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		GameResources gm = (GameResources) voxel.getGameResources();
		Window window = gm.getGameWindow();

		gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
		gm.getRenderer().render(gm.getWorldsHandler().getActiveWorld().getActiveDimension(), gm.getCamera(),
				gm.getSun_Camera(), gm.getWorldSimulation(), gm.getLightPos(), gm.getInvertedLightPosition(), alpha);
		window.beingNVGFrame();
		if (ClientVariables.debug) {
			UIRendering.renderText(window.getID(), "Voxel " + " (" + ClientVariables.version + ")", "Roboto-Bold", 5,
					12, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Used VRam: " + WindowManager.getUsedVRAM() + "KB " + " UPS: " + CoreInfo.ups, "Roboto-Bold", 5, 95,
					20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
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
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		GameResources gm = (GameResources) voxel.getGameResources();
		((PlayerCamera) gm.getCamera()).update(delta, gm, gm.getWorldsHandler().getActiveWorld().getActiveDimension());

		for (Dimension dim : gm.getWorldsHandler().getActiveWorld().getDimensions().values()) {
			dim.getPhysicsEngine().update(delta);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processItems(gm);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processEntities(gm);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).doSpawn(gm);
		}

		gm.update(gm.getWorldSimulation().update(delta), delta);
		ParticleMaster.getInstance().update(delta, gm.getCamera());

		if (gm.getGameWindow().getKeyboardHandler().isKeyPressed(GLFW.GLFW_KEY_F1))
			ClientVariables.debug = !ClientVariables.debug;

		if (gm.getGameWindow().getKeyboardHandler().isKeyPressed(GLFW.GLFW_KEY_R))
			ClientVariables.raining = !ClientVariables.raining;
		/*
		 * while (next()) { if (isKeyDown(KEY_F1)) ClientVariables.debug =
		 * !ClientVariables.debug; if (Keyboard.isKeyDown(Keyboard.KEY_R))
		 * ClientVariables.raining = !ClientVariables.raining; }
		 */
	}

}
