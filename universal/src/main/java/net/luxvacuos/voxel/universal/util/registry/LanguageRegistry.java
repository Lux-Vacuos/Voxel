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

package net.luxvacuos.voxel.universal.util.registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

public class LanguageRegistry extends PersistentRegistry<String, String> {

	@Override
	public void load(File database) {
		this.database = database;
		BufferedReader reader = null;
		try {
			InputStream file = getClass().getClassLoader().getResourceAsStream(database.getPath());
			reader = new BufferedReader(new InputStreamReader(file));
			Type type = new TypeToken<HashMap<String, String>>() {
			}.getType();
			registry = gson.fromJson(reader, type);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void save() {
		throw new UnsupportedOperationException("Languages can't be saved");
	}

	@Override
	@Nullable
	public String getRegistryItem(@Nonnull String key) {
		String value = super.getRegistryItem(key);
		return (value == null ? key : value);
	}

}
