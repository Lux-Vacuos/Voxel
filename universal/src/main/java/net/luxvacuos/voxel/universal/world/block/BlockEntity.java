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

package net.luxvacuos.voxel.universal.world.block;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.collision.BoundingBox;

public class BlockEntity extends Entity implements IBlockEntity {

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAffectedByGravity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCollision() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFluid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasMetadata() {
		return false;
	}

}
