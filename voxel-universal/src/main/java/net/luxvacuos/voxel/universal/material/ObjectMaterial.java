/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

package net.luxvacuos.voxel.universal.material;

public class ObjectMaterial implements IMaterial {
	
	/** The name of this Material */
	protected final String name;
	
	/** Flag to see if the Material is flammable */
	protected boolean flammable = false;
	
	/** 
	 * The burn time, in seconds, this Material burns when lit on fire.<br />
	 * Set to <b>-1f</b> if the Material has no burn time
	 */
	protected float burnTime = -1f;
	
	/** How opaque the Material is. 0 for transparent, 1 for opaque. */
	protected float opacity = 1f;
	
	/**
	 * The modifier for movement when this Material is walked on or through, or used<br />
	 * <b>Negative values</b> for slower movements, <b>Positive values</b> for faster movements<br />
	 */
	protected float movementModifier = 0f;
	
	/** How hard this Material is */
	protected float hardness = 1f;
	
	/** The type of object associated with this Material */
	protected Type objectType = Type.OTHER;
	
	protected ObjectMaterial(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of this Material
	 * 
	 * @return the name of this Material
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns if this Material is flammable
	 * 
	 * @return <b>true</b> if flammable, <b>false</b> otherwise
	 */
	public boolean isFlammable() {
		return this.flammable;
	}
	
	/**
	 * Gets the burn time of this Material in seconds<br />
	 * 
	 * This will return <b>-1f</b> if the Material is not flammable
	 * @return the time in seconds this Material burns, or <b>-1f</b> if the Material is not flammable
	 */
	public float getBurnTime() {
		return this.burnTime;
	}
	
	/**
	 * Returns if this Material is opaque<br />
	 * <br />
	 * <b>NOTE:</b> This will return <b>true</b> if the internal float is <i>GREATER THAN OR EQUAL TO</i> 0.5f
	 * 
	 * @return <b>true</b> if the Material is opaque, <b>false</b> otherwise
	 */
	public boolean isOpaque() {
		return (this.opacity >= 0.5f);
	}
	
	/**
	 * Returns if this Material is transparent<br />
	 * <br />
	 * <b>NOTE:</b> This will return <b>true</b> if the internal float is <i>LESS THAN</i> 0.5f;
	 * @return <b>true</b> if this Material is transparent, <b>false</b> otherwise
	 */
	public boolean isTransparent() {
		return (this.opacity < 0.5f);
	}
	 /**
	  * Returns the opacity of this Material
	  * @return the opacity of this Material between 0f and 1f
	  */
	public float getOpacity() {
		return this.opacity;
	}
	
	/**
	 * Returns if this Material has a movement modifier
	 * @return <b>true</b> if this Material has a movement modifier, <b>false</b> otherwise
	 */
	public boolean hasMovementModifier() {
		return (this.movementModifier != 0f);
	}
	
	/**
	 * Gets the movement modifier of this Material
	 * @return a positive modifier for faster movement <b>or</b> a negative modifier for slower movement
	 */
	public float getMovementModifier() {
		return this.movementModifier;
	}
	
	/**
	 * Gets the hardness of this Material
	 * @return how hard this material is
	 */
	public float getHardness() {
		return this.hardness; // ( ͡° ͜ʖ ͡°)
	}
	
	public Type getMaterialType() {
		return this.objectType;
	}

	public enum Type {
		BLOCK, ITEM, OTHER
	}
}
