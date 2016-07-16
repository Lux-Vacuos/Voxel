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

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Window;

/**
 * Options State, here all the options are stored.
 * 
 * @author danirod
 */
public class OptionsState extends State {

	private Button exitButton;
	private Button dofButton;
	private Button shadowsButton;
	private Button godraysButton;
	private Button fxaaButton;
	private Button motionBlurButton;
	private Button reflectionsButton;
	private Button parallaxButton;
	private Window window;

	public OptionsState() {

		window = new Window(20, GameResources.getInstance().getDisplay().getDisplayHeight() - 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40, "Options");

		exitButton = new Button(window.getWidth() / 2 - 100, -window.getHeight() + 35, 200, 40, "Back");
		godraysButton = new Button(40, -110, 200, 40, "Light Rays");
		shadowsButton = new Button(40, -170, 200, 40, "Shadows");
		dofButton = new Button(40, -230, 200, 40, "DoF");
		fxaaButton = new Button(40, -290, 200, 40, "FXAA");
		motionBlurButton = new Button(40, -350, 200, 40, "Motion Blur");

		reflectionsButton = new Button(260, -110, 200, 40, "Reflections");
		parallaxButton = new Button(260, -170, 200, 40, "Parallax");

		if (VoxelVariables.useVolumetricLight) {
			godraysButton.setText("Light Rays: ON");
			godraysButton.setColor(100, 255, 100, 255);
		} else {
			godraysButton.setText("Light Rays: OFF");
			godraysButton.setColor(255, 100, 100, 255);
		}

		if (VoxelVariables.useShadows) {
			shadowsButton.setText("Shadows: ON");
			shadowsButton.setColor(100, 255, 100, 255);
		} else {
			shadowsButton.setText("Shadows: OFF");
			shadowsButton.setColor(255, 100, 100, 255);
		}

		if (VoxelVariables.useDOF) {
			dofButton.setText("DoF: ON");
			dofButton.setColor(100, 255, 100, 255);
		} else {
			dofButton.setText("DoF: OFF");
			dofButton.setColor(255, 100, 100, 255);
		}

		if (VoxelVariables.useFXAA) {
			fxaaButton.setText("FXAA: ON");
			fxaaButton.setColor(100, 255, 100, 255);
		} else {
			fxaaButton.setText("FXAA: OFF");
			fxaaButton.setColor(255, 100, 100, 255);
		}

		if (VoxelVariables.useMotionBlur) {
			motionBlurButton.setText("Motion Blur: ON");
			motionBlurButton.setColor(100, 255, 100, 255);
		} else {
			motionBlurButton.setText("Motion Blur: OFF");
			motionBlurButton.setColor(255, 100, 100, 255);
		}

		if (VoxelVariables.useReflections) {
			reflectionsButton.setText("Reflections: ON");
			reflectionsButton.setColor(100, 255, 100, 255);
		} else {
			reflectionsButton.setText("Reflections: OFF");
			reflectionsButton.setColor(255, 100, 100, 255);
		}

		if (VoxelVariables.useParallax) {
			parallaxButton.setText("Parallax: ON");
			parallaxButton.setColor(100, 255, 100, 255);
		} else {
			parallaxButton.setText("Parallax: OFF");
			parallaxButton.setColor(255, 100, 100, 255);
		}

		exitButton.setOnButtonPress((button, delta) -> {
			GameResources.getInstance().getGameSettings().updateSetting();
			GameResources.getInstance().getGameSettings().save();
			switchTo(GameResources.getInstance().getGlobalStates().getOldState());
		});

		shadowsButton.setOnButtonPress((button, delta) -> {
			VoxelVariables.useShadows = !VoxelVariables.useShadows;
			if (VoxelVariables.useShadows) {
				shadowsButton.setText("Shadows: ON");
				shadowsButton.setColor(100, 255, 100, 255);
			} else {
				shadowsButton.setText("Shadows: OFF");
				shadowsButton.setColor(255, 100, 100, 255);
			}
		});

		dofButton.setOnButtonPress((button, delta) -> {
			VoxelVariables.useDOF = !VoxelVariables.useDOF;
			if (VoxelVariables.useDOF) {
				dofButton.setText("DoF: ON");
				dofButton.setColor(100, 255, 100, 255);
			} else {
				dofButton.setText("DoF: OFF");
				dofButton.setColor(255, 100, 100, 255);
			}

		});

		godraysButton.setOnButtonPress((button, delta) -> {
			VoxelVariables.useVolumetricLight = !VoxelVariables.useVolumetricLight;
			if (VoxelVariables.useVolumetricLight) {
				godraysButton.setText("Light Rays: ON");
				godraysButton.setColor(100, 255, 100, 255);
			} else {
				godraysButton.setText("Light Rays: OFF");
				godraysButton.setColor(255, 100, 100, 255);
			}
		});

		fxaaButton.setOnButtonPress((button, delta) -> {
			VoxelVariables.useFXAA = !VoxelVariables.useFXAA;

			if (VoxelVariables.useFXAA) {
				fxaaButton.setText("FXAA: ON");
				fxaaButton.setColor(100, 255, 100, 255);
			} else {
				fxaaButton.setText("FXAA: OFF");
				fxaaButton.setColor(255, 100, 100, 255);
			}
		});

		parallaxButton.setOnButtonPress((button, delta) -> {
			VoxelVariables.useParallax = !VoxelVariables.useParallax;
			if (VoxelVariables.useParallax) {
				parallaxButton.setText("Parallax: ON");
				parallaxButton.setColor(100, 255, 100, 255);
			} else {
				parallaxButton.setText("Parallax: OFF");
				parallaxButton.setColor(255, 100, 100, 255);
			}

		});

		motionBlurButton.setOnButtonPress((button, delta) -> {
			VoxelVariables.useMotionBlur = !VoxelVariables.useMotionBlur;
			if (VoxelVariables.useMotionBlur) {
				motionBlurButton.setText("Motion Blur: ON");
				motionBlurButton.setColor(100, 255, 100, 255);
			} else {
				motionBlurButton.setText("Motion Blur: OFF");
				motionBlurButton.setColor(255, 100, 100, 255);
			}

		});

		reflectionsButton.setOnButtonPress((button, delta) -> {
			VoxelVariables.useReflections = !VoxelVariables.useReflections;
			if (VoxelVariables.useReflections) {
				reflectionsButton.setText("Reflections: ON");
				reflectionsButton.setColor(100, 255, 100, 255);
			} else {
				reflectionsButton.setText("Reflections: OFF");
				reflectionsButton.setColor(255, 100, 100, 255);
			}
		});

		window.addChildren(exitButton);
		window.addChildren(shadowsButton);
		//window.addChildren(dofButton);
		window.addChildren(godraysButton);
		//window.addChildren(fxaaButton);
		//window.addChildren(parallaxButton);
		//window.addChildren(motionBlurButton);
		window.addChildren(reflectionsButton);

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
	public void update(Voxel voxel, float delta) {
		GameResources gm = voxel.getGameResources();
		while (Mouse.next()) {
			window.update(delta);
		}

		if (!switching)
			window.fadeIn(4, delta);
		if (switching)
			if (window.fadeOut(4, delta)) {
				readyForSwitch = true;
			}

		gm.getRenderer().update(gm);
	}

	@Override
	public void render(Voxel voxel, float delta) {
		GameResources gm = voxel.getGameResources();
		if (gm.getGlobalStates().getOldState().equals(GameState.SP_PAUSE)) {
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().lighting();
			gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
			gm.getFrustum().calculateFrustum(gm.getMasterShadowRenderer().getProjectionMatrix(), gm.getSun_Camera());
			if (VoxelVariables.useShadows) {
				gm.getMasterShadowRenderer().being();
				MasterRenderer.prepare();
				gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksShadow(gm);
				gm.getItemsDropRenderer().getTess().drawShadow(gm.getSun_Camera());
				gm.getMasterShadowRenderer().renderEntity(
						gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(),
						gm);
				gm.getMasterShadowRenderer().end();
			}
			gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
			MasterRenderer.prepare();
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksOcclusion(gm);

			gm.getRenderingPipeline().begin();
			MasterRenderer.prepare();
			gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, false);
			gm.getRenderer().renderEntity(
					gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
			gm.getItemsDropRenderer().render(gm);
			gm.getRenderingPipeline().end();
			MasterRenderer.prepare();
			gm.getRenderingPipeline().render(gm);
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, true);
			ParticleMaster.getInstance().render(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
		} else {
			MasterRenderer.prepare(0, 0, 0, 1);
		}

		gm.getDisplay().beingNVGFrame();
		window.render();
		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

}
