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

package net.luxvacuos.voxel.client.api;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;

import net.luxvacuos.voxel.client.api.mod.ModStateLoop;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.block.BlockBase;

public class MoltenAPI {

	private GameResources gm;
	private List<ModStateLoop> modStateLoops;

	public static final String apiVersion = "0.0.1";
	public static final int apiIntVersion = 1;
	public static final int build = 1;

	public MoltenAPI(GameResources gm) {
		this.gm = gm;
		modStateLoops = new ArrayList<>();
	}

	/**
	 * 
	 * Add an entity to the specified dimension
	 * 
	 * @param dimID
	 *            Dimension ID
	 * @param entity
	 *            Entity to add
	 */
	public void addEntity(int dimID, Entity entity) {
		gm.getWorldsHandler().getActiveWorld().getDimension(dimID).getPhysicsEngine().addEntity(entity);
	}

	public void removeEntity(int dimID, Entity entity) {
		gm.getWorldsHandler().getActiveWorld().getDimension(dimID).getPhysicsEngine().removeEntity(entity);
	}

	public void registerBlock(BlockBase block) {
		Block.registerBlock(block);
	}

	public void registerModStateLoop(ModStateLoop modStateLoop) {
		modStateLoops.add(modStateLoop);
	}

	/**
	 * <h1>NOT USE THIS!!!!!!</h1>
	 * 
	 * <p>
	 * This is used internally for calling all modStates
	 * </p>
	 * 
	 * @return
	 */
	public List<ModStateLoop> getModStateLoops() {
		return modStateLoops;
	}

}
