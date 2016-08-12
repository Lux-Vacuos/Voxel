package net.luxvacuos.voxel.launcher.core;

import java.util.ArrayList;
import java.util.List;

public class VersionsHandler {

	private List<VersionKey> versions;

	public VersionsHandler() {
		versions = new ArrayList<>();
	}

	public List<VersionKey> getVersions() {
		return versions;
	}

}
