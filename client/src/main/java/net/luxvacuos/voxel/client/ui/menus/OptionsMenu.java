/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.client.ui.menus;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.*;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.RootComponent;
import net.luxvacuos.voxel.client.ui.Slider;
import net.luxvacuos.voxel.client.ui.Text;

public class OptionsMenu extends RootComponent {

	public OptionsMenu(float x, float y, float w, float h) {
		super(x, y, w, h, "Options");
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);

		Button godraysButton = new Button(40, -40, 200, 40, "Volumetric Light");
		Button shadowsButton = new Button(40, -100, 200, 40, "Shadows");
		Button dofButton = new Button(40, -160, 200, 40, "Depth of Field");
		Button fxaaButton = new Button(40, -220, 200, 40, "FXAA");
		Button motionBlurButton = new Button(40, -280, 200, 40, "Motion Blur");
		Button reflectionsButton = new Button(260, -40, 200, 40, "Reflections");
		Button parallaxButton = new Button(260, -100, 200, 40, "Parallax");
		Button ambientOccButton = new Button(260, -160, 200, 40, "Ambient Occlusion");
		Button chromaticAberrationButton = new Button(260, -220, 200, 40, "Chromatic Aberration");
		Button lensFlaresButton = new Button(260, -280, 200, 40, "Lens Flares");
		Text wmBorderText = new Text("Window Border: " + NRendering.BORDER_SIZE / 2f, 520, -40);
		Slider wmBorder = new Slider(520, -60, 200, 20, NRendering.BORDER_SIZE / 80f);

