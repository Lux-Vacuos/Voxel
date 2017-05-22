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

package net.luxvacuos.voxel.client.core.subsystems;

import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.block.types.WaterBlock;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.core.subsystems.ISubsystem;
import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.material.MaterialModder;
import net.luxvacuos.voxel.universal.world.block.Blocks;

public class WorldSubsystem implements ISubsystem {

	@Override
	public void init() {
		TaskManager.addTask(() -> {
			MaterialModder matMod = new MaterialModder();
			Blocks.startRegister("voxel");
			BlockMaterial airMat = new BlockMaterial("air");
			airMat = (BlockMaterial) matMod.modify(airMat).canBeBroken(false).setBlocksMovement(false).setOpacity(0f)
					.setVisible(false).done();
			Blocks.register(new RenderBlock(airMat, new BlockFaceAtlas("air")));
			Blocks.register(new RenderBlock(new BlockMaterial("stone"), new BlockFaceAtlas("stone")));
			Blocks.register(
					new RenderBlock(new BlockMaterial("grass"), new BlockFaceAtlas("grass", "dirt", "grassSide")));
			Blocks.register(new RenderBlock(new BlockMaterial("dirt"), new BlockFaceAtlas("dirt")));
			Blocks.register(new RenderBlock(new BlockMaterial("sand"), new BlockFaceAtlas("sand")));
			Blocks.register(new RenderBlock(new BlockMaterial("cobblestone"), new BlockFaceAtlas("cobblestone")));
			Blocks.register(new RenderBlock(new BlockMaterial("wood"), new BlockFaceAtlas("wood")));
			Blocks.register(new RenderBlock(new BlockMaterial("leaves"), new BlockFaceAtlas("leaves")));
			BlockMaterial glassMat = new BlockMaterial("glass");
			glassMat = (BlockMaterial) matMod.modify(glassMat).setOpacity(0.0f).done();
			Blocks.register(new RenderBlock(glassMat, new BlockFaceAtlas("glass")));
			BlockMaterial waterMat = new BlockMaterial("water");
			waterMat = (BlockMaterial) matMod.modify(waterMat).canBeBroken(false).setBlocksMovement(false)
					.affectedByGravity(true).liquid().setOpacity(0.2f).done();
			Blocks.register(new WaterBlock(waterMat, new BlockFaceAtlas("water")));
			Blocks.register(new RenderBlock(new BlockMaterial("ice"), new BlockFaceAtlas("ice")));
			// Blocks.register(new RenderBlock(new BlockMaterial("pedestal"),
			// new BlockFaceAtlas("pedestaltop", "pedestalbottom",
			// "pedestal")));
			Blocks.finishRegister();
		});
	}

	@Override
	public void restart() {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void dispose() {
	}
}
