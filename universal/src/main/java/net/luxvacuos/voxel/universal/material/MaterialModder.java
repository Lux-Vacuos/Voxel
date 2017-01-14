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

import com.badlogic.gdx.math.MathUtils;

import net.luxvacuos.voxel.universal.tools.ToolTier;

public final class MaterialModder {

	public MaterialModder() {
	}

	public MatMod modify(IMaterial mat) {
		return new MatMod(mat);
	}

	public final class MatMod {
		private final IMaterial mat;

		protected MatMod(IMaterial mat) {
			this.mat = mat;
		}

		public IMaterial done() {
			return this.mat;
		}

		// -----ObjectMaterial-----\\

		public MatMod setFlammable(boolean flammable) {
			((ObjectMaterial) this.mat).flammable = flammable;
			return this;
		}

		public MatMod setBurnTime(float burnTime) {
			boolean flag = ((ObjectMaterial) this.mat).flammable;
			((ObjectMaterial) this.mat).burnTime = (burnTime < 0f && !flag ? -1 : burnTime);
			return this;
		}

		public MatMod setOpacity(float opacity) {
			MathUtils.clamp(opacity, 0f, 1f);
			((ObjectMaterial) this.mat).opacity = opacity;
			return this;
		}

		public MatMod setMovementModifier(float movementModifier) {
			((ObjectMaterial) this.mat).movementModifier = movementModifier;
			return this;
		}

		public MatMod setHardness(float hardness) {
			MathUtils.clamp(hardness, 1f, Float.MAX_VALUE);
			((ObjectMaterial) this.mat).hardness = hardness;
			return this;
		}

		// -----BlockMaterial-----\\

		public MatMod liquid() {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).liquid = true;
			}
			return this;
		}

		public MatMod solid() {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).liquid = false;
			}
			return this;
		}

		public MatMod setBlocksMovement(boolean should) {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).blocksMovement = should;
			}
			return this;
		}

		public MatMod setRequiresTool(boolean should) {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).requiresTool = should;
			}
			return this;
		}

		public MatMod setMinimumTier(ToolTier tier) {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).minimumTier = tier;
			}
			return this;
		}

		public MatMod canBeBroken(boolean flag) {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).canBeBroken = flag;
			}
			return this;
		}

		public MatMod affectedByGravity(boolean flag) {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).affectedByGravity = flag;
			}
			return this;
		}

		public MatMod providesLight(boolean flag) {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).providesLight = flag;
			}
			return this;
		}

		public MatMod lightAmount(byte amount) {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).lightAmount = amount;
			}
			return this;
		}

		public MatMod setVisible(boolean flag) {
			if (this.mat instanceof BlockMaterial) {
				((BlockMaterial) this.mat).visible = flag;
			}
			return this;
		}

		// -----
	}

}
