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

package net.luxvacuos.voxel.launcher.core;

import net.luxvacuos.adus.core.VersionKey;

public class Profile {

	private String name;
	private VersionKey version;
	private String vmArgs = "";
	private String userArgs = "";
	private int width = 1280, height = 720;
	private boolean active = false;

	public Profile(String name) {
		this.name = name;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserArgs(String userArgs) {
		this.userArgs = userArgs;
	}

	public void setVersion(VersionKey version) {
		this.version = version;
	}

	public void setVmArgs(String vmArgs) {
		this.vmArgs = vmArgs;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public String getUserArgs() {
		return userArgs;
	}

	public String getVmArgs() {
		return vmArgs;
	}

	public VersionKey getVersion() {
		return version;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
