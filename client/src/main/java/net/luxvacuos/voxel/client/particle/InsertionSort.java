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

package net.luxvacuos.voxel.client.particle;

import java.util.List;

public class InsertionSort {

	/**
	 * Sorts a list of particles so that the particles with the highest distance
	 * from the camera are first, and the particles with the shortest distance
	 * are last.
	 * 
	 * @param list
	 *            the list of particles needing sorting.
	 */
	public static void sortHighToLow(List<Particle> list) {
		for (int i = 1; i < list.size(); i++) {
			Particle item = list.get(i);
			if (item.getDistance() > list.get(i - 1).getDistance()) {
				sortUpHighToLow(list, i);
			}
		}
	}

	private static void sortUpHighToLow(List<Particle> list, int i) {
		Particle item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).getDistance() < item.getDistance()) {
			attemptPos--;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}

}
