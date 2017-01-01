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

package net.luxvacuos.voxel.client.input;

/**
 * Internal utility class to keep track of event positions in an array. When the
 * array is full the position will wrap to the beginning.
 */
@Deprecated
class EventQueue {

	private int maxEvents = 32;
	private int currentEventPos = -1;
	private int nextEventPos = 0;

	EventQueue(int maxEvents) {
		this.maxEvents = maxEvents;
	}

	/**
	 * add event to the queue
	 */
	void add() {
		nextEventPos++; // increment next event position
		if (nextEventPos == maxEvents)
			nextEventPos = 0; // wrap next event position

		if (nextEventPos == currentEventPos) {
			currentEventPos++; // skip oldest event is queue full
			if (currentEventPos == maxEvents)
				currentEventPos = 0; // wrap current event position
		}
	}

	/**
	 * Increment the event queue
	 * 
	 * @return - true if there is an event available
	 */
	boolean next() {
		if (currentEventPos == nextEventPos - 1)
			return false;
		if (nextEventPos == 0 && currentEventPos == maxEvents - 1)
			return false;

		currentEventPos++; // increment current event position
		if (currentEventPos == maxEvents)
			currentEventPos = 0; // wrap current event position

		return true;
	}

	int getMaxEvents() {
		return maxEvents;
	}

	int getCurrentPos() {
		return currentEventPos;
	}

	int getNextPos() {
		return nextEventPos;
	}
}
