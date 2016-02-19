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

package net.guerra24.voxel.client.menu;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;

import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.client.nanovg.rendering.VectorsRendering;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class Button {
	private Vector2f pos, renderPos;
	private Vector2f size, renderSize;
	private float xScale, yScale;

	public Button(Vector2f pos, Vector2f size, float xScale, float yScale) {
		this.pos = new Vector2f(pos.x * xScale, pos.y * yScale);
		this.size = new Vector2f((pos.x + size.x) * xScale, (pos.y + size.y) * yScale);
		renderPos = pos;
		renderSize = size;
		this.xScale = xScale;
		this.yScale = yScale;
	}

	public boolean insideButton() {
		if (Mouse.getX() > pos.getX() && Mouse.getY() > pos.getY() && Mouse.getX() < size.getX()
				&& Mouse.getY() < size.getY())
			return true;
		else
			return false;
	}

	public void render(String text) {
		this.render(text, "Roboto-Regular", "Entypo", null,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
	}

	public void render(String text, ByteBuffer preicon) {
		this.render(text, "Roboto-Regular", "Entypo", preicon,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
	}

	public void render(String text, NVGColor color) {
		this.render(text, "Roboto-Regular", "Entypo", null, color);
	}

	public void render(String text, ByteBuffer preicon, NVGColor color) {
		this.render(text, "Roboto-Regular", "Entypo", preicon, color);
	}

	public void render(String text, String font, String entypo, ByteBuffer preicon, NVGColor color) {
		VectorsRendering.renderButton(preicon, text, font, entypo, renderPos.x * xScale,
				(720f - renderPos.y - renderSize.y) * yScale, renderSize.x * xScale, renderSize.y * yScale, color,
				this.insideButton());
	}

	public boolean pressed() {
		if (insideButton())
			if (Mouse.isButtonDown(0))
				return true;
			else
				return false;
		else
			return false;
	}
}
