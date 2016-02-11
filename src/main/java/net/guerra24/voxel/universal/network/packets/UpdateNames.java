package net.guerra24.voxel.universal.network.packets;

import java.io.Serializable;
import java.util.List;

public class UpdateNames implements Serializable {

	private static final long serialVersionUID = 2796495908386106974L;
	private List<String> names;

	public UpdateNames() {
	}

	public UpdateNames(List<String> names) {
		this.names = names;
	}

	public List<String> getNames() {
		return names;
	}

}
