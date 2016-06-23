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

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class Text extends Component {

	private String text, font = "Roboto-Regular";
	private int align = NVG_ALIGN_LEFT | NVG_ALIGN_MIDDLE;
	private int fontSize = 25;
	private NVGColor color = UIRendering.rgba(255, 255, 255, 255, NVGColor.create());

	public Text(String text, float x, float y) {
		this.text = text;
		this.x = x;
		this.y = y;
	}

	@Override
	public void render() {
		UIRendering.renderText(text, font, align, rootX + x,
				GameResources.getInstance().getDisplay().getDisplayHeight() - rootY - y, fontSize, color, fadeAlpha);
		super.render();
	}

	public void setAlign(int align) {
		this.align = align;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setColor(int r, int g, int b, int a) {
		UIRendering.rgba(r, g, b, a, color);
	}
}
