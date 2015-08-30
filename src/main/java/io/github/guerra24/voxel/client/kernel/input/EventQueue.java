package io.github.guerra24.voxel.client.kernel.input;

/**
 * EventQueue
 * 
 * @author kappaOne
 * @category Input
 */
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
		nextEventPos++;
		if (nextEventPos == maxEvents)
			nextEventPos = 0;

		if (nextEventPos == currentEventPos) {
			currentEventPos++;
			if (currentEventPos == maxEvents)
				currentEventPos = 0;
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

		currentEventPos++;
		if (currentEventPos == maxEvents)
			currentEventPos = 0;
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
