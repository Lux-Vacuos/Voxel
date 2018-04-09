
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

import java.util.Random;
import java.util.UUID;

import net.luxvacuos.lightengine.client.bootstrap.Bootstrap;
import net.luxvacuos.lightengine.universal.core.GlobalVariables;
import net.luxvacuos.lightengine.universal.core.IEngineLoader;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.core.states.StateMachine;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.MainMenuState;
import net.luxvacuos.voxel.universal.remote.User;

public class EngineLoader implements IEngineLoader {

	@Override
	public void loadExternal() {
		TaskManager.tm.addTaskMainThread(() -> StateMachine.registerState(new MainMenuState()));
	}
	
	public static void main(String[] args) {
		GlobalVariables.PROJECT = "Voxel";
		// TODO: Move this
		ClientVariables.user = new User("user" + new Random().nextInt(), UUID.randomUUID().toString());
		new Bootstrap(args, new EngineLoader());
	}


}
