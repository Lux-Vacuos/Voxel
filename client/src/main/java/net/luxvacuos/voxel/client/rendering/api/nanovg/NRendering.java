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

package net.luxvacuos.voxel.client.rendering.api.nanovg;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_HOLE;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgBoxGradient;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFillPaint;
import static org.lwjgl.nanovg.NanoVG.nvgFontBlur;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgLineTo;
import static org.lwjgl.nanovg.NanoVG.nvgMoveTo;
import static org.lwjgl.nanovg.NanoVG.nvgPathWinding;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRect;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;

import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;

public class NRendering {

	private static enum ButtonStyle {
		EXIT, MAXIMIZE
	};

	public static enum BackgroundStyle {
		SOLID, TRANSPARENT
	};

	public static final NVGPaint paintA = NVGPaint.create();
	public static final NVGPaint paintB = NVGPaint.create();
	public static final NVGPaint paintC = NVGPaint.create();
	public static final NVGColor colorA = NVGColor.create();
	public static final NVGColor colorB = NVGColor.create();
	public static final NVGColor colorC = NVGColor.create();

	public static NVGColor rgba(int r, int g, int b, int a, NVGColor color) {
		color.r(r / 255.0f);
		color.g(g / 255.0f);
		color.b(b / 255.0f);
		color.a(a / 255.0f);
		return color;
	}

	public static NVGColor rgba(int r, int g, int b, int a) {
		NVGColor color = NVGColor.create();
		color.r(r / 255.0f);
		color.g(g / 255.0f);
		color.b(b / 255.0f);
		color.a(a / 255.0f);
		return color;
	}

	public static void renderWindow(long windowID, String title, String font, float x, float y, float w, float h,
			BackgroundStyle backgroundStyle, NVGColor backgroundColor, boolean decorations, boolean resizable) {
		float cornerRadius = 0.0f;
		NVGPaint shadowPaint = paintA;
		long vg = WindowManager.getWindow(windowID).getNVGID();

		nvgSave(vg);
		if (decorations) {
			// Window
			nvgBeginPath(vg);
			nvgRoundedRect(vg, x, y, w, h, cornerRadius);
			nvgRoundedRect(vg, x + 2, y + 33, w - 4, h - 35, cornerRadius);
			nvgPathWinding(vg, NVG_HOLE);
			nvgFillColor(vg, UIRendering.rgba(120, 120, 120, 255, colorA));
			nvgFill(vg);
		}

		// Background
		switch (backgroundStyle) {
		case SOLID:
			nvgBeginPath(vg);
			nvgRoundedRect(vg, x + 2, y + 33, w - 4, h - 35, cornerRadius);
			nvgFillColor(vg, backgroundColor);
			nvgFill(vg);
			break;
		case TRANSPARENT:
			break;
		}
		if (decorations) {
			// Button Close
			renderWindowButton(windowID, x + w - 31, y + 2, 29, 29, rgba(200, 0, 0, 200, colorB), ButtonStyle.EXIT);

			// Button Maximize
			if (resizable)
				renderWindowButton(windowID, x + w - 62, y + 2, 29, 29, rgba(100, 100, 100, 200, colorB),
						ButtonStyle.MAXIMIZE);

			// Drop shadow
			nvgBoxGradient(vg, x, y + 2, w, h, cornerRadius * 2, 10, rgba(0, 0, 0, 128, colorA),
					rgba(0, 0, 0, 0, colorB), shadowPaint);
			nvgBeginPath(vg);
			nvgRect(vg, x - 10, y - 10, w + 20, h + 30);
			nvgRoundedRect(vg, x, y, w, h, cornerRadius);
			nvgPathWinding(vg, NVG_HOLE);
			nvgFillPaint(vg, shadowPaint);
			nvgFill(vg);

			// Title
			nvgFontSize(vg, 18.0f);
			nvgFontFace(vg, font);
			nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);

			nvgFontBlur(vg, 2);
			nvgFillColor(vg, rgba(0, 0, 0, 128, colorA));
			nvgText(vg, x + w / 2, y + 16 + 1, title);

			nvgFontBlur(vg, 0);
			nvgFillColor(vg, rgba(220, 220, 220, 255, colorA));
			nvgText(vg, x + w / 2, y + 16, title);
		}

		nvgRestore(vg);
	}

	public static void renderWindowButton(long windowID, float x, float y, float w, float h, NVGColor color,
			ButtonStyle style) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		nvgFillColor(vg, color);
		nvgFill(vg);

		switch (style) {
		case EXIT:
			nvgBeginPath(vg);
			nvgMoveTo(vg, x + 8, y + 8);
			nvgLineTo(vg, x + w - 8, y + h - 8);
			nvgStrokeColor(vg, rgba(0, 0, 0, 200, colorA));
			nvgStroke(vg);
			nvgBeginPath(vg);
			nvgMoveTo(vg, x + 8, y + h - 8);
			nvgLineTo(vg, x + w - 8, y + 8);
			nvgStrokeColor(vg, rgba(0, 0, 0, 200, colorA));
			nvgStroke(vg);
			break;
		case MAXIMIZE:
			nvgBeginPath(vg);
			nvgMoveTo(vg, x + 8, y + 8);
			nvgLineTo(vg, x + w - 8, y + 8);
			nvgStrokeColor(vg, rgba(0, 0, 0, 200, colorA));
			nvgStroke(vg);

			nvgBeginPath(vg);
			nvgMoveTo(vg, x + w - 8, y + h - 8);
			nvgLineTo(vg, x + w - 8, y + 8);
			nvgStrokeColor(vg, rgba(0, 0, 0, 200, colorA));
			nvgStroke(vg);

			nvgBeginPath(vg);
			nvgMoveTo(vg, x + w - 8, y + h - 8);
			nvgLineTo(vg, x + 8, y + h - 8);
			nvgStrokeColor(vg, rgba(0, 0, 0, 200, colorA));
			nvgStroke(vg);

			nvgBeginPath(vg);
			nvgMoveTo(vg, x + 8, y + h - 8);
			nvgLineTo(vg, x + 8, y + 8);
			nvgStrokeColor(vg, rgba(0, 0, 0, 200, colorA));
			nvgStroke(vg);
			break;
		}

	}

}
