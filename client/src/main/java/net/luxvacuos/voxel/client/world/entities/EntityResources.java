package net.luxvacuos.voxel.client.world.entities;

import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.client.resources.ResourceLoader;

public class EntityResources {

	private static TexturedModel plane;

	public static void load(ResourceLoader loader) {

		RawModel planeModel = loader.loadObjModel("plane");
		plane = new TexturedModel(planeModel,
				new Material(new Vector4f(1), 0.6f, 0, 0.0f, loader.loadTexture("plane"),
						loader.loadTextureMisc("plane_norm"), null, null, null));

	}

	public static TexturedModel getPlane() {
		return plane;
	}

}
