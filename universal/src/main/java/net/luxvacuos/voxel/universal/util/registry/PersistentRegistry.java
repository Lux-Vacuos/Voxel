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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PersistentRegistry<K extends Comparable<K>, V> implements IRegistry<K, V> {

	@Nonnull
	protected Map<K, V> registry = new HashMap<K, V>();

	protected File database;
	protected Gson gson;

	public PersistentRegistry() {
		gson = new Gson();
	}

	public void save() {
		Writer writer = null;
		try {
			writer = new FileWriter(this.database);
			gson.toJson(registry, writer);
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

	public void load(File database) {
		this.database = database;
		if (this.database.exists()) {
			Reader reader = null;
			try {
				reader = new FileReader(this.database);
				Type type = new TypeToken<HashMap<K, V>>() {
				}.getType();
				registry = gson.fromJson(reader, type);
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

	@Override
	public Iterator<V> iterator() {
		return this.registry.values().iterator();
	}

	@Override
	@Nullable
	public V getRegistryItem(@Nonnull K key) {
		return this.registry.get(key);
	}

	@Override
	public boolean hasRegistryItem(@Nonnull K key) {
		return this.registry.containsKey(key);
	}

	@Override
	public void register(@Nonnull K key, @Nullable V value) {
		this.registry.put(key, value);
	}

	@Override
	public boolean unregister(@Nonnull K key) {
		if (this.registry.containsKey(key)) {
			this.registry.remove(key);
			return true;
		} else
			return false;
	}

	@Override
	public Set<K> getKeySet() {
		return Collections.unmodifiableSet(this.registry.keySet());
	}

}
