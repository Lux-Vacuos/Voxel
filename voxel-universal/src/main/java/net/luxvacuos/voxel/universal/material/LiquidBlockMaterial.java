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

package net.luxvacuos.voxel.universal.material;

public class LiquidBlockMaterial extends BlockMaterial {
	
	/** Flag to see if this LiquidBlockMaterial is flowing */
	protected boolean isFlowing = false;
	
	public LiquidBlockMaterial(String name) {
		super(name);
		this.liquid = true;
		this.blocksMovement = false;
		this.flammable = false;
		this.canBeBroken = false;
		this.movementModifier = -0.5f;
		this.opacity = 0.4f;
	}
	
	/**
	 * Returns if this LiquidBlockMaterial is flowing
	 * @return <b>true</b> if this LiquidBlockMaterial is flowing, <b>false</b> otherwise
	 */
	public boolean isFlowing() {
		return this.isFlowing;
	}
	
	/**
	 * Returns if this LiquidBlockMaterial is still
	 * @return <b>true</b> if this LiquidBlockMaterial is still, <b>false</b> otherwise
	 */
	public boolean isStill() {
		return !(this.isFlowing);
	}
	
	/**
	 * Sets if this LiquidBlockMaterial should flow or not
	 * @param should flag to set this LiquidBlockMaterial should flow
	 * @return this LiquidBlockMaterial, for ease of use
	 */
	public LiquidBlockMaterial flow(boolean should) {
		this.isFlowing = should;
		return this;
	}

}
