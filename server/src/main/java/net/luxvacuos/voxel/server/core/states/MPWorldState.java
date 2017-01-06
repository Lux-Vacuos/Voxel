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

package net.luxvacuos.voxel.server.core.states;

import net.luxvacuos.voxel.server.commands.ServerCommandManager;
import net.luxvacuos.voxel.server.commands.StopCommand;
import net.luxvacuos.voxel.server.console.Console;
import net.luxvacuos.voxel.server.core.ServerVariables;
import net.luxvacuos.voxel.server.core.ServerWorldSimulation;
import net.luxvacuos.voxel.universal.commands.ICommandManager;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.material.MaterialModder;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.World;
import net.luxvacuos.voxel.universal.world.block.BlockBase;
import net.luxvacuos.voxel.universal.world.block.Blocks;

public class MPWorldState extends AbstractState {

	private ServerWorldSimulation worldSimulation;
	private IWorld world;
	private Console console;
	private ICommandManager commandManager;

	public MPWorldState() {
		super(StateNames.MP_WORLD);
	}

	@Override
	public void init() {
		
		worldSimulation = new ServerWorldSimulation();

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
		world = new World(ServerVariables.worldName);
		world.loadDimension(0);
		world.setActiveDimension(0);
		
		commandManager = new ServerCommandManager();
		commandManager.registerCommand(new StopCommand());
		
		console = new Console();
		console.setCommandManager(commandManager);
		console.start();
	}

	@Override
	public void dispose() {
		world.dispose();
		console.stop();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		world.update(delta);
		worldSimulation.update(delta);
	}

}
