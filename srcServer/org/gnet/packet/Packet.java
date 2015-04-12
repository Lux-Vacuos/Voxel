package org.gnet.packet;

import java.util.HashMap;
import java.util.Iterator;

public class Packet implements java.io.Serializable {
	private static final long serialVersionUID = -3945349088377843798L;

	// The packets name.
	private final String packetName;

	// A HashMap that contains packets keys/values.
	private final HashMap<String, Object> entrys;

	// Number of slots to initialize our HashMap to.
	private final int allottedSlots;

	// True to print out packet process for debugging.
	private boolean debugging;

	/**
	 * Constructor.
	 * 
	 * @param packetName
	 *            The desired packet name.
	 * @param dataSlots
	 *            The amount of slots to reserve for the packets data list.
	 */
	public Packet(final String packetName, final int dataSlots) {
		this.packetName = packetName;
		allottedSlots = dataSlots;
		entrys = new HashMap<String, Object>(dataSlots);
		print("New packet created. (Name: " + packetName + ", Data slots: "
				+ dataSlots + ")");
	}

	/**
	 * Add a entry to our packets data list.
	 * 
	 * @param name
	 *            The 'key'/name of the value to add to data list.
	 * @param value
	 *            The value of the 'key' to be set.
	 */
	public void addEntry(final String name, final Object value) {
		if (entrys.size() == allottedSlots) {
			printERR("Unable to add data entry (" + name
					+ "), please increase data slots!");
			return;
		}
		if (!entrys.containsKey(name)) {
			entrys.put(name, value);
			print("Successfully added data entry (" + name + ").");
		} else {
			printERR("Unable to add data entry (" + name
					+ "), already exists in DB.");
			return;
		}

	}

	/**
	 * Remove a entry from our packets data list.
	 * 
	 * @param name
	 *            The 'key'/name to remove from data list.
	 */
	public void removeEntry(final String name) {
		if (entrys.containsKey(name)) {
			entrys.remove(name);
			print("Successfully removed data entry (" + name + ").");
		} else {
			printERR("Unable to remove data entry (" + name
					+ "), doesn't exist in DB.");
		}
	}

	/**
	 * Edit a stored data entry.
	 * 
	 * @param name
	 *            The 'key'/name that is requesting editing.
	 * @param newValue
	 *            The new value for this 'key'.
	 */
	public void editEntry(final String name, final String newValue) {
		if (entrys.containsKey(name)) {
			entrys.put(name, newValue);
			print("Successfully edited data entry (" + name + ").");
		} else {
			printERR("Unable to edit data entry (" + name
					+ "), doesn't exist in DB.");
		}
	}

	/**
	 * Get a value from a stored entry.
	 * 
	 * @param name
	 *            The 'key'/name of the value to retrieve.
	 * @return The value from data list determined by the 'key' parameter.
	 */
	public Object getEntry(final String name) {
		if (!entrys.containsKey(name)) {
			printERR("Unable to fetch data entry (" + name
					+ "), doesn't exist in DB.");
			return null;
		}
		print("Fetching data entry (" + name + ").");
		long start = 0;

		// Only bother to monitor time taken to retrieve a value
		// if the packets debugging is enabled.
		if (debugging) {
			start = System.nanoTime();
		}
		final Iterator<String> keySetIterator = entrys.keySet().iterator();
		while (keySetIterator.hasNext()) {
			if (keySetIterator.next().equals(name)) {
				// Obtain the value.
				final Object value = entrys.get(name);

				// Only bother to calculate the time taken to retrieve the value
				// if the packets debugging has been enabled.
				if (debugging) {
					final long time = (System.nanoTime() - start) / 1000000;
					print("Data entry (" + name + ") fetched in " + time
							+ "ms.");
				}
				return value;
			}
		}
		return null;
	}

	public void setDebugging(final boolean debugging) {
		this.debugging = debugging;
	}

	private final void print(final String msg) {
		if (debugging) {
			System.out.println("[Packet]: " + msg);
		}
	}

	private final void printERR(final String msg) {
		if (debugging) {
			System.err.println("[Packet]: " + msg);
		}
	}

	public String getPacketName() {
		return packetName;
	}

	public int getAllottedSlots() {
		return allottedSlots;
	}

}
