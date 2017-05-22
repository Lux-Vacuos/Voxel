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

package net.luxvacuos.voxel.client.rendering.api.nanovg.themes;

import static org.lwjgl.nanovg.NanoVGGL3.nvglCreateImageFromHandle;
import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.ScrollPaneElement;

public class Theme {

	private static ITheme theme;

	public static void setTheme(ITheme theme) {
		Theme.theme = theme;
	}

	public static final ByteBuffer ICON_SEARCH = cpToUTF8(0x1F50D);
	public static final ByteBuffer ICON_CIRCLED_CROSS = cpToUTF8(0x2716);
	public static final ByteBuffer ICON_CHEVRON_RIGHT = cpToUTF8(0xE75E);
	public static final ByteBuffer ICON_CHECK = cpToUTF8(0x2713);
	public static final ByteBuffer ICON_LOGIN = cpToUTF8(0xE740);
	public static final ByteBuffer ICON_TRASH = cpToUTF8(0xE729);
	public static final ByteBuffer ICON_INFORMATION_SOURCE = cpToUTF8(0x2139);
	public static final ByteBuffer ICON_GEAR = cpToUTF8(0x2699);
	public static final ByteBuffer ICON_BLACK_RIGHT_POINTING_TRIANGLE = cpToUTF8(0x25B6);

	public static final NVGPaint paintA = NVGPaint.create();
	public static final NVGPaint paintB = NVGPaint.create();
	public static final NVGPaint paintC = NVGPaint.create();
	public static final NVGColor colorA = NVGColor.create();
	public static final NVGColor colorB = NVGColor.create();
	public static final NVGColor colorC = NVGColor.create();

	public enum ButtonStyle {
		CLOSE, MAXIMIZE, MINIMIZE, NONE, LEFT_ARROW, RIGHT_ARROW
	};

	public enum BackgroundStyle {
		SOLID, TRANSPARENT
	};

	public static ByteBuffer cpToUTF8(int cp) {
		return memUTF8(new String(Character.toChars(cp)), true);
	}

	public static int generateImageFromTexture(long vg, int texID, int w, int h, int flags) {
		return nvglCreateImageFromHandle(vg, texID, w, h, flags);
	}

	public static NVGColor rgba(int r, int g, int b, int a, NVGColor color) {
		return setColor(r / 255f, g / 255f, b / 255f, a / 255f, color);
	}

	public static NVGColor rgba(int r, int g, int b, int a) {
		return setColor(r / 255f, g / 255f, b / 255f, a / 255f);
	}

	public static NVGColor setColor(float r, float g, float b, float a, NVGColor color) {
		color.r(r);
		color.g(g);
		color.b(b);
		color.a(a);
		return color;
	}

	public static NVGColor setColor(float r, float g, float b, float a) {
		return setColor(r, g, b, a, NVGColor.create());
	}

	public static NVGColor setColor(String hex, NVGColor color) {
		color.r(Integer.valueOf(hex.substring(1, 3), 16) / 255f);
		color.g(Integer.valueOf(hex.substring(3, 5), 16) / 255f);
		color.b(Integer.valueOf(hex.substring(5, 7), 16) / 255f);
		color.a(Integer.valueOf(hex.substring(7, 9), 16) / 255f);
		return color;
	}

	public static NVGColor setColor(String hex) {
		return setColor(hex, NVGColor.create());
	}

	public static void renderWindow(long vg, float x, float y, float w, float h, BackgroundStyle backgroundStyle,
			NVGColor backgroundColor, boolean decorations, boolean titleBar, boolean maximized) {
		theme.renderWindow(vg, x, y, w, h, backgroundStyle, backgroundColor, decorations, titleBar, maximized);

	}

	public static void renderTitleBarText(long vg, String text, String font, int align, float x, float y,
			float fontSize) {
		theme.renderTitleBarText(vg, text, font, align, x, y, fontSize);

	}

	public static void renderTitleBarButton(long vg, float x, float y, float w, float h, ButtonStyle style,
			boolean highlight) {
		theme.renderTitleBarButton(vg, x, y, w, h, style, highlight);

	}

	public static void renderText(long vg, String text, String font, int align, float x, float y, float fontSize,
			NVGColor color) {
		theme.renderText(vg, text, font, align, x, y, fontSize, color);

	}

	public static void renderImage(long vg, float x, float y, float w, float h, int image, float alpha) {
		theme.renderImage(vg, x, y, w, h, image, alpha);
	}

	public static void renderImage(long vg, float x, float y, int image, float alpha) {
		theme.renderImage(vg, x, y, image, alpha);
	}

	public static void renderEditBoxBase(long vg, float x, float y, float w, float h, boolean selected) {
		theme.renderEditBoxBase(vg, x, y, w, h, selected);
	}

	public static void renderEditBox(long vg, String text, String font, float x, float y, float w, float h,
			float fontSize, boolean selected) {
		theme.renderEditBox(vg, text, font, x, y, w, h, fontSize, selected);
	}

	public static void renderButton(long vg, ByteBuffer preicon, String text, String font, String entypo, float x,
			float y, float w, float h, boolean highlight, float fontSize) {
		theme.renderButton(vg, preicon, text, font, entypo, x, y, w, h, highlight, fontSize);
	}

	public static void renderContexMenuButton(long vg, String text, String font, float x, float y, float w, float h,
			float fontSize, boolean highlight) {
		theme.renderContexMenuButton(vg, text, font, x, y, w, h, fontSize, highlight);
	}

	public static void renderToggleButton(long vg, String text, String font, float x, float y, float w, float h,
			float fontSize, boolean status) {
		theme.renderToggleButton(vg, text, font, x, y, w, h, fontSize, status);
	}

	public static void renderScrollPane(long vg, float x, float y, float w, float h, float t, int hSize, float cardW,
			float cardH, List<ScrollPaneElement> elements, Window window) {
		theme.renderScrollPane(vg, x, y, w, h, t, hSize, cardW, cardH, elements, window);
	}

	public static void renderSpinner(long vg, float cx, float cy, float r, float t) {
		theme.renderSpinner(vg, cx, cy, r, t);
	}

	public static void renderParagraph(long vg, float x, float y, float width, float fontSize, String font, String text,
			int align, NVGColor color) {
		theme.renderParagraph(vg, x, y, width, fontSize, font, text, align, color);
	}

	public static void renderBox(long vg, float x, float y, float w, float h, NVGColor color) {
		theme.renderBox(vg, x, y, w, h, color);
	}

	public static void renderSlider(long vg, float pos, float x, float y, float w, float h) {
		theme.renderSlider(vg, pos, x, y, w, h);
	}

	public static void renderScrollBarV(long vg, float x, float y, float w, float h, float pos, float sizeV) {
		theme.renderScrollBarV(vg, x, y, w, h, pos, sizeV);
	}

}
