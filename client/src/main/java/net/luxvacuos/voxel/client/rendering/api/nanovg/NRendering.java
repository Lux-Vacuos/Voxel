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
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_HOLE;
import static org.lwjgl.nanovg.NanoVG.NVG_PI;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgBoxGradient;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFillPaint;
import static org.lwjgl.nanovg.NanoVG.nvgFontBlur;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgImagePattern;
import static org.lwjgl.nanovg.NanoVG.nvgImageSize;
import static org.lwjgl.nanovg.NanoVG.nvgLineTo;
import static org.lwjgl.nanovg.NanoVG.nvgLinearGradient;
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
import static org.lwjgl.nanovg.NanoVG.nvgTextBounds;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NVGTextRow;

import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;

/**
 *
 * Nano UI
 *
 */
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

	public static final ByteBuffer ICON_SEARCH = cpToUTF8(0x1F50D);
	public static final ByteBuffer ICON_CIRCLED_CROSS = cpToUTF8(0x2716);
	public static final ByteBuffer ICON_CHEVRON_RIGHT = cpToUTF8(0xE75E);
	public static final ByteBuffer ICON_CHECK = cpToUTF8(0x2713);
	public static final ByteBuffer ICON_LOGIN = cpToUTF8(0xE740);
	public static final ByteBuffer ICON_TRASH = cpToUTF8(0xE729);
	public static final ByteBuffer ICON_INFORMATION_SOURCE = cpToUTF8(0x2139);
	public static final ByteBuffer ICON_GEAR = cpToUTF8(0x2699);
	public static final ByteBuffer ICON_BLACK_RIGHT_POINTING_TRIANGLE = cpToUTF8(0x25B6);

	private static final FloatBuffer lineh = BufferUtils.createFloatBuffer(1);
	private static final NVGTextRow.Buffer rows = NVGTextRow.create(3);

	private static boolean isBlack(NVGColor col) {
		return col.r() == 0.0f && col.g() == 0.0f && col.b() == 0.0f && col.a() == 0.0f;
	}

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

	public static void renderWindow(long vg, String title, String font, float x, float y, float w, float h,
			BackgroundStyle backgroundStyle, NVGColor backgroundColor, boolean decorations, boolean resizable) {
		float cornerRadius = 0.0f;
		NVGPaint shadowPaint = paintA;

		nvgSave(vg);
		if (decorations) {
			// Window
			nvgBeginPath(vg);
			nvgRoundedRect(vg, x, y, w, h, cornerRadius);
			nvgRoundedRect(vg, x + 2, y + 33, w - 4, h - 35, cornerRadius);
			nvgPathWinding(vg, NVG_HOLE);
			nvgFillColor(vg, rgba(120, 120, 120, 255, colorA));
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
			renderWindowButton(vg, x + w - 31, y + 2, 29, 29, rgba(200, 0, 0, 200, colorB), ButtonStyle.EXIT);

			// Button Maximize
			if (resizable)
				renderWindowButton(vg, x + w - 62, y + 2, 29, 29, rgba(100, 100, 100, 200, colorB),
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

	public static void renderWindowButton(long vg, float x, float y, float w, float h, NVGColor color,
			ButtonStyle style) {
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

	public static void renderText(long vg, String text, String font, int align, float x, float y, float fontSize,
			NVGColor color) {
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, align);
		nvgFillColor(vg, color);
		nvgText(vg, x, y, text);
	}

	public static void renderImage(long vg, float x, float y, float w, float h, int image, float alpha) {
		NVGPaint imgPaint = paintB;
		IntBuffer imgw = memAllocInt(1), imgh = memAllocInt(1);
		nvgSave(vg);
		nvgImageSize(vg, image, imgw, imgh);
		nvgImagePattern(vg, x, y, w, h, 0.0f / 180.0f * NVG_PI, image, alpha, imgPaint);
		nvgBeginPath(vg);
		nvgRect(vg, x, y, w, h);
		nvgFillPaint(vg, imgPaint);
		nvgFill(vg);
		nvgRestore(vg);
		memFree(imgh);
		memFree(imgw);
	}

	public static void drawEditBoxBase(long vg, float x, float y, float w, float h) {
		NVGPaint bg = paintA;
		nvgBoxGradient(vg, x + 1, y + 1 + 1.5f, w - 2, h - 2, 3, 4, rgba(255, 255, 255, 255, colorA),
				rgba(32, 32, 32, 100, colorB), bg);
		nvgBeginPath(vg);
		nvgRect(vg, x + 1, y + 1, w - 2, h - 2);
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1);
		nvgStrokeColor(vg, rgba(0, 0, 0, 100, colorA));
		nvgStroke(vg);
	}

	public static void renderEditBox(long vg, String text, String font, float x, float y, float w, float h,
			float fontSize) {
		drawEditBoxBase(vg, x, y, w, h);
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, rgba(0, 0, 0, 255, colorA));
		nvgText(vg, x + h * 0.3f, y + h * 0.5f, text);
		nvgFillColor(vg, rgba(255, 255, 255, 100, colorA));
		nvgText(vg, x + h * 0.3f, y + h * 0.5f, text);
	}

	public static void renderButton(long vg, ByteBuffer preicon, String text, String font, String entypo, float x,
			float y, float w, float h, NVGColor color, boolean mouseInside, float fontSize) {
		NVGPaint bg = paintA;
		float tw, iw = 0;

		if (mouseInside) {
			x += 1;
			y += 1;
			w -= 2;
			h -= 2;
			fontSize -= 1f;
		}

		nvgLinearGradient(vg, x, y, x, y + h, rgba(255, 255, 255, (isBlack(color) ? 16 : 32), colorB),
				rgba(0, 0, 0, (isBlack(color) ? 16 : 32), colorC), bg);
		nvgBeginPath(vg);
		nvgRect(vg, x + 1, y + 1, w - 2, h - 2);
		if (!isBlack(color)) {
			nvgFillColor(vg, color);
			nvgFill(vg);
		}
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1);
		nvgStrokeColor(vg, rgba(0, 0, 0, 100, colorA));
		nvgStroke(vg);

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		tw = nvgTextBounds(vg, 0, 0, text, (FloatBuffer) null);
		if (preicon != null) {
			nvgFontSize(vg, h * 1.3f);
			nvgFontFace(vg, entypo);
			iw = nvgTextBounds(vg, 0, 0, preicon, (FloatBuffer) null);
			iw += h * 0.15f;
		}

		if (preicon != null) {
			nvgFontSize(vg, h * 1.3f);
			nvgFontFace(vg, entypo);
			nvgFillColor(vg, rgba(100, 100, 100, 96, colorA));
			nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
			nvgText(vg, x + w * 0.5f - tw * 0.5f - iw * 0.75f, y + h * 0.5f, preicon);
		}

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, rgba(0, 0, 0, 255, colorA));
		nvgText(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f - 1, text);
		nvgFillColor(vg, rgba(255, 255, 255, 100, colorA));
		nvgText(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f, text);

	}

	public static ByteBuffer cpToUTF8(int cp) {
		return memUTF8(new String(Character.toChars(cp)), true);
	}

}
