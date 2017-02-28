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

package net.luxvacuos.voxel.server.core;

import java.io.File;

import net.luxvacuos.voxel.universal.core.AbstractInternalSubsystem;
import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.material.MaterialModder;
import net.luxvacuos.voxel.universal.world.block.BlockBase;
import net.luxvacuos.voxel.universal.world.block.Blocks;

public class ServerInternalSubsystem extends AbstractInternalSubsystem {

	private static ServerInternalSubsystem instance = null;

	public static ServerInternalSubsystem getInstance() {
		if (instance == null)
			instance = new ServerInternalSubsystem();
		return instance;
	}

	private ServerInternalSubsystem() {
	}

	@Override
	public void preInit() {
		gameSettings = new ServerGameSettings();
		gameSettings.load(new File(ServerVariables.SETTINGS_PATH));
		gameSettings.read();
	}

	@Override
	public void init() {
		MaterialModder matMod = new MaterialModder();

		Blocks.startRegister("voxel");
		BlockMaterial airMat = new BlockMaterial("air");
		airMat = (BlockMaterial) matMod.modify(airMat).canBeBroken(false).setBlocksMovement(false).setOpacity(0f)
				.done();
		Blocks.register(new BlockBase(airMat));
		Blocks.register(new BlockBase(new BlockMaterial("stone")));
		Blocks.register(new BlockBase(new BlockMaterial("grass")));
		Blocks.register(new BlockBase(new BlockMaterial("dirt")));
		Blocks.register(new BlockBase(new BlockMaterial("sand")));
		Blocks.register(new BlockBase(new BlockMaterial("cobblestone")));
		Blocks.register(new BlockBase(new BlockMaterial("wood")));
		Blocks.register(new BlockBase(new BlockMaterial("leaves")));
		Blocks.register(new BlockBase(new BlockMaterial("glass")));
		BlockMaterial waterMat = new BlockMaterial("water");
		waterMat = (BlockMaterial) matMod.modify(waterMat).canBeBroken(false).setBlocksMovement(false)
				.affectedByGravity(true).liquid().done();
		Blocks.register(new BlockBase(waterMat));
		Blocks.finishRegister();
	}

	@Override
	public void dispose() {
		gameSettings.save();
	}

}
