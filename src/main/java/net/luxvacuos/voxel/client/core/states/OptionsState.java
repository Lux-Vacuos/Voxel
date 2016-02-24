/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.luxvacuos.voxel.client.core.states;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glReadPixels;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import net.luxvacuos.voxel.client.core.GlobalStates;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.particle.ParticleMaster;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.WorldsHandler;

/**
 * Options Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class OptionsState implements State {

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
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
				states.setState(states.getOldState());
			}
		}
		gm.getMenuSystem().optionsMenu.update();
		gm.getRenderer().update(gm);
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		WorldsHandler worlds = voxel.getWorldsHandler();
		if (states.getOldState().equals(GameState.IN_PAUSE)) {
			worlds.getActiveWorld().lighting();
			gm.getFrustum().calculateFrustum(gm.getMasterShadowRenderer().getProjectionMatrix(), gm.getSun_Camera());
			if (VoxelVariables.useShadows) {
				gm.getMasterShadowRenderer().being();
				gm.getRenderer().prepare();
				worlds.getActiveWorld().updateChunksShadow(gm);
				gm.getMasterShadowRenderer().renderEntity(gm.getPhysicsEngine().getEntities(), gm);
				gm.getMasterShadowRenderer().end();
			}
			gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
			gm.getRenderer().prepare();
			worlds.getActiveWorld().updateChunksOcclusion(gm);

			gm.getDeferredShadingRenderer().getPost_fbo().begin();
			gm.getRenderer().prepare();
			gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
			worlds.getActiveWorld().updateChunksRender(gm);
			FloatBuffer p = BufferUtils.createFloatBuffer(1);
			glReadPixels(gm.getDisplay().getDisplayWidth() / 2, gm.getDisplay().getDisplayHeight() / 2, 1, 1,
					GL_DEPTH_COMPONENT, GL_FLOAT, p);
			gm.getCamera().depth = p.get(0);
			gm.getRenderer().renderEntity(gm.getPhysicsEngine().getEntities(), gm);
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
