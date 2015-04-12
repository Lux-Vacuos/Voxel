package org.gnet.util;

import java.util.ArrayList;

public class UIDGenerator {

	// Actual length
	private final int length;

	// Represents how long the UID is "9999" = 4
	private final String string;

	// List to contain the previous UID's
	private final ArrayList<Integer> list;

	public UIDGenerator(final String string) {
		length = string.length();
		this.string = string;
		list = new ArrayList<Integer>();
	}

	public int generateUID() {
		final String id = new RandomInt(0, Integer.parseInt(string))
				.generateRandom() + "";
		if (!(id.length() == length)) {
			return generateUID();
		} else {
			final int idNum = Integer.parseInt(id);
			if (!list.contains(idNum)) {
				list.add(idNum);
				return idNum;
			} else if (list.contains(idNum)) {
				return generateUID();
			} else {
				return -1;
			}
		}
	}
}