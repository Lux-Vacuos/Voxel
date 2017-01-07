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

package net.luxvacuos.voxel.universal.world.dimension;

import java.util.Collection;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.universal.resources.IDisposable;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;

public interface IDimension extends IDisposable {
	
	public String getWorldName();
	
	public int getID();

	public boolean exists();
	
	public void update(float delta);
	
	public IBlock getBlockAt(int x, int y, int z);
	
	public List<BoundingBox> getGlobalBoundingBox(BoundingBox box);
	
	public Engine getEntitiesManager();
	
	public Collection<IChunk> getLoadedChunks();
}
