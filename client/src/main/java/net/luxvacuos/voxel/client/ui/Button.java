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

import java.nio.ByteBuffer;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.themes.Theme;

public class Button extends Component {

	protected String text = "missigno", font = "Poppins-Medium", entypo = "Entypo";
	protected ByteBuffer preicon;
	protected OnAction onPress;
	protected float fontSize = 21;
	protected boolean pressed = false, enabled = true, inside;

	public Button(float x, float y, float w, float h, String text) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
	}

	@Override
	public void render(Window window) {
		if (!enabled)
			return;
		Theme.renderButton(window.getNVGID(), preicon, text, font, entypo, rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h, inside, fontSize);
	}

	@Override
	public void update(float delta, Window window) {
		if (!enabled)
			return;
		if (onPress != null)
			if (pressed() && !pressed)
				onPress.onAction();
		pressed = pressed();
		inside = insideButton();
		super.update(delta, window);
	}

	public boolean insideButton() {
		return Mouse.getX() > rootComponent.rootX + alignedX && Mouse.getY() > rootComponent.rootY + alignedY
				&& Mouse.getX() < rootComponent.rootX + alignedX + w
				&& Mouse.getY() <= rootComponent.rootY + alignedY + h;
	}

	public boolean pressed() {
		if (insideButton())
			return Mouse.isButtonDown(0);
		else
			return false;
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

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
