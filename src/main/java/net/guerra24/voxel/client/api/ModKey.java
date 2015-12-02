package net.guerra24.voxel.client.api;

public class ModKey implements Cloneable {
	private int id, apiVersion;
	private String name, version;

	public ModKey(int id, String name, String version, int apiVersion) {
		this.id = id;
		this.name = name;
		this.version = version;
		this.apiVersion = apiVersion;
	}

	@Override
	public int hashCode() {
		return id + name.hashCode() + version.hashCode() + apiVersion;
	}

	@Override
	public boolean equals(Object obj) {
		ModKey key = (ModKey) obj;
		if (key.id != id)
			return false;
		if (!key.name.equals(name))
			return false;
		if (!key.version.equals(version))
			return false;
		if (key.apiVersion != apiVersion)
			return false;
		return true;
	}

	@Override
	public ModKey clone() {
		return new ModKey(id, name, version, apiVersion);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public int getApiVersion() {
		return apiVersion;
	}
}
