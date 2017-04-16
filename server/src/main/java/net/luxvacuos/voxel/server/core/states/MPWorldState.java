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

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import net.luxvacuos.voxel.server.commands.SayCommand;
import net.luxvacuos.voxel.server.commands.ServerCommandManager;
import net.luxvacuos.voxel.server.commands.StopCommand;
import net.luxvacuos.voxel.server.commands.TimeCommand;
import net.luxvacuos.voxel.server.console.Console;
import net.luxvacuos.voxel.server.core.ServerWorldSimulation;
import net.luxvacuos.voxel.server.network.Server;
import net.luxvacuos.voxel.server.network.ServerHandler;
import net.luxvacuos.voxel.universal.commands.ICommandManager;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.network.packets.Time;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.World;

public class MPWorldState extends AbstractState {

	private ServerWorldSimulation worldSimulation;
	private IWorld world;
	private Console console;
	private ICommandManager commandManager;
	private Server server;

	public MPWorldState() {
		super(StateNames.MP_WORLD);
	}

	@Override
	public void init() {

		worldSimulation = new ServerWorldSimulation();

		world = new World((String) REGISTRY.getRegistryItem("/Voxel/Simulation/World/name"));
		world.loadDimension(0);
		world.setActiveDimension(0);

		commandManager = new ServerCommandManager();
		commandManager.registerCommand(new StopCommand());
		commandManager.registerCommand(new SayCommand());
		commandManager.registerCommand(new TimeCommand(worldSimulation));

		console = new Console();
		console.setCommandManager(commandManager);
		console.start();

		server = new Server((int) REGISTRY.getRegistryItem("/Voxel/Server/port"));
		server.run(this);
	}

	@Override
	public void dispose() {
		world.dispose();
		console.stop();
		server.end();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		world.update(delta);
		worldSimulation.update(delta);
		ServerHandler.channels.writeAndFlush(new Time(worldSimulation.getTime()));
	}

	public IWorld getWorld() {
		return world;
	}

}
