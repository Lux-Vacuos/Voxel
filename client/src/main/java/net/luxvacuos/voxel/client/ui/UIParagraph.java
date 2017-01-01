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

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;

public class UIParagraph extends UIComponent {

	private String text, font = "Roboto-Regular";
	private int align = NVG_ALIGN_LEFT | NVG_ALIGN_TOP;
	private int fontSize = 25;
	private NVGColor color = UIRendering.rgba(255, 255, 255, 255, NVGColor.create());
	private float mx, my;

	public UIParagraph(String text, float x, float y, float w, float h, float mx, float my) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.mx = mx;
		this.my = my;
	}

	@Override
	public void render(long windowID) {
		UIRendering.renderParagraph(windowID, rootX + x, WindowManager.getWindow(windowID).getHeight() - rootY - y,
				width, height, mx, my, fontSize, font, text, align, color);
		super.render(windowID);
	}

	public void setAlign(int align) {
		this.align = align;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setColor(int r, int g, int b, int a) {
		UIRendering.rgba(r, g, b, a, color);
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}
