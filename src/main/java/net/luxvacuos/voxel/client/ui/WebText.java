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

public class WebText {

	private String text;
	private float fontSize;
	private boolean title;

	public WebText(String text, float fontSize, boolean title) {
		this.text = text;
		this.fontSize = fontSize;
		this.title = title;
	}

	public boolean isTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public float getFontSize() {
		return fontSize;
	}

}
