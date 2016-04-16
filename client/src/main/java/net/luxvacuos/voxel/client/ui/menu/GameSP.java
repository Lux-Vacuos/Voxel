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

package net.luxvacuos.voxel.client.ui.menu;

import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.components.LifeComponent;
import net.luxvacuos.voxel.universal.api.MoltenAPI;

public class GameSP {
	private float xScale, yScale;

	private float x, y, w, h;

	public GameSP(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		x = gm.getDisplay().getDisplayWidth() / 2;
		y = gm.getDisplay().getDisplayHeight() / 2;
		w = 16;
		h = 16;
	}

	public void render(GameResources gm, Dimension world) {
		if (!VoxelVariables.hideHud)
			renderHud(gm);

		if (VoxelVariables.debug) {
			VectorsRendering.renderText(
					"Voxel " + " (" + VoxelVariables.version + "/" + VoxelVariables.build + ")" + " Molten API" + " ("
							+ MoltenAPI.apiVersion + "/" + MoltenAPI.build + ")",
					"Roboto-Bold", 5 * xScale, 12 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			VectorsRendering.renderText("Used VRam: " + gm.getDisplay().getUsedVRAM() + "KB " + " UPS: " + CoreInfo.ups,
					"Roboto-Bold", 5 * xScale, 100 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			VectorsRendering.renderText(
					"Loaded Chunks: " + world.getLoadedChunks() + "   Rendered Chunks: " + world.getRenderedChunks(),
					"Roboto-Bold", 5 * xScale, 120 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			VectorsRendering.renderText(
					"Position XYZ:  " + gm.getCamera().getPosition().getX() + "  " + gm.getCamera().getPosition().getY()
							+ "  " + gm.getCamera().getPosition().getZ(),
					"Roboto-Bold", 5 * xScale, 142 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			VectorsRendering.renderText(
					"Pitch:  " + gm.getCamera().getPitch() + "   Yaw: " + gm.getCamera().getYaw() + "   Roll: "
							+ gm.getCamera().getRoll(),
					"Roboto-Bold", 5 * xScale, 164 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			Timers.renderDebugDisplay(5 * xScale, 24 * yScale, 300 * xScale, 55 * yScale);
		}

		VectorsRendering.renderLife(440 * xScale, 700 * yScale, 200 * xScale, 20 * yScale,
				((PlayerCamera) gm.getCamera()).getComponent(LifeComponent.class).life / 20f);

	}

	private void renderHud(GameResources gm) {

		VectorsRendering.renderBox(x - 8, y - 8, w, h,
				VectorsRendering.rgba(255, 255, 255, 200, VectorsRendering.colorA),
				VectorsRendering.rgba(32, 32, 32, 32, VectorsRendering.colorB),
				VectorsRendering.rgba(0, 0, 0, 48, VectorsRendering.colorC));
	}

}
