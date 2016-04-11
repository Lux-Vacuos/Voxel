
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

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.particle.ParticleMaster;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

/**
 * Options Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class OptionsState implements State {

	@Override
	public void update(Voxel voxel, float delta) {
		GameResources gm = voxel.getGameResources();

		while (Mouse.next()) {
			if (gm.getMenuSystem().optionsMenu.getShadowsButton().pressed())
				VoxelVariables.useShadows = !VoxelVariables.useShadows;
			if (gm.getMenuSystem().optionsMenu.getDofButton().pressed())
				VoxelVariables.useDOF = !VoxelVariables.useDOF;
			if (gm.getMenuSystem().optionsMenu.getGodraysButton().pressed())
				VoxelVariables.useVolumetricLight = !VoxelVariables.useVolumetricLight;
			if (gm.getMenuSystem().optionsMenu.getFxaaButton().pressed())
				VoxelVariables.useFXAA = !VoxelVariables.useFXAA;

			if (gm.getMenuSystem().optionsMenu.getParallaxButton().pressed())
				VoxelVariables.useParallax = !VoxelVariables.useParallax;

			if (gm.getMenuSystem().optionsMenu.getMotionBlurButton().pressed())
				VoxelVariables.useMotionBlur = !VoxelVariables.useMotionBlur;

			if (gm.getMenuSystem().optionsMenu.getReflectionsButton().pressed())
				VoxelVariables.useReflections = !VoxelVariables.useReflections;

			if (gm.getMenuSystem().optionsMenu.getExitButton().pressed()) {
				gm.getGameSettings().updateSetting();
				gm.getGameSettings().save();
				gm.getGlobalStates().setState(gm.getGlobalStates().getOldState());
			}
		}
		gm.getMenuSystem().optionsMenu.update();
		gm.getRenderer().update(gm);
	}

	@Override
	public void render(Voxel voxel, float delta) {
		GameResources gm = voxel.getGameResources();
		if (gm.getGlobalStates().getOldState().equals(GameState.IN_PAUSE)) {
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().lighting();
			gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
			gm.getFrustum().calculateFrustum(gm.getMasterShadowRenderer().getProjectionMatrix(), gm.getSun_Camera());
			if (VoxelVariables.useShadows) {
				gm.getMasterShadowRenderer().being();
				gm.getRenderer().prepare();
				gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksShadow(gm);
				gm.getItemsDropRenderer().getTess().drawShadow(gm.getSun_Camera());
				gm.getMasterShadowRenderer().renderEntity(
						gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(),
						gm);
				gm.getMasterShadowRenderer().end();
			}
			gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
			gm.getRenderer().prepare();
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksOcclusion(gm);

			gm.getDeferredShadingRenderer().getPost_fbo().begin();
			gm.getRenderer().prepare();
			gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm);
			FloatBuffer p = BufferUtils.createFloatBuffer(1);
			glReadPixels(gm.getDisplay().getDisplayWidth() / 2, gm.getDisplay().getDisplayHeight() / 2, 1, 1,
					GL_DEPTH_COMPONENT, GL_FLOAT, p);
			gm.getCamera().depth = p.get(0);
			gm.getRenderer().renderEntity(
					gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
			gm.getItemsDropRenderer().render(gm);
			gm.getDeferredShadingRenderer().getPost_fbo().end();
			gm.getRenderer().prepare();
			gm.getDeferredShadingRenderer().render(gm);
			ParticleMaster.getInstance().render(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
		} else {
			gm.getRenderer().prepare();
		}

		gm.getDisplay().beingNVGFrame();
		gm.getMenuSystem().optionsMenu.render();
		VectorsRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

}
