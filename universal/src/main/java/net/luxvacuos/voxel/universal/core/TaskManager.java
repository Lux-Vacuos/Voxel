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

package net.luxvacuos.voxel.universal.core;

import java.util.LinkedList;
import java.util.Queue;

public class TaskManager {

	private static Queue<Runnable> tasks = new LinkedList<>();

	private static float timer;
	private static boolean crashed = false;

	public static void update() {
		if(crashed)
			return;
		if (!tasks.isEmpty()) {
			timer += 0.5 * 0.33;
			if (timer > 1) {
				tasks.poll().run();
				timer = 0;
			}
		}
	}

	public static void addTask(Runnable task) {
		tasks.add(task);
	}

	public static boolean isEmpty() {
		return tasks.isEmpty();
	}
	
	public static void crash(){
		crashed = true;
	}

}
