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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.luxvacuos.voxel.universal.util.MapDeserializerDoubleAsIntFix;

public class SystemRegistry extends PersistentRegistry<Key, Object> {

	public SystemRegistry() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		gsonBuilder.registerTypeAdapter(new TypeToken<HashMap<String, Object>>() {
		}.getType(), new MapDeserializerDoubleAsIntFix()).setPrettyPrinting();
		gson = gsonBuilder.create();
	}

	@Override
	public void save() {
		Writer writer = null;
		try {
			writer = new FileWriter(this.database);
			Map<Key, Object> save = new HashMap<>(this.registry);
			List<Key> toRemove = new ArrayList<>();
			for (Key key : save.keySet()) {
				if (!key.saveKey())
					toRemove.add(key);
			}
			for (Key key : toRemove) {
				save.remove(key);
			}
			gson.toJson(save, writer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void load(File database) {
		this.database = database;
		if (this.database.exists()) {
			Reader reader = null;
			try {
				reader = new FileReader(this.database);
				Type type = new TypeToken<HashMap<String, Object>>() {
				}.getType();
				Map<String, Object> load = gson.fromJson(reader, type);
				for (String key : load.keySet()) {
					this.registry.put(new Key(key, true), load.get(key));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			String absolutePath = this.database.getAbsolutePath();
			String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
			new File(filePath).mkdirs();
		}
	}

}
