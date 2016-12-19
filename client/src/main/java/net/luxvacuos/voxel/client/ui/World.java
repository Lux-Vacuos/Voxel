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

import org.lwjgl.nanovg.NanoVG;

public class World extends UIComponent {

	private String name;
	private UIWindow info;
	private UIPanel uIPanel;
	private boolean selected = false;

	public World(float x, float y, float w, float h, String name) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		uIPanel = new UIPanel(0, 0, w, h);
		uIPanel.setBorderColor(0, 0, 0, 255);
		uIPanel.setFillColor(255, 255, 255, 100);
		addChildren(uIPanel);

		UIText uIText = new UIText(name, 10, h / 2);
		uIText.setColor(80, 80, 80, 255);
		addChildren(uIText);

		UIButton btn = new UIButton(w - 60, 2, 58, h - 4, "Info");
		btn.setOnButtonPress((button, delta) -> {
			info.setEnabled(!info.isEnabled());
		});
		addChildren(btn);

		info = new UIWindow(w + 10, h, 300, 200, "World " + name + " info");
		info.setEnabled(false);
		UIText infoText = new UIText("WORK IN PROGRESS", 150, -100);
		infoText.setAlign(NanoVG.NVG_ALIGN_CENTER);
		info.addChildren(infoText);

		addChildren(info);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isPressed() {
		return uIPanel.pressed();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if (selected)
			uIPanel.setFillColor(255, 255, 255, 255);
		else
			uIPanel.setFillColor(255, 255, 255, 100);
		this.selected = selected;
	}

}
