package net.luxvacuos.voxel.client.api;

import net.luxvacuos.voxel.universal.api.APIMethod;

public class Client_Test implements APIMethod<Object> {
	
	@Override
	public Object run(Object... objects) {
		return "TEST";
	}

}
