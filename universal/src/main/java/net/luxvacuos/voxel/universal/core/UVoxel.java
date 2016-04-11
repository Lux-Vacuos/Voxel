package net.luxvacuos.voxel.universal.core;

import java.util.Map;

import net.luxvacuos.voxel.universal.api.APIMethod;
import net.luxvacuos.voxel.universal.api.MoltenAPI;
import net.luxvacuos.voxel.universal.resources.UGameResources;

public abstract class UVoxel {

	protected UGameResources gameResources;
	protected String prefix;
	protected boolean client, server;

	public abstract void registerAPIMethods(MoltenAPI api, Map<String, APIMethod<Object>> methods);

	public String getPrefix() {
		return prefix;
	}

	public boolean isClient() {
		return client;
	}

	public boolean isServer() {
		return server;
	}

}
