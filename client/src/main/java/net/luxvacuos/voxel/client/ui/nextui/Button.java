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

package net.luxvacuos.voxel.client.ui.nextui;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.ui.OnAction;

public class Button extends Component {

	private String text = "missigno", font = "Poppins-Medium", entypo = "Entypo";
	private NVGColor color = UIRendering.rgba(255, 255, 255, 255);
	private ByteBuffer preicon;
	private OnAction onPress;
	private float fontSize = 21;
	private boolean pressed = false;

	public Button(float x, float y, float w, float h, String text) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
	}

	@Override
	public void render(long windowID) {
		UIRendering.renderButton(windowID, preicon, text, font, entypo, rootComponent.rootX + alignedX,
				WindowManager.getWindow(windowID).getHeight() - rootComponent.rootY - alignedY - h, w, h, color,
				this.insideButton(), fontSize, 1);
		super.render(windowID);
	}

	@Override
	public void update(float delta) {
		if (onPress != null)
			if (pressed() && !pressed)
				onPress.onAction(null, delta);
		pressed = pressed();
		super.update(delta);
	}

	public boolean insideButton() {
		return Mouse.getX() > rootComponent.rootX + alignedX && Mouse.getY() > rootComponent.rootY + alignedY
				&& Mouse.getX() < rootComponent.rootX + alignedX + w
				&& Mouse.getY() < rootComponent.rootY + alignedY + h;
	}

	public boolean pressed() {
		if (insideButton())
			return Mouse.isButtonDown(0);
		else
			return false;
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
