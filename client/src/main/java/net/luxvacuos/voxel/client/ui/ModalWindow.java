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

package net.luxvacuos.voxel.client.ui;

import static net.luxvacuos.voxel.universal.core.subsystems.CoreSubsystem.REGISTRY;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.universal.util.registry.Key;

public class ModalWindow extends RootComponentWindow {

	private float mwW, mwH;
	private String text, title;
	private Button accept, cancel;

	public ModalWindow(float w, float h, String text, String title) {
		super(0, (int) REGISTRY.getRegistryItem(new Key("/Voxel/Display/height")),
				(int) REGISTRY.getRegistryItem(new Key("/Voxel/Display/width")),
				(int) REGISTRY.getRegistryItem(new Key("/Voxel/Display/height")), "");
		this.mwW = w;
		this.mwH = h;
		this.text = text;
		this.title = title;
	}

	@Override
	public void initApp(Window window) {
		super.setDecorations(false);
		super.setAlwaysOnTop(true);
		super.toggleTitleBar();
		super.setBackgroundColor(0f, 0f, 0f, 0.4f);

		Box box = new Box(0, 0, mwW, mwH);
		box.setAlignment(Alignment.CENTER);
		box.setWindowAlignment(Alignment.CENTER);
		box.setColor(0.4f, 0.4f, 0.4f, 1f);

		TextArea text = new TextArea(this.text, -mwW / 2f + 20f, mwH / 2f - 40f, mwW - 10f);
		text.setWindowAlignment(Alignment.CENTER);

		Text title = new Text(this.title, -mwW / 2f + 20f, mwH / 2f - 15f);
		title.setFont("Poppins-Medium");
		title.setFontSize(30f);
		title.setAlign(NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		title.setWindowAlignment(Alignment.CENTER);

		accept = new Button(-10, -mwH / 2f + 20, 100, 30, "Accept");
		accept.setAlignment(Alignment.LEFT_TOP);
		accept.setWindowAlignment(Alignment.CENTER);

		cancel = new Button(10, -mwH / 2f + 20, 100, 30, "Cancel");
		cancel.setAlignment(Alignment.RIGHT_TOP);
		cancel.setWindowAlignment(Alignment.CENTER);
		cancel.setOnButtonPress(() -> {
			super.closeWindow();
		});

		super.addComponent(box);
		super.addComponent(text);
		super.addComponent(title);
		super.addComponent(accept);
		super.addComponent(cancel);

		super.initApp(window);
	}

	public void setOnAccept(OnAction action) {
		accept.setOnButtonPress(action);
	}

	public void setOnCancel(OnAction action) {
		cancel.setOnButtonPress(action);
	}

}
