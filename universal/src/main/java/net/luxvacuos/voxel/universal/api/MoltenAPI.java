package net.luxvacuos.voxel.universal.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.voxel.universal.core.EngineType;

public class MoltenAPI {

	public static final String apiVersion = "0.0.2";
	public static final int apiIntVersion = 2;
	public static final int build = 8;

	private Class<?> api;
	private Object apiI;

	private Map<String, Method> methods;

	public MoltenAPI(EngineType type) throws Exception {
		switch (type) {
		case CLIENT:
			api = Class.forName("net.luxvacuos.voxel.client.api.MoltenAPI");
			break;
		case SERVER:
			api = Class.forName("net.luxvacuos.voxel.server.api.MoltenAPI");
			break;
		case UNKNOWN:
			// TODO: Handle This
			break;
		default:
			throw new RuntimeException("Invalid Engine Type");
		}
		apiI = api.newInstance();
		methods = new HashMap<>();
		for (Method mth : api.getMethods()) {
			methods.put(mth.getName(), mth);
		}
	}

	public void testPrint() {
		runCustomMethod("testPrint");
	}

	public Object getChunk(int cx, int cy, int cz) {
		return runCustomMethod("getChunk", cx, cy, cz);
	}

	public void addChunk(Object chunk) {
		runCustomMethod("addChunk", chunk);
	}

	public void addEntity(Object entity) {
		runCustomMethod("addEntity", entity);
	}

	public Object runCustomMethod(String mehtod, Object... objects) {
		try {
			return methods.get(mehtod).invoke(apiI, objects);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
