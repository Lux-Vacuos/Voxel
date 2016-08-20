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

package net.luxvacuos.voxel.universal.material;

import net.luxvacuos.voxel.universal.tools.ToolTier;

public class BlockMaterial extends ObjectMaterial {
	
	/** Flag to see if this BlockMaterial is a liquid */
	protected boolean liquid = false;
	
	/** Flag to see if this BlockMaterial should block movement */
	protected boolean blocksMovement = true;
	
	/** Flag to see if this BlockMaterial requires a tool to break */
	protected boolean requiresTool = false;
	
	/** The lowest ToolTier needed to break this BlockMaterial */
	protected ToolTier minimumTier = ToolTier.ZERO;
	
	/** Flag to see if this BlockMaterial can be broken */
	protected boolean canBeBroken = true;
	
	/** Flag to see if this BlockMaterial is affected by gravity */
	protected boolean affectedByGravity = false;
	
	/** Flag to see if this BlockMaterial provides light */
	protected boolean providesLight = false;
	
	/** The amount of light this BlockMaterial provides */
	protected byte lightAmount = -1;
	
	public BlockMaterial(String name) {
		super(name);
		this.objectType = Type.BLOCK;
	}
	
	public BlockMaterial(String name, ToolTier minimumTier) {
		super(name);
		this.objectType = Type.BLOCK;
		this.minimumTier = minimumTier;
	}
	
	/**
	 * Returns if this BlockMaterial is a liquid
	 * @return <b>true</b> if this BlockMaterial is a liquid, <b>false</b> otherwise
	 */
	public boolean isLiquid() {
		return this.liquid;
	}
	
	/**
	 * Returns if this BlockMaterial is solid
	 * @return <b>true</b> if this BlockMaterial is solid, <b>false</b> otherwise
	 */
	public boolean isSolid() {
		return !(this.liquid);
	}
	
	/**
	 * Returns if this BlockMaterial should block movement
	 * @return <b>true</b> if this BlockMaterial blocks movement, <b>false</b> otherwise
	 */
	public boolean blocksMovement() {
		return this.blocksMovement;
	}
	
	/**
	 * Returns if this BlockMaterial requires a tool to break effectively
	 * @return <b>true</b> if this BlockMaterial requires a tool, <b>false</b> otherwise
	 */
	public boolean requiresTool() {
		return this.requiresTool;
	}
	
	/**
	 * Gets the minimum ToolTier needed to break this BlockMaterial
	 * @return the minimum ToolTier needed to break this BlockMaterial
	 */
	public ToolTier getTierNeeded() {
		return this.minimumTier;
	}
	
	/**
	 * Returns if this BlockMaterial can be broken
	 * @return <b>true</b> if this BlockMaterial can be broken, <b>false</b> otherwise
	 */
	public boolean canBeBroken() {
		return this.canBeBroken;
	}
	
	/**
	 * Returns if this BlockMaterial is affected by gravity
	 * @return <b>true</b> if this BlockMaterial is affected by gravity, <b>false</b> otherwise
	 */
	public boolean affectedByGravity() {
		return this.affectedByGravity;
	}
	
	/**
	 * Returns if this BlockMaterial provides light
	 * @return <b>true</b> if this BlockMaterial provides light, <b>false</b> otherwise
	 */
	public boolean providesLight() {
		return this.providesLight;
	}
	
	/**
	 * Gets the amount of light this BlockMaterial provides, or -1 if it doesn't provide any light
	 * @return 0 to Byte.MAX_VALUE, or -1 for no light
	 */
	public byte lightAmount() {
		return this.lightAmount;
	}

}
