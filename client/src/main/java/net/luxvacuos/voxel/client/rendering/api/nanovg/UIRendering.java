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
import static org.lwjgl.nanovg.NanoVG.nnvgText;
import static org.lwjgl.nanovg.NanoVG.nnvgTextBreakLines;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgBoxGradient;
import static org.lwjgl.nanovg.NanoVG.nvgCircle;
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
import static org.lwjgl.nanovg.NanoVG.nvgRadialGradient;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRect;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextAlign;
import static org.lwjgl.nanovg.NanoVG.nvgTextBounds;
import static org.lwjgl.nanovg.NanoVG.nvgTextMetrics;
import static org.lwjgl.system.MemoryUtil.memAddress;
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
 * GlassUI
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class UIRendering {

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

	public static void renderParagraph(long windowID, float x, float y, float width, float height, float mx, float my,
			float fontSize, String font, String text, int align, NVGColor color) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		if (text == null)
			text = "";
		ByteBuffer paragraph = memUTF8(text);

		nvgSave(vg);

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, align);
		nvgTextMetrics(vg, null, null, lineh);

		long start = memAddress(paragraph);
		long end = start + paragraph.remaining();
		int nrows;
		while ((nrows = nnvgTextBreakLines(vg, start, end, width, memAddress(rows), 3)) != 0) {
			for (int i = 0; i < nrows; i++) {
				NVGTextRow row = rows.get(i);
				nvgFillColor(vg, color);
				nnvgText(vg, x, y, row.start(), row.end());
				y += lineh.get(0);
			}
			start = rows.get(nrows - 1).next();
		}

		nvgRestore(vg);
	}

	public static void renderLabel(long windowID, String text, String font, float x, float y, float w, float h,
			float fontSize) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgFillColor(vg, rgba(255, 255, 255, 128, colorA));
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgText(vg, x, y, text);
	}

	public static void renderSearchBox(long windowID, String text, String font, String entypo, float x, float y,
			float w, float h) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		NVGPaint bg = paintA;
		float cornerRadius = h / 2 - 1;

		// Edit
		nvgBoxGradient(vg, x, y + 1.5f, w, h, h / 2, 5, rgba(0, 0, 0, 80, colorA), rgba(0, 0, 0, 120, colorB), bg);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, y, w, h, cornerRadius);
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1, cornerRadius - 0.5f);
		nvgStrokeColor(vg, rgba(0, 0, 0, 140, colorA));
		nvgStroke(vg);

		nvgFontSize(vg, h * 1.3f);
		nvgFontFace(vg, entypo);
		nvgFillColor(vg, rgba(255, 255, 255, 0, colorA));
		nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		nvgText(vg, x + h * 0.55f, y + h * 0.55f, ICON_SEARCH);

		nvgFontSize(vg, h * 1.3f);
		nvgFontFace(vg, font);
		nvgFillColor(vg, rgba(255, 255, 255, 140, colorA));

		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgText(vg, x + h * 1.05f, y + h * 0.5f, text);

		nvgFontSize(vg, h * 1.3f);
		nvgFontFace(vg, entypo);
		nvgFillColor(vg, rgba(255, 255, 255, 0, colorA));
		nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		nvgText(vg, x + w - h * 0.55f, y + h * 0.55f, ICON_CIRCLED_CROSS);
	}

	public static void renderImage(long windowID, float x, float y, float w, float h, int image, float alpha) {
		NVGPaint imgPaint = paintB;
		IntBuffer imgw = memAllocInt(1), imgh = memAllocInt(1);
		long vg = WindowManager.getWindow(windowID).getNVGID();
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

	public static void renderProgressBar(long windowID, float x, float y, float w, float h, float pos) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		nvgBoxGradient(vg, x + 1, y + 1, w - 2, h, 3, 4, rgba(32, 32, 32, 255, colorA), rgba(92, 92, 92, 255, colorB),
				paintA);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, y, w, h, 3);
		nvgFillPaint(vg, paintA);
		nvgFill(vg);

		float value = Math.min(Math.max(0.0f, pos), 1.0f);
		int barPos = (int) (Math.round(w - 2) * value);

		nvgBoxGradient(vg, x, y, barPos + 1.5f, h - 1, 3, 4, rgba(220, 220, 220, 255, colorC),
				rgba(128, 100, 128, 255, colorB), paintB);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, barPos, h - 2, 3);
		nvgFillPaint(vg, paintB);
		nvgFill(vg);
	}

	public static void renderLife(long windowID, float x, float y, float w, float h, float pos) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		nvgBoxGradient(vg, x + 1, y + 1, w - 2, h, 3, 4, rgba(32, 32, 32, 120, colorA), rgba(92, 92, 92, 255, colorB),
				paintA);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, y, w, h, 1);
		nvgFillPaint(vg, paintA);
		nvgFill(vg);

		float value = Math.min(Math.max(0.0f, pos), 1.0f);
		int barPos = (int) (Math.round(w - 2) * value);

		nvgBoxGradient(vg, x, y, barPos + 1.5f, h - 1, 3, 4, rgba(220, 0, 0, 255, colorC),
				rgba(128, 100, 128, 255, colorB), paintB);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, barPos, h - 2, 3);
		nvgFillPaint(vg, paintB);
		nvgFill(vg);
	}

	public static void renderWindow(long windowID, String title, String font, float x, float y, float w, float h,
			float alphaMult) {
		float cornerRadius = 3.0f;
		NVGPaint shadowPaint = paintA;
		NVGPaint headerPaint = paintB;
		long vg = WindowManager.getWindow(windowID).getNVGID();

		nvgSave(vg);
		// nvgClearState(vg);

		// Window
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, y, w, h, cornerRadius);
		nvgFillColor(vg, UIRendering.rgba(28, 30, 34, (int) (192 * alphaMult), colorA));
		// nvgFillColor(vg, rgba(0,0,0,128, color));
		nvgFill(vg);

		// Drop shadow
		nvgBoxGradient(vg, x, y + 2, w, h, cornerRadius * 2, 10, rgba(0, 0, 0, (int) (128 * alphaMult), colorA),
				rgba(0, 0, 0, 0, colorB), shadowPaint);
		nvgBeginPath(vg);
		nvgRect(vg, x - 10, y - 10, w + 20, h + 30);
		nvgRoundedRect(vg, x, y, w, h, cornerRadius);
		nvgPathWinding(vg, NVG_HOLE);
		nvgFillPaint(vg, shadowPaint);
		nvgFill(vg);

		// Header
		nvgLinearGradient(vg, x, y, x, y + 15, rgba(255, 255, 255, (int) (8 * alphaMult), colorA),
				rgba(0, 0, 0, (int) (16 * alphaMult), colorB), headerPaint);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, w - 2, 30, cornerRadius - 1);
		nvgFillPaint(vg, headerPaint);
		nvgFill(vg);
		nvgBeginPath(vg);
		nvgMoveTo(vg, x + 0.5f, y + 0.5f + 30);
		nvgLineTo(vg, x + 0.5f + w - 1, y + 0.5f + 30);
		nvgStrokeColor(vg, rgba(0, 0, 0, (int) (32 * alphaMult), colorA));
		nvgStroke(vg);

		nvgFontSize(vg, 18.0f);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);

		nvgFontBlur(vg, 2);
		nvgFillColor(vg, rgba(0, 0, 0, (int) (128 * alphaMult), colorA));
		nvgText(vg, x + w / 2, y + 16 + 1, title);

		nvgFontBlur(vg, 0);
		nvgFillColor(vg, rgba(220, 220, 220, (int) (160 * alphaMult), colorA));
		nvgText(vg, x + w / 2, y + 16, title);

		nvgRestore(vg);
	}

	public static void renderWindow(long windowID, float x, float y, float w, float h) {
		float cornerRadius = 3.0f;
		NVGPaint shadowPaint = paintA;
		long vg = WindowManager.getWindow(windowID).getNVGID();

		nvgSave(vg);
		// nvgClearState(vg);

		// Window
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, y, w, h, cornerRadius);
		nvgFillColor(vg, UIRendering.rgba(28, 30, 34, 192));
		// nvgFillColor(vg, rgba(0,0,0,128, color));
		nvgFill(vg);

		// Drop shadow
		nvgBoxGradient(vg, x, y + 2, w, h, cornerRadius * 2, 10, rgba(0, 0, 0, 128, colorA), rgba(0, 0, 0, 0, colorB),
				shadowPaint);
		nvgBeginPath(vg);
		nvgRect(vg, x - 10, y - 10, w + 20, h + 30);
		nvgRoundedRect(vg, x, y, w, h, cornerRadius);
		nvgPathWinding(vg, NVG_HOLE);
		nvgFillPaint(vg, shadowPaint);
		nvgFill(vg);

		nvgRestore(vg);
	}

	private static void drawEditBoxBase(long windowID, float x, float y, float w, float h, float fadeAlpha) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		NVGPaint bg = paintA;
		
		nvgBoxGradient(vg, x + 1, y + 1 + 1.5f, w - 2, h - 2, 3, 4, rgba(255, 255, 255, (int) (32 * fadeAlpha), colorA),
				rgba(32, 32, 32, (int) (32 * fadeAlpha), colorB), bg);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, w - 2, h - 2, 4 - 1);
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1, 4 - 0.5f);
		nvgStrokeColor(vg, rgba(0, 0, 0, (int) (100 * fadeAlpha), colorA));
		nvgStroke(vg);
	}

	public static void renderEditBox(long windowID, String text, String font, float x, float y, float w, float h,
			float fontSize, float fadeAlpha) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		drawEditBoxBase(windowID, x, y, w, h, fadeAlpha);
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgFillColor(vg, UIRendering.rgba(255, 255, 255, (int) (127 * fadeAlpha)));
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgText(vg, x + h * 0.3f, y + h * 0.5f, text);
	}

	public static void renderBox(long windowID, float x, float y, float w, float h, NVGColor color1, NVGColor color2,
			NVGColor color3) {
		renderBox(windowID, x, y, w, h, color1, color2, color3, 1, 4);
	}

	public static void renderBox(long windowID, float x, float y, float w, float h, NVGColor color1, NVGColor color2,
			NVGColor color3, float fadeAlpha, float round) {
		NVGPaint bg = paintA;
		long vg = WindowManager.getWindow(windowID).getNVGID();
		nvgBoxGradient(vg, x + 1, y + 1 + 1.5f, w - 2, h - 2, round - 2, 4,
				rgba((int) color1.r() * 255, (int) color1.g() * 255, (int) color1.b() * 255,
						(int) (color1.a() * fadeAlpha * 255), colorA),
				rgba((int) color2.r() * 255, (int) color2.g() * 255, (int) color2.b() * 255,
						(int) (color2.a() * fadeAlpha * 255), colorB),
				bg);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, w - 2, h - 2, round - 1);
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1, round - 0.5f);
		nvgStrokeColor(vg, rgba((int) color3.r() * 255, (int) color3.g() * 255, (int) color3.b() * 255,
				(int) (color3.a() * fadeAlpha * 255), colorC));
		nvgStroke(vg);
	}

	public static void renderSlider(long windowID, float pos, float x, float y, float w, float h) {
		NVGPaint bg = paintA, knob = paintB;
		float cy = y + (int) (h * 0.5f);
		float kr = (int) (h * 0.25f);
		long vg = WindowManager.getWindow(windowID).getNVGID();

		nvgSave(vg);
		// nvgClearState(vg);

		// Slot
		nvgBoxGradient(vg, x, cy - 2 + 1, w, 4, 2, 2, rgba(0, 0, 0, 32, colorA), rgba(0, 0, 0, 128, colorB), bg);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, cy - 2, w, 4, 2);
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		// Knob Shadow
		nvgRadialGradient(vg, x + (int) (pos * w), cy + 1, kr - 3, kr + 3, rgba(0, 0, 0, 64, colorA),
				rgba(0, 0, 0, 0, colorB), bg);
		nvgBeginPath(vg);
		nvgRect(vg, x + (int) (pos * w) - kr - 5, cy - kr - 5, kr * 2 + 5 + 5, kr * 2 + 5 + 5 + 3);
		nvgCircle(vg, x + (int) (pos * w), cy, kr);
		nvgPathWinding(vg, NVG_HOLE);
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		// Knob
		nvgLinearGradient(vg, x, cy - kr, x, cy + kr, rgba(255, 255, 255, 255, colorA),
				rgba(100, 100, 100, 255, colorB), knob);
		nvgBeginPath(vg);
		nvgCircle(vg, x + (int) (pos * w), cy, kr - 1);
		nvgFillColor(vg, rgba(40, 43, 48, 255, colorA));
		nvgFill(vg);
		nvgFillPaint(vg, knob);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgCircle(vg, x + (int) (pos * w), cy, kr - 0.5f);
		nvgStrokeColor(vg, rgba(0, 0, 0, 92, colorA));
		nvgStroke(vg);

		nvgRestore(vg);
	}

	public static void renderText(long windowID, String text, String font, float x, float y, float fontSize,
			NVGColor color, NVGColor color1) {
		renderText(windowID, text, font, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE, x, y, fontSize, color, color1);
	}

	public static void renderText(long windowID, String text, String font, int align, float x, float y, float fontSize,
			NVGColor color, NVGColor color1) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, align);
		nvgFillColor(vg, color);
		nvgText(vg, x, y, text);
	}

	public static void renderText(long windowID, String text, String font, int align, float x, float y, float fontSize,
			NVGColor color, float fadeAlpha) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, align);
		nvgFillColor(vg, rgba((int) (color.r() * 255f), (int) (color.g() * 255f), (int) (color.b() * 255f),
				(int) (color.a() * fadeAlpha * 255f), colorA));
		nvgText(vg, x, y, text);
	}

	public static void renderButton(long windowID, ByteBuffer preicon, String text, String font, String entypo, float x,
			float y, float w, float h, NVGColor color, boolean mouseInside, float fontSize, float fadeAlpha) {
		long vg = WindowManager.getWindow(windowID).getNVGID();
		NVGPaint bg = paintA;
		float cornerRadius = 0.0f;
		float tw, iw = 0;

		if (mouseInside) {
			x += 1;
			y += 1;
			w -= 2;
			h -= 2;
			fontSize -= 1f;
		}

		nvgLinearGradient(vg, x, y, x, y + h,
				rgba(255, 255, 255, (int) ((isBlack(color) ? 16 : 32) * fadeAlpha), colorB),
				rgba(0, 0, 0, (int) ((isBlack(color) ? 16 : 32) * fadeAlpha), colorC), bg);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, w - 2, h - 2, cornerRadius - 1);
		if (!isBlack(rgba((int) color.r() * 255, (int) color.g() * 255, (int) color.b() * 255,
				(int) (color.a() * fadeAlpha * 255), colorA))) {
			nvgFillColor(vg, rgba((int) color.r() * 255, (int) color.g() * 255, (int) color.b() * 255,
					(int) (color.a() * fadeAlpha * 255), colorA));
			nvgFill(vg);
		}
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1, cornerRadius - 0.5f);
		nvgStrokeColor(vg, rgba(0, 0, 0, (int) (48 * fadeAlpha), colorA));
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
			nvgFillColor(vg, rgba(100, 100, 100, (int) (96 * fadeAlpha), colorA));
			nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
			nvgText(vg, x + w * 0.5f - tw * 0.5f - iw * 0.75f, y + h * 0.5f, preicon);
		}

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, rgba(0, 0, 0, (int) (255 * fadeAlpha), colorA));
		nvgText(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f - 1, text);
		nvgFillColor(vg, rgba(255, 255, 255, (int) (100 * fadeAlpha), colorA));
		nvgText(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f, text);

	}

	public static ByteBuffer cpToUTF8(int cp) {
		return memUTF8(new String(Character.toChars(cp)), true);
	}

}
