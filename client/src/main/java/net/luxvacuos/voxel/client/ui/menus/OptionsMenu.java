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

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.ui.nextui.Alignment;
import net.luxvacuos.voxel.client.ui.nextui.Button;
import net.luxvacuos.voxel.client.ui.nextui.RootComponent;

public class OptionsMenu extends RootComponent {

	public OptionsMenu(float x, float y, float w, float h) {
		super(x, y, w, h, "Options");
	}

	@Override
	public void initApp() {
		super.setAlwaysOnTop(true);
		Button backButton = new Button(0, 40, 200, 40, "Close");
		backButton.setAlignment(Alignment.CENTER);
		backButton.setWindowAlignment(Alignment.BOTTOM);
		backButton.setOnButtonPress((button, delta) -> {
			ClientInternalSubsystem.getInstance().getGameSettings().update();
			ClientInternalSubsystem.getInstance().getGameSettings().save();
			super.closeWindow();
		});

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

		if (ClientVariables.useChromaticAberration) {
			chromaticAberrationButton.setText("Chromatic Aberration: ON");
			chromaticAberrationButton.setColor(100, 255, 100, 255);
		} else {
			chromaticAberrationButton.setText("Chromatic Aberration: OFF");
			chromaticAberrationButton.setColor(255, 100, 100, 255);
		}

		if (ClientVariables.useLensFlares) {
			lensFlaresButton.setText("Lens Flares: ON");
			lensFlaresButton.setColor(100, 255, 100, 255);
		} else {
			lensFlaresButton.setText("Lens Flares: OFF");
			lensFlaresButton.setColor(255, 100, 100, 255);
		}

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

		chromaticAberrationButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useChromaticAberration = !ClientVariables.useChromaticAberration;
			if (ClientVariables.useChromaticAberration) {
				chromaticAberrationButton.setText("Chromatic Aberration: ON");
				chromaticAberrationButton.setColor(100, 255, 100, 255);
			} else {
				chromaticAberrationButton.setText("Chromatic Aberration: OFF");
				chromaticAberrationButton.setColor(255, 100, 100, 255);
			}
		});

		lensFlaresButton.setOnButtonPress((button, delta) -> {
			ClientVariables.useLensFlares = !ClientVariables.useLensFlares;
			if (ClientVariables.useLensFlares) {
				lensFlaresButton.setText("Lens Flares: ON");
				lensFlaresButton.setColor(100, 255, 100, 255);
			} else {
				lensFlaresButton.setText("Lens Flares: OFF");
				lensFlaresButton.setColor(255, 100, 100, 255);
			}
		});

		super.addComponent(backButton);
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

		super.initApp();
	}

}
