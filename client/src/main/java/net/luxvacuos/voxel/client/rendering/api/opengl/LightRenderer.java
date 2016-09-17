package net.luxvacuos.voxel.client.rendering.api.opengl;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;

public class LightRenderer {

	private List<Light> lights;

	public LightRenderer() {
		lights = new ArrayList<>();
	}

	public void addLight(Light light) {
		lights.add(light);
	}

	public List<Light> getLights() {
		return lights;
	}

}
