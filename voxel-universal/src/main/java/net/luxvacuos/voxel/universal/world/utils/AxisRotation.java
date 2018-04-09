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

package net.luxvacuos.voxel.universal.world.utils;

public class AxisRotation {
	
	private byte packed; //0b00ZZYYXX
	
	private final byte XMASK = 0b00000011;
	private final byte YMASK = 0b00001100;
	private final byte ZMASK = 0b00110000;

	public AxisRotation() {
		this.packed = 0b00000000;
	}
	
	public AxisRotation(byte packed) {
		this.packed = packed;
	}
	
	public void rotate(Direction dir, Axis axis, Rotation rot) {
		boolean reverse = (dir == Direction.COUNTERCLOCKWISE);
		byte data, mask, shift;
		byte amount = ((byte)rot.getAmount());
		switch(axis) {
		case X_AXIS:
			data = this.normalize(((byte)(this.packed & XMASK)), amount, reverse);
			mask = (byte)(this.packed & ~XMASK);
			shift = (byte)(data & XMASK);
			this.packed = (byte)(mask + shift);
			break;
		case Y_AXIS:
			data = this.normalize(((byte)((this.packed & YMASK) >> 2)), amount, reverse);
			mask = (byte)(this.packed & ~YMASK);
			shift = (byte)((data & XMASK) << 2);
			this.packed = (byte)(mask + shift);
			break;
		case Z_AXIS:
			data = this.normalize(((byte)((this.packed & ZMASK) >> 4)), amount, reverse);
			mask = (byte)(this.packed & ~ZMASK);
			shift = (byte)((data & XMASK) << 4);
			this.packed = (byte)(mask + shift);
			break;
		}
	}
	
	public final byte getPackedData() {
		return this.packed;
	}
	
	private byte normalize(byte data, byte amount, boolean reverse) {
		byte result = data;
		
		if(reverse) result -= amount;
		else result += amount;
		
		if(result < 0) return result += 4;
		if(result > 3) return result -= 4;
		return result;
	}
	
	public enum Axis {
		X_AXIS, Y_AXIS, Z_AXIS;
	}
	
	public enum Rotation {
		ZERO(0), NINETY(1), ONE_EIGHTY(2), TWO_SEVENTY(3);
		
		private int amount;
		
		private Rotation(int amount) {
			this.amount = amount;
		}
		
		public final int getAmount() {
			return this.amount;
		}
	}
	
	public enum Direction {
		CLOCKWISE, COUNTERCLOCKWISE
	}

}
