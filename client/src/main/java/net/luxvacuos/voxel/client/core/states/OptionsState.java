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

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * Options State, here all the options are stored.
 * 
 * @author danirod
 */
public class OptionsState extends AbstractFadeState {

	private Button exitButton;
	private Button dofButton;
	private Button shadowsButton;
	private Button godraysButton;
	private Button fxaaButton;
	private Button motionBlurButton;
	private Button reflectionsButton;
	private Button parallaxButton;
	private Button ambientOccButton;
	private UIWindow uiWindow;

	public OptionsState() {
		super(StateNames.OPTIONS);
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40,
				"Options");

		exitButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() + 35, 200, 40, "Back");
		godraysButton = new Button(40, -110, 200, 40, "Volumetric Light");
		shadowsButton = new Button(40, -170, 200, 40, "Shadows");
		dofButton = new Button(40, -230, 200, 40, "Depth of Field");
		fxaaButton = new Button(40, -290, 200, 40, "FXAA");
		motionBlurButton = new Button(40, -350, 200, 40, "Motion Blur");

		reflectionsButton = new Button(260, -110, 200, 40, "Reflections");
		parallaxButton = new Button(260, -170, 200, 40, "Parallax");

		ambientOccButton = new Button(260, -230, 200, 40, "Ambient Occlusion");

		if (ClientVariables.useVolumetricLight) {
			godraysButton.setText("Volumetric Light: ON");
			godraysButton.setColor(100, 255, 100, 255);
		} else {
			godraysButton.setText("Volumetric Light: OFF");
			godraysButton.setColor(255, 100, 100, 255);
		}

		if (ClientVariables.useShadows) {
			shadowsButton.setText("Shadows: ON");
			shadowsButton.setColor(100, 255, 100, 255);
		} else {
			shadowsButton.setText("Shadows: OFF");
			shadowsButton.setColor(255, 100, 100, 255);
		}

		if (ClientVariables.useDOF) {
			dofButton.setText("Depth of Field: ON");
			dofButton.setColor(100, 255, 100, 255);
		} else {
			dofButton.setText("Depth of Field: OFF");
			dofButton.setColor(255, 100, 100, 255);
		}

		if (ClientVariables.useFXAA) {
			fxaaButton.setText("FXAA: ON");
			fxaaButton.setColor(100, 255, 100, 255);
		} else {
			fxaaButton.setText("FXAA: OFF");
			fxaaButton.setColor(255, 100, 100, 255);
		}

		if (ClientVariables.useMotionBlur) {
			motionBlurButton.setText("Motion Blur: ON");
			motionBlurButton.setColor(100, 255, 100, 255);
		} else {
			motionBlurButton.setText("Motion Blur: OFF");
			motionBlurButton.setColor(255, 100, 100, 255);
		}

		if (ClientVariables.useReflections) {
			reflectionsButton.setText("Reflections: ON");
			reflectionsButton.setColor(100, 255, 100, 255);
		} else {
			reflectionsButton.setText("Reflections: OFF");
			reflectionsButton.setColor(255, 100, 100, 255);
		}

		if (ClientVariables.useParallax) {
			parallaxButton.setText("Parallax: ON");
			parallaxButton.setColor(100, 255, 100, 255);
		} else {
			parallaxButton.setText("Parallax: OFF");
			parallaxButton.setColor(255, 100, 100, 255);
		}

		if (ClientVariables.useAmbientOcclusion) {
			ambientOccButton.setText("Ambient Occlusion: ON");
			ambientOccButton.setColor(100, 255, 100, 255);
		} else {
			ambientOccButton.setText("Ambient Occlusion: OFF");
			ambientOccButton.setColor(255, 100, 100, 255);
		}

		exitButton.setOnButtonPress((button, delta) -> {
			ClientInternalSubsystem.getInstance().getGameSettings().update();
			ClientInternalSubsystem.getInstance().getGameSettings().save();
			this.switchTo(StateMachine.getPreviousState().getName());
		});

		shadowsButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useShadows = !ClientVariables.useShadows;
			if (ClientVariables.useShadows) {
				shadowsButton.setText("Shadows: ON");
				shadowsButton.setColor(100, 255, 100, 255);
			} else {
				shadowsButton.setText("Shadows: OFF");
				shadowsButton.setColor(255, 100, 100, 255);
			}
		});

		dofButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useDOF = !ClientVariables.useDOF;
			if (ClientVariables.useDOF) {
				dofButton.setText("Depth of Field: ON");
				dofButton.setColor(100, 255, 100, 255);
			} else {
				dofButton.setText("Depth of Field: OFF");
				dofButton.setColor(255, 100, 100, 255);
			}

		});

		godraysButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useVolumetricLight = !ClientVariables.useVolumetricLight;
			if (ClientVariables.useVolumetricLight) {
				godraysButton.setText("Volumetric Light: ON");
				godraysButton.setColor(100, 255, 100, 255);
			} else {
				godraysButton.setText("Volumetric Light: OFF");
				godraysButton.setColor(255, 100, 100, 255);
			}
		});

		fxaaButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useFXAA = !ClientVariables.useFXAA;

			if (ClientVariables.useFXAA) {
				fxaaButton.setText("FXAA: ON");
				fxaaButton.setColor(100, 255, 100, 255);
			} else {
				fxaaButton.setText("FXAA: OFF");
				fxaaButton.setColor(255, 100, 100, 255);
			}
		});

		parallaxButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useParallax = !ClientVariables.useParallax;
			if (ClientVariables.useParallax) {
				parallaxButton.setText("Parallax: ON");
				parallaxButton.setColor(100, 255, 100, 255);
			} else {
				parallaxButton.setText("Parallax: OFF");
				parallaxButton.setColor(255, 100, 100, 255);
			}

		});

		motionBlurButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useMotionBlur = !ClientVariables.useMotionBlur;
			if (ClientVariables.useMotionBlur) {
				motionBlurButton.setText("Motion Blur: ON");
				motionBlurButton.setColor(100, 255, 100, 255);
			} else {
				motionBlurButton.setText("Motion Blur: OFF");
				motionBlurButton.setColor(255, 100, 100, 255);
			}

		});

		reflectionsButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useReflections = !ClientVariables.useReflections;
			if (ClientVariables.useReflections) {
				reflectionsButton.setText("Reflections: ON");
				reflectionsButton.setColor(100, 255, 100, 255);
			} else {
				reflectionsButton.setText("Reflections: OFF");
				reflectionsButton.setColor(255, 100, 100, 255);
			}
		});

		ambientOccButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useAmbientOcclusion = !ClientVariables.useAmbientOcclusion;
			if (ClientVariables.useAmbientOcclusion) {
				ambientOccButton.setText("Ambient Occlusion: ON");
				ambientOccButton.setColor(100, 255, 100, 255);
			} else {
				ambientOccButton.setText("Ambient Occlusion: OFF");
				ambientOccButton.setColor(255, 100, 100, 255);
			}
		});

		uiWindow.addChildren(exitButton);
		uiWindow.addChildren(shadowsButton);
		uiWindow.addChildren(dofButton);
		uiWindow.addChildren(godraysButton);
		uiWindow.addChildren(fxaaButton);
		uiWindow.addChildren(parallaxButton);
		uiWindow.addChildren(motionBlurButton);
		uiWindow.addChildren(reflectionsButton);
		uiWindow.addChildren(ambientOccButton);

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
		while (Mouse.next()) {
			uiWindow.update(delta);
		}
		super.update(voxel, delta);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		Renderer.prepare(1, 1, 1, 1);
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
