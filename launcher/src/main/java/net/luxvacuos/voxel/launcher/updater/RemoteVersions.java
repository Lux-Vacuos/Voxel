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

package net.luxvacuos.voxel.launcher.updater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteVersions {

	private Map<String, List<VersionKey>> versions = new HashMap<>();

	public Map<String, List<VersionKey>> getVersions() {
		return versions;
	}

	public List<VersionKey> getVersions(String key) {
		return versions.get(key);
	}

}
