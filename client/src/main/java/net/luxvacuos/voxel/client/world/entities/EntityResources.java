package net.luxvacuos.voxel.client.world.entities;

import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Texture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.client.resources.ResourceLoader;

public class EntityResources {

	private static TexturedModel test, plane, dragon, soccerBall;
	private static Material testMat, planeMat, dragonMat, soccerBallMat;

	public static void load(ResourceLoader loader) {

		Texture planeNormal = new Texture(loader.loadTextureEntityMisc("test_norm"));

		RawModel testModel = loader.loadObjModel("test");
		testMat = new Material(new Vector4f(0.8f, 0.8f, 0.8f, 1), 0.05f, 0f, 0.0f, new Texture(0), planeNormal);
		test = new TexturedModel(testModel, testMat);

		RawModel planeModel = loader.loadObjModel("plane");
		planeMat = new Material(new Vector4f(-1), 0.6f, 0, 0.0f, new Texture(loader.loadTextureEntity("plane")),
				new Texture(loader.loadTextureEntityMisc("plane_norm")));
		plane = new TexturedModel(planeModel, planeMat);

		RawModel dragonModel = loader.loadObjModel("stanford");
		dragonMat = new Material(new Vector4f(1.000f, 0.766f, 0.336f, 1), 0.1f, 0.6f, 0.0f, new Texture(0),
				planeNormal);
		dragon = new TexturedModel(dragonModel, dragonMat);

		RawModel soccerBall = loader.loadObjModel("soccer");
		soccerBallMat = new Material(new Vector4f(-1), 0.1f, 0.0f, 0.05f, new Texture(loader.loadTextureEntity("ball")),
				new Texture(loader.loadTextureEntityMisc("ball_norm")));
		EntityResources.soccerBall = new TexturedModel(soccerBall, soccerBallMat);
	}

	public static TexturedModel getTest() {
		return test;
	}

	public static TexturedModel getPlane() {
		return plane;
	}

	public static TexturedModel getDragon() {
		return dragon;
	}

	public static TexturedModel getSoccerBall() {
		return soccerBall;
	}

}
