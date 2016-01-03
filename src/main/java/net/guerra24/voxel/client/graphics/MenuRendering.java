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

package net.guerra24.voxel.client.graphics;

import static org.lwjgl.nanovg.NanoVG.*;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;

import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.input.Mouse;

import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

public class MenuRendering {

	public static final NVGPaint paintA = NVGPaint.create();
	public static final NVGPaint paintB = NVGPaint.create();
	public static final NVGPaint paintC = NVGPaint.create();
	public static final NVGColor colorA = NVGColor.create();
	public static final NVGColor colorB = NVGColor.create();
	public static final NVGColor colorC = NVGColor.create();

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
		long vg = Display.getVg();
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgFillColor(vg, rgba(255, 255, 255, 128, colorA));

		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgText(vg, x, y + h * 0.5f, text, NULL);
	}

	public static void renderWindow(String title, String font, float x, float y, float w, float h) {
		float cornerRadius = 3.0f;
		NVGPaint shadowPaint = paintA;
		NVGPaint headerPaint = paintB;
		long vg = Display.getVg();

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

	public static void renderMouse() {
		float x = Mouse.getX() - 8;
		float y = -Mouse.getY() - 8 + Display.getHeight();
		float w = 16;
		float h = 16;
		NVGPaint bg = paintA;
		long vg = Display.getVg();
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

	public static void renderSlider(float pos, float x, float y, float w, float h) {
		NVGPaint bg = paintA, knob = paintB;
		float cy = y + (int) (h * 0.5f);
		float kr = (int) (h * 0.25f);
		long vg = Display.getVg();

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

	public static void renderText(String text, String font, float x, float y, float fontSize) {
		long vg = Display.getVg();
		nvgFontSize(vg, fontSize);
		nvgFontFace(vg, font);
		nvgTextAlign(vg, NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE);
		nvgFillColor(vg, rgba(0, 0, 0, 160, colorA));
		nvgText(vg, x, y, text, NULL);
		nvgFillColor(vg, rgba(255, 255, 255, 160, colorA));
		nvgText(vg, x, y, text, NULL);
	}

	public static void renderButton(ByteBuffer preicon, String text, String font, float x, float y, float w, float h,
			NVGColor color, boolean mouseInside) {
		long vg = Display.getVg();
		NVGPaint bg = paintA;
		float cornerRadius = 4.0f;
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
			nvgFontFace(vg, "icons");
			iw = nvgTextBounds(vg, 0, 0, preicon, NULL, (ByteBuffer) null);
			iw += h * 0.15f;
		}

		if (preicon != null) {
			nvgFontSize(vg, h * 1.3f);
			nvgFontFace(vg, "icons");
			nvgFillColor(vg, rgba(255, 255, 255, 96, colorA));
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

}
