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

package net.luxvacuos.voxel.universal.tools;

public enum ToolTier {
	ZERO(0, 1f),
	ONE(1, 1.5f),
	TWO(2, 2f),
	THREE(3, 2.25f),
	FOUR(4, 2.5f),
	FIVE(5, 2.7f),
	SIX(6, 2.9f),
	SEVEN(7, 3f),
	EIGHT(8, 3.1f),
	NINE(9, 3.15f),
	TEN(10, 3.2f),
	CUSTOM(-1, 1f);
	
	private final float multiplier;
	private final int tier;
	
	private ToolTier(int tier, float multiplier) {
		this.multiplier = multiplier;
		this.tier = tier;
	}
	
	/**
	 * Returns if this ToolTier is a custom one
	 * @return <b>true</b> if this ToolTier is a custom tier, <b>false</b> otherwise
	 */
	public final boolean isCustomTier() {
		return this.tier == -1;
	}
	
	/**
	 * Gets the multiplier this ToolTier provides
	 * @return the ToolTier multiplier
	 */
	public final float getMultiplier() {
		return this.multiplier;
	}
	
	/**
	 * Gets the tier this ToolTier is
	 * @return the tier of this ToolTier
	 */
	public final int getTier() {
		return this.tier;
	}
	
	/**
	 * Checks to see if the supplied ToolTier is sufficient enough for the base ToolTier
	 * @param base the ToolTier to check against
	 * @param supplied the ToolTier used to check the base
	 * @return <b>true</b> if supplied >= base
	 */
	public static boolean isSufficient(ToolTier base, ToolTier supplied) {
		return (supplied.tier >= base.tier);
	}
	
	/**
	 * Checks to see if the supplied CustomToolTier is sufficient enough for the base ToolTier
	 * @param base the ToolTier to check against
	 * @param supplied the CustomToolTier used to check the base
	 * @return <b>true</b> if supplied >= base
	 */
	public static boolean isSufficient(ToolTier base, ICustomToolTier supplied) {
		return (supplied.getTier() >= base.tier);
	}
	
	/**
	 * Checks to see if the supplied ToolTier is sufficient enough for the base CustomToolTier
	 * @param base the CustomToolTier to check against
	 * @param supplied the ToolTier used to check the base
	 * @return <b>true</b> if supplied >= base
	 */
	public static boolean isSufficient(ICustomToolTier base, ToolTier supplied) {
		return (supplied.tier >= base.getTier());
	}
	
	/**
	 * Checks to see if the supplied CustomToolTier is sufficient enough for the base CustomToolTier
	 * @param base the ToolTier to check against
	 * @param supplied the ToolTier used to check the base
	 * @return <b>true</b> if supplied >= base
	 */
	public static boolean isSufficient(ICustomToolTier base, ICustomToolTier check) {
		return (check.getTier() >= base.getTier());
	}
}
