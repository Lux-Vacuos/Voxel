/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.nanovg.rendering;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_HOLE;
import static org.lwjgl.nanovg.NanoVG.NVG_PI;
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
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memEncodeASCII;
import static org.lwjgl.system.MemoryUtil.memEncodeUTF8;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.system.MemoryUtil.BufferAllocator;

import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.client.opengl.Display;

/**
 * This class contains basic rendering methods using NanoVG
 * 
 * @author pablo
 *
 */
public class VectorsRendering {

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

	private static Display display;

	public static void setDisplay(Display display) {
		VectorsRendering.display = display;
	}

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

	public static void renderLabel(String text, String font, float x, float y, float w, float h, float fontSize) {
		long vg = display.getVg();
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgFillColor(vg, rgba(255, 255, 255, 128, colorA));

		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgText(vg, x, y + h * 0.5f, text, NULL);
	}

	public static void renderImage(float x, float y, float w, float h, int image, float alpha) {
		NVGPaint imgPaint = paintB;
		IntBuffer imgw = memAllocInt(1), imgh = memAllocInt(1);
		long vg = display.getVg();
		nvgSave(vg);
		nvgImageSize(vg, image, imgw, imgh);
		nvgImagePattern(vg, x, y, w, h, 0.0f / 180.0f * NVG_PI, image, alpha, imgPaint);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, y, w, h, 5);
		nvgFillPaint(vg, imgPaint);
		nvgFill(vg);
		nvgRestore(vg);
		memFree(imgh);
		memFree(imgw);
	}

	public static void renderProgressBar(float x, float y, float w, float h, float pos) {
		long vg = display.getVg();
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

	public static void renderWindow(String title, String font, float x, float y, float w, float h) {
		float cornerRadius = 3.0f;
		NVGPaint shadowPaint = paintA;
		NVGPaint headerPaint = paintB;
		long vg = display.getVg();

		nvgSave(vg);
		// nvgClearState(vg);

		// Window
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, y, w, h, cornerRadius);
		nvgFillColor(vg, rgba(28, 30, 34, 192, colorA));
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

		// Header
		nvgLinearGradient(vg, x, y, x, y + 15, rgba(255, 255, 255, 8, colorA), rgba(0, 0, 0, 16, colorB), headerPaint);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, w - 2, 30, cornerRadius - 1);
		nvgFillPaint(vg, headerPaint);
		nvgFill(vg);
		nvgBeginPath(vg);
		nvgMoveTo(vg, x + 0.5f, y + 0.5f + 30);
		nvgLineTo(vg, x + 0.5f + w - 1, y + 0.5f + 30);
		nvgStrokeColor(vg, rgba(0, 0, 0, 32, colorA));
		nvgStroke(vg);

		nvgFontSize(vg, 18.0f);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);

		ByteBuffer titleText = memEncodeASCII(title, BufferAllocator.MALLOC);

		nvgFontBlur(vg, 2);
		nvgFillColor(vg, rgba(0, 0, 0, 128, colorA));
		nvgText(vg, x + w / 2, y + 16 + 1, titleText, NULL);

		nvgFontBlur(vg, 0);
		nvgFillColor(vg, rgba(220, 220, 220, 160, colorA));
		nvgText(vg, x + w / 2, y + 16, titleText, NULL);

		memFree(titleText);

		nvgRestore(vg);
	}

	public static void renderWindow(float x, float y, float w, float h) {
		float cornerRadius = 3.0f;
		NVGPaint shadowPaint = paintA;
		long vg = display.getVg();

		nvgSave(vg);
		// nvgClearState(vg);

		// Window
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x, y, w, h, cornerRadius);
		nvgFillColor(vg, rgba(28, 30, 34, 192, colorA));
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

	public static void renderMouse() {
		float x = Mouse.getX() - 8;
		float y = -Mouse.getY() - 8 + display.getDisplayHeight();
		float w = 16;
		float h = 16;
		NVGPaint bg = paintA;
		long vg = display.getVg();
		nvgBoxGradient(vg, x + 1, y + 1 + 1.5f, w - 2, h - 2, 3, 4, rgba(255, 255, 255, 200, colorA),
				rgba(32, 32, 32, 32, colorB), bg);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, w - 2, h - 2, 4 - 1);
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1, 4 - 0.5f);
		nvgStrokeColor(vg, rgba(0, 0, 0, 48, colorA));
		nvgStroke(vg);
	}

	public static void renderBox(float x, float y, float w, float h, NVGColor color1, NVGColor color2,
			NVGColor color3) {
		NVGPaint bg = paintA;
		long vg = display.getVg();
		nvgBoxGradient(vg, x + 1, y + 1 + 1.5f, w - 2, h - 2, 3, 4, color1, color2, bg);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, w - 2, h - 2, 4 - 1);
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1, 4 - 0.5f);
		nvgStrokeColor(vg, color3);
		nvgStroke(vg);
	}

	public static void renderSlider(float pos, float x, float y, float w, float h) {
		NVGPaint bg = paintA, knob = paintB;
		float cy = y + (int) (h * 0.5f);
		float kr = (int) (h * 0.25f);
		long vg = display.getVg();

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

	public static void renderText(String text, String font, float x, float y, float fontSize, NVGColor colort,
			NVGColor colorg) {
		renderText(text, font, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE, x, y, fontSize, colort, colorg);
	}

	public static void renderText(String text, String font, int align, float x, float y, float fontSize,
			NVGColor colort, NVGColor colorg) {
		long vg = display.getVg();
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, align);
		nvgFillColor(vg, colort);
		nvgText(vg, x, y, text, NULL);
		nvgFillColor(vg, colorg);
		nvgText(vg, x, y, text, NULL);
	}

	public static void renderButton(ByteBuffer preicon, String text, String font, String entypo, float x, float y,
			float w, float h, NVGColor color, boolean mouseInside) {
		long vg = display.getVg();
		NVGPaint bg = paintA;
		float cornerRadius = 10.0f;
		float tw, iw = 0;

		if (mouseInside) {
			x += 3;
			y += 3;
			w -= 6;
			h -= 6;
		}
		float fontSize = h / 2;

		nvgLinearGradient(vg, x, y, x, y + h, rgba(255, 255, 255, isBlack(color) ? 16 : 32, colorB),
				rgba(0, 0, 0, isBlack(color) ? 16 : 32, colorC), bg);
		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 1, y + 1, w - 2, h - 2, cornerRadius - 1);
		if (!isBlack(color)) {
			nvgFillColor(vg, color);
			nvgFill(vg);
		}
		nvgFillPaint(vg, bg);
		nvgFill(vg);

		nvgBeginPath(vg);
		nvgRoundedRect(vg, x + 0.5f, y + 0.5f, w - 1, h - 1, cornerRadius - 0.5f);
		nvgStrokeColor(vg, rgba(0, 0, 0, 48, colorA));
		nvgStroke(vg);

		ByteBuffer textEncoded = memEncodeASCII(text, BufferAllocator.MALLOC);

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		tw = nvgTextBounds(vg, 0, 0, textEncoded, NULL, (ByteBuffer) null);
		if (preicon != null) {
			nvgFontSize(vg, h * 1.3f);
			nvgFontFace(vg, entypo);
			iw = nvgTextBounds(vg, 0, 0, preicon, NULL, (ByteBuffer) null);
			iw += h * 0.15f;
		}

		if (preicon != null) {
			nvgFontSize(vg, h * 1.3f);
			nvgFontFace(vg, entypo);
			nvgFillColor(vg, rgba(100, 100, 100, 96, colorA));
			nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
			nvgText(vg, x + w * 0.5f - tw * 0.5f - iw * 0.75f, y + h * 0.5f, preicon, NULL);
		}

		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, rgba(0, 0, 0, 160, colorA));
		nvgText(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f - 1, textEncoded, NULL);
		nvgFillColor(vg, rgba(255, 255, 255, 160, colorA));
		nvgText(vg, x + w * 0.5f - tw * 0.5f + iw * 0.25f, y + h * 0.5f, textEncoded, NULL);

		memFree(textEncoded);
	}

	public static ByteBuffer cpToUTF8(int cp) {
		return memEncodeUTF8(new String(Character.toChars(cp)), true);
	}

}
