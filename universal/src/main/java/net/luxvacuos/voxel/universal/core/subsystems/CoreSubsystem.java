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

package net.luxvacuos.voxel.universal.core.subsystems;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import net.luxvacuos.voxel.universal.bootstrap.AbstractBootstrap;
import net.luxvacuos.voxel.universal.core.AbstractGameSettings;
import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.util.registry.Key;
import net.luxvacuos.voxel.universal.util.registry.SystemRegistry;

public class CoreSubsystem implements ISubsystem {

	protected static AbstractGameSettings gameSettings;
	public static SystemRegistry REGISTRY;
	public static int ups;
	public static int upsCount;

	@Override
	public void init() {
		try {
			Manifest manifest = new Manifest(getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
			Attributes attr = manifest.getMainAttributes();
			String t = attr.getValue("Specification-Version");
			if (t != null)
				GlobalVariables.version = t;
		} catch (IOException E) {
			E.printStackTrace();
		}
		REGISTRY = new SystemRegistry();
		REGISTRY.register(new Key("/Voxel/Settings/file"), AbstractBootstrap.getPrefix() + "/config/registry.json");
		REGISTRY.register(new Key("/Voxel/System/os"),
				System.getProperty("os.name") + " " + System.getProperty("os.arch").toUpperCase());
	}

	@Override
	public void restart() {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void dispose() {
	}

	public static AbstractGameSettings getGameSettings() {
		return gameSettings;
	}

}
