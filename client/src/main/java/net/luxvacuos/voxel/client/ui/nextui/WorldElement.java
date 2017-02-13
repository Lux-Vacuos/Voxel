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

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.menus.WorldMenu;

public class WorldElement extends ScrollPaneElement {

	private boolean pressed = false;
	private String worldName;
	private WorldMenu worldMenu;

	public WorldElement(float w, float h, String name, WorldMenu worldMenu) {
		super(0, 0, w, h);
		Text n = new Text(name, 0, 0);
		n.setWindowAlignment(Alignment.LEFT);
		addComponent(n);
		worldName = name;
		this.worldMenu = worldMenu;
	}

	@Override
	public void update(float delta, Window window) {
		super.update(delta, window);

		if (pressed() && !pressed) {
			ClientVariables.worldNameToLoad = new String(worldName);
			worldMenu.worldName.setText("Name: " + worldName);
		}
		pressed = pressed();
	}
	
	@Override
	public void dispose() {
		ClientVariables.worldNameToLoad = "";
		super.dispose();
	}

	public boolean insideButton() {
		return Mouse.getX() > root.rootX && Mouse.getY() > root.rootY && Mouse.getX() < root.rootX + root.rootW
				&& Mouse.getY() < root.rootY + root.rootH;
	}

	public boolean pressed() {
		if (insideButton())
			return Mouse.isButtonDown(0);
		else
			return false;
	}

}