		godraysButton.setWindowAlignment(Alignment.LEFT_TOP);
		godraysButton.setAlignment(Alignment.RIGHT_BOTTOM);
		shadowsButton.setWindowAlignment(Alignment.LEFT_TOP);
		shadowsButton.setAlignment(Alignment.RIGHT_BOTTOM);
		dofButton.setWindowAlignment(Alignment.LEFT_TOP);
		dofButton.setAlignment(Alignment.RIGHT_BOTTOM);
		fxaaButton.setWindowAlignment(Alignment.LEFT_TOP);
		fxaaButton.setAlignment(Alignment.RIGHT_BOTTOM);
		motionBlurButton.setWindowAlignment(Alignment.LEFT_TOP);
		motionBlurButton.setAlignment(Alignment.RIGHT_BOTTOM);
		reflectionsButton.setWindowAlignment(Alignment.LEFT_TOP);
		reflectionsButton.setAlignment(Alignment.RIGHT_BOTTOM);
		parallaxButton.setWindowAlignment(Alignment.LEFT_TOP);
		parallaxButton.setAlignment(Alignment.RIGHT_BOTTOM);
		ambientOccButton.setWindowAlignment(Alignment.LEFT_TOP);
		ambientOccButton.setAlignment(Alignment.RIGHT_BOTTOM);
		chromaticAberrationButton.setWindowAlignment(Alignment.LEFT_TOP);
		chromaticAberrationButton.setAlignment(Alignment.RIGHT_BOTTOM);
		lensFlaresButton.setWindowAlignment(Alignment.LEFT_TOP);
		lensFlaresButton.setAlignment(Alignment.RIGHT_BOTTOM);
		wmBorderText.setWindowAlignment(Alignment.LEFT_TOP);
		wmBorderText.setFontSize(20);
		wmBorderText.setAlign(NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		wmBorder.setWindowAlignment(Alignment.LEFT_TOP);
		wmBorder.setAlignment(Alignment.RIGHT_BOTTOM);
		wmBorder.setPrecision(40f);
		wmBorder.useCustomPrecision(true);

		if (ClientVariables.useVolumetricLight) {
			godraysButton.setText("Volumetric Light: ON");
			godraysButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			godraysButton.setText("Volumetric Light: OFF");
			godraysButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useShadows) {
			shadowsButton.setText("Shadows: ON");
			shadowsButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			shadowsButton.setText("Shadows: OFF");
			shadowsButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useDOF) {
			dofButton.setText("Depth of Field: ON");
			dofButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			dofButton.setText("Depth of Field: OFF");
			dofButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useFXAA) {
			fxaaButton.setText("FXAA: ON");
			fxaaButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			fxaaButton.setText("FXAA: OFF");
			fxaaButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useMotionBlur) {
			motionBlurButton.setText("Motion Blur: ON");
			motionBlurButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			motionBlurButton.setText("Motion Blur: OFF");
			motionBlurButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useReflections) {
			reflectionsButton.setText("Reflections: ON");
			reflectionsButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			reflectionsButton.setText("Reflections: OFF");
			reflectionsButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useParallax) {
			parallaxButton.setText("Parallax: ON");
			parallaxButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			parallaxButton.setText("Parallax: OFF");
			parallaxButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useAmbientOcclusion) {
			ambientOccButton.setText("Ambient Occlusion: ON");
			ambientOccButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			ambientOccButton.setText("Ambient Occlusion: OFF");
			ambientOccButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useChromaticAberration) {
			chromaticAberrationButton.setText("Chromatic Aberration: ON");
			chromaticAberrationButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			chromaticAberrationButton.setText("Chromatic Aberration: OFF");
			chromaticAberrationButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if (ClientVariables.useLensFlares) {
			lensFlaresButton.setText("Lens Flares: ON");
			lensFlaresButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			lensFlaresButton.setText("Lens Flares: OFF");
			lensFlaresButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		shadowsButton.setOnButtonPress(() -> {
			ClientVariables.useShadows = !ClientVariables.useShadows;
			if (ClientVariables.useShadows) {
				shadowsButton.setText("Shadows: ON");
				shadowsButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				shadowsButton.setText("Shadows: OFF");
				shadowsButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
		});

		dofButton.setOnButtonPress(() -> {
			ClientVariables.useDOF = !ClientVariables.useDOF;
			if (ClientVariables.useDOF) {
				dofButton.setText("Depth of Field: ON");
				dofButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				dofButton.setText("Depth of Field: OFF");
				dofButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}

		});

		godraysButton.setOnButtonPress(() -> {
			ClientVariables.useVolumetricLight = !ClientVariables.useVolumetricLight;
			if (ClientVariables.useVolumetricLight) {
				godraysButton.setText("Volumetric Light: ON");
				godraysButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				godraysButton.setText("Volumetric Light: OFF");
				godraysButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
		});

		fxaaButton.setOnButtonPress(() -> {
			ClientVariables.useFXAA = !ClientVariables.useFXAA;

			if (ClientVariables.useFXAA) {
				fxaaButton.setText("FXAA: ON");
				fxaaButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				fxaaButton.setText("FXAA: OFF");
				fxaaButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
		});

		parallaxButton.setOnButtonPress(() -> {
			ClientVariables.useParallax = !ClientVariables.useParallax;
			if (ClientVariables.useParallax) {
				parallaxButton.setText("Parallax: ON");
				parallaxButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				parallaxButton.setText("Parallax: OFF");
				parallaxButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}

		});

		motionBlurButton.setOnButtonPress(() -> {
			ClientVariables.useMotionBlur = !ClientVariables.useMotionBlur;
			if (ClientVariables.useMotionBlur) {
				motionBlurButton.setText("Motion Blur: ON");
				motionBlurButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				motionBlurButton.setText("Motion Blur: OFF");
				motionBlurButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}

		});

		reflectionsButton.setOnButtonPress(() -> {
			ClientVariables.useReflections = !ClientVariables.useReflections;
			if (ClientVariables.useReflections) {
				reflectionsButton.setText("Reflections: ON");
				reflectionsButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				reflectionsButton.setText("Reflections: OFF");
				reflectionsButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
		});

		ambientOccButton.setOnButtonPress(() -> {
			ClientVariables.useAmbientOcclusion = !ClientVariables.useAmbientOcclusion;
			if (ClientVariables.useAmbientOcclusion) {
				ambientOccButton.setText("Ambient Occlusion: ON");
				ambientOccButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				ambientOccButton.setText("Ambient Occlusion: OFF");
				ambientOccButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
		});

		chromaticAberrationButton.setOnButtonPress(() -> {
			ClientVariables.useChromaticAberration = !ClientVariables.useChromaticAberration;
			if (ClientVariables.useChromaticAberration) {
				chromaticAberrationButton.setText("Chromatic Aberration: ON");
				chromaticAberrationButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				chromaticAberrationButton.setText("Chromatic Aberration: OFF");
				chromaticAberrationButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
		});

		lensFlaresButton.setOnButtonPress(() -> {
			ClientVariables.useLensFlares = !ClientVariables.useLensFlares;
			if (ClientVariables.useLensFlares) {
				lensFlaresButton.setText("Lens Flares: ON");
				lensFlaresButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				lensFlaresButton.setText("Lens Flares: OFF");
				lensFlaresButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
		});
		
		wmBorder.setOnPress(() -> {
			NRendering.BORDER_SIZE = wmBorder.getPosition() * 80f;
			wmBorderText.setText("Window Border: " + NRendering.BORDER_SIZE / 2f);
		});

		super.addComponent(shadowsButton);
		super.addComponent(dofButton);
		super.addComponent(godraysButton);
		super.addComponent(fxaaButton);
		super.addComponent(parallaxButton);
		super.addComponent(motionBlurButton);
		super.addComponent(reflectionsButton);
		super.addComponent(ambientOccButton);
		super.addComponent(chromaticAberrationButton);
		super.addComponent(lensFlaresButton);
		super.addComponent(wmBorderText);
		super.addComponent(wmBorder);

		super.initApp(window);
	}
	
	@Override
	public void onClose() {
		ClientInternalSubsystem.getInstance().getGameSettings().update();
		ClientInternalSubsystem.getInstance().getGameSettings().save();
	}

}
