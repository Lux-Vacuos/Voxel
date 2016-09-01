package net.luxvacuos.voxel.client.world;

import java.util.HashMap;
import java.util.Map;
@Deprecated
public class DimensionData {

	private Map<String, Object> values;

	public DimensionData() {
		values = new HashMap<>();
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public void addObject(String key, Object obj) {
		values.put(key, obj);
	}

	public Object getObject(String key) {
		return values.get(key);
	}

}
