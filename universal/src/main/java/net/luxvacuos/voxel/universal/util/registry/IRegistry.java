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

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A basic Registry interface.
 * 
 * @see http://www.nurkiewicz.com/2014/04/hashmap-performance-improvements-in.html
 * @author HACKhalo2
 *
 * @param <K> the Registry Key. Cannot be null.
 * @param <V> the Registry Value. Can be null.
 */
public interface IRegistry<K extends Comparable<K>, V> extends Iterable<V> {
	
	/**
	 * Gets the Value from the Registry with the given Key, or null if the key does not exist.
	 * @param key the key to check
	 * @return the value associated with the supplied key, or null
	 */
	@Nullable
	public V getRegistryItem(@Nonnull K key);
	
	/**
	 * Checks to see if the Registry has an item associated with the supplied key
	 * @param key
	 * @return
	 */
	public boolean hasRegistryItem(@Nonnull K key);
	
	/**
	 * Registers an object with the associated key. The key cannot be null, but the object can be.
	 * @param key
	 * @param value
	 */
	public void register(@Nonnull K key, @Nullable V value);
	
	public boolean unregister(@Nonnull K key);
	
	public Set<K> getKeySet();

}
