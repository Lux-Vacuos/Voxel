/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
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

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;

public class Button extends Component {

	private String text = "missigno", font = "Poppins-Medium", entypo = "Entypo";
	private NVGColor color = UIRendering.rgba(255, 255, 255, 255);
	private ByteBuffer preicon;
	private OnAction onPress;
	private float fontSize = 21;

	public Button(float x, float y, float width, float height, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}

	public boolean insideButton() {
		if (Mouse.getX() > rootX + x && Mouse.getY() > rootY + y && Mouse.getX() < rootX + x + width
				&& Mouse.getY() < rootY + y + height)
			return true;
		else
			return false;
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

	@Override
	public void render(long windowID) {
		if (enabled)
			UIRendering.renderButton(windowID, preicon, text, font, entypo, rootX + x,
					WindowManager.getWindow(windowID).getHeight() - rootY - y - height, width, height, color,
					this.insideButton(), fontSize, fadeAlpha);
		super.render(windowID);
	}

	@Override
	public void update(float delta) {
		if (enabled)
			if (pressed() && onPress != null)
				onPress.onAction(this, delta);
		super.update(delta);
	}

	public void setColor(int r, int g, int b, int a) {
		UIRendering.rgba(r, g, b, a, color);
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setOnButtonPress(OnAction onPress) {
		this.onPress = onPress;
	}

	public void setEntypo(String entypo) {
		this.entypo = entypo;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public void setPreicon(ByteBuffer preicon) {
		this.preicon = preicon;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

}
