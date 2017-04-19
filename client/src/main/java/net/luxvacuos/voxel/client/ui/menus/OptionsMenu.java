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

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.ButtonStyle;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.RootComponent;
import net.luxvacuos.voxel.client.ui.Slider;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.TitleBarButton;
import net.luxvacuos.voxel.universal.core.TaskManager;

public class OptionsMenu extends RootComponent {

	private TitleBarButton backButton;

	public OptionsMenu(float x, float y, float w, float h) {
		super(x, y, w, h, "Options");
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);

		backButton = new TitleBarButton(0, -1, 28, 28);
		backButton.setAlignment(Alignment.RIGHT_BOTTOM);
		backButton.setWindowAlignment(Alignment.LEFT_TOP);
		backButton.setColor("#646464C8");
		backButton.setStyle(ButtonStyle.LEFT_ARROW);
		backButton.setEnabled(false);
		super.getTitleBar().addComponent(backButton);

		mainMenu(window);

		super.initApp(window);
	}

	private void mainMenu(Window window) {
		Button graphics = new Button(40, -40, 200, 40, "Graphics");
		graphics.setWindowAlignment(Alignment.LEFT_TOP);
		graphics.setAlignment(Alignment.RIGHT_BOTTOM);

		graphics.setOnButtonPress(() -> {
			TaskManager.addTask(() -> {
				super.disposeApp(window);
				graphicOptions();
				backButton.setOnButtonPress(() -> {
					TaskManager.addTask(() -> {
						super.disposeApp(window);
						backButton.setEnabled(false);
						mainMenu(window);
					});
				});
			});
		});
		
		Button wm = new Button(40, -100, 200, 40, "Window Manager");
		wm.setWindowAlignment(Alignment.LEFT_TOP);
		wm.setAlignment(Alignment.RIGHT_BOTTOM);
		
		wm.setOnButtonPress(() -> {
			TaskManager.addTask(() -> {
				super.disposeApp(window);
				wmOptions();
				backButton.setOnButtonPress(() -> {
					TaskManager.addTask(() -> {
						super.disposeApp(window);
						backButton.setEnabled(false);
						mainMenu(window);
					});
				});
			});
		});
		
		super.addComponent(graphics);
		super.addComponent(wm);
	}
	
	private void graphicOptions() {
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

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/volumetricLight")) {
			godraysButton.setText("Volumetric Light: ON");
			godraysButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			godraysButton.setText("Volumetric Light: OFF");
			godraysButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadows")) {
			shadowsButton.setText("Shadows: ON");
			shadowsButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			shadowsButton.setText("Shadows: OFF");
			shadowsButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/dof")) {
			dofButton.setText("Depth of Field: ON");
			dofButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			dofButton.setText("Depth of Field: OFF");
			dofButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/fxaa")) {
			fxaaButton.setText("FXAA: ON");
			fxaaButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			fxaaButton.setText("FXAA: OFF");
			fxaaButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/motionBlur")) {
			motionBlurButton.setText("Motion Blur: ON");
			motionBlurButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			motionBlurButton.setText("Motion Blur: OFF");
			motionBlurButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/reflections")) {
			reflectionsButton.setText("Reflections: ON");
			reflectionsButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			reflectionsButton.setText("Reflections: OFF");
			reflectionsButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/parallax")) {
			parallaxButton.setText("Parallax: ON");
			parallaxButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			parallaxButton.setText("Parallax: OFF");
			parallaxButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/ambientOcclusion")) {
			ambientOccButton.setText("Ambient Occlusion: ON");
			ambientOccButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			ambientOccButton.setText("Ambient Occlusion: OFF");
			ambientOccButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/chromaticAberration")) {
			chromaticAberrationButton.setText("Chromatic Aberration: ON");
			chromaticAberrationButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			chromaticAberrationButton.setText("Chromatic Aberration: OFF");
			chromaticAberrationButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		if ((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/lensFlares")) {
			lensFlaresButton.setText("Lens Flares: ON");
			lensFlaresButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
		} else {
			lensFlaresButton.setText("Lens Flares: OFF");
			lensFlaresButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
		}

		shadowsButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadows");
			val = !val;
			if (val) {
				shadowsButton.setText("Shadows: ON");
				shadowsButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				shadowsButton.setText("Shadows: OFF");
				shadowsButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/shadows", val);
		});

		dofButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/dof");
			val = !val;
			if (val) {
				dofButton.setText("Depth of Field: ON");
				dofButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				dofButton.setText("Depth of Field: OFF");
				dofButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/dof", val);
		});

		godraysButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/volumetricLight");
			val = !val;
			if (val) {
				godraysButton.setText("Volumetric Light: ON");
				godraysButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				godraysButton.setText("Volumetric Light: OFF");
				godraysButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/volumetricLight", val);
		});

		fxaaButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/fxaa");
			val = !val;
			if (val) {
				fxaaButton.setText("FXAA: ON");
				fxaaButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				fxaaButton.setText("FXAA: OFF");
				fxaaButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/fxaa", val);
		});

		parallaxButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/parallax");
			val = !val;
			if (val) {
				parallaxButton.setText("Parallax: ON");
				parallaxButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				parallaxButton.setText("Parallax: OFF");
				parallaxButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/parallax", val);
		});

		motionBlurButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/motionBlur");
			val = !val;
			if (val) {
				motionBlurButton.setText("Motion Blur: ON");
				motionBlurButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				motionBlurButton.setText("Motion Blur: OFF");
				motionBlurButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/motionBlur", val);
		});

		reflectionsButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/reflections");
			val = !val;
			if (val) {
				reflectionsButton.setText("Reflections: ON");
				reflectionsButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				reflectionsButton.setText("Reflections: OFF");
				reflectionsButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/reflections", val);
		});

		ambientOccButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/ambientOcclusion");
			val = !val;
			if (val) {
				ambientOccButton.setText("Ambient Occlusion: ON");
				ambientOccButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				ambientOccButton.setText("Ambient Occlusion: OFF");
				ambientOccButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/ambientOcclusion", val);
		});

		chromaticAberrationButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/chromaticAberration");
			val = !val;
			if (val) {
				chromaticAberrationButton.setText("Chromatic Aberration: ON");
				chromaticAberrationButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				chromaticAberrationButton.setText("Chromatic Aberration: OFF");
				chromaticAberrationButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/chromaticAberration", val);
		});

		lensFlaresButton.setOnButtonPress(() -> {
			boolean val = (boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/lensFlares");
			val = !val;
			if (val) {
				lensFlaresButton.setText("Lens Flares: ON");
				lensFlaresButton.setColor(0.2f, 1.0f, 0.2f, 1.0f);
			} else {
				lensFlaresButton.setText("Lens Flares: OFF");
				lensFlaresButton.setColor(1.0f, 0.2f, 0.2f, 1.0f);
			}
			REGISTRY.register("/Voxel/Settings/Graphics/lensFlares", val);
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
		backButton.setEnabled(true);
	}
	
	private void wmOptions(){
		float border = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
		Text wmBorderText = new Text("Window Border: " + border, 40, -40);
		Slider wmBorder = new Slider(40, -60, 200, 20, border / 40f);
		
		wmBorderText.setWindowAlignment(Alignment.LEFT_TOP);
		wmBorderText.setFontSize(20);
		wmBorderText.setAlign(NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		wmBorder.setWindowAlignment(Alignment.LEFT_TOP);
		wmBorder.setAlignment(Alignment.RIGHT_BOTTOM);
		wmBorder.setPrecision(40f);
		wmBorder.useCustomPrecision(true);
		
		wmBorder.setOnPress(() -> {
			float val = wmBorder.getPosition() * 40f;
			REGISTRY.register("/Voxel/Settings/WindowManager/borderSize", val);
			wmBorderText.setText("Window Border: " + val);
		});
		
		super.addComponent(wmBorderText);
		super.addComponent(wmBorder);
		backButton.setEnabled(true);
	}

	@Override
	public void onClose() {
		ClientInternalSubsystem.getInstance().getGameSettings().update();
		ClientInternalSubsystem.getInstance().getGameSettings().save();
	}

}
