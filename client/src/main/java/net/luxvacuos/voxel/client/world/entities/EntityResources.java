package net.luxvacuos.voxel.client.world.entities;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.resources.models.ModelTexture;
import net.luxvacuos.voxel.client.resources.models.RawModel;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;

public class EntityResources {

	private static TexturedModel test, plane;
	private static Material testMat, planeMat;

	public static void load(ResourceLoader loader) {
		ModelTexture testText = new ModelTexture(loader.loadTextureEntity("test"));
		RawModel testModel = loader.loadObjModel("test");
		test = new TexturedModel(testModel, testText);
		testMat = new Material(new Vector3f(), 1, 0);

		RawModel planeModel = loader.loadObjModel("plane");
		plane = new TexturedModel(planeModel, testText);
		planeMat = new Material(new Vector3f(), 1, 0);
	}

	public static TexturedModel getTest() {
		return test;
	}

	public static Material getTestMat() {
		return testMat;
	}

	public static TexturedModel getPlane() {
		return plane;
	}

	public static Material getPlaneMat() {
		return planeMat;
	}

}
