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

package net.luxvacuos.voxel.universal.util.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleRegistry<K extends Comparable<K>, V> implements IRegistry<K, V> {
	
	@Nonnull
	protected final Map<K, V> registry = new HashMap<K, V>();

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
		if(this.registry.containsKey(key)) {
			this.registry.remove(key);
			return true;
		} else return false;
	}

	@Override
	public Set<K> getKeySet() {
		return Collections.unmodifiableSet(this.registry.keySet());
	}

}
