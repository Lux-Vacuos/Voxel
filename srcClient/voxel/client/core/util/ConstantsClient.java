package voxel.client.core.util;

import java.util.Random;

public class ConstantsClient {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final float ASPECT = 1.777777777777778f;
	public static final float FOV = 90f;

	public static Random rand = new Random();

	public static int viewDistance = 12;
	public static int CHUNKSIZE = 16;
	public static int CHUNKHEIGTH = 16;

	public static int chunksLoaded = 0;
	public static int chunksFrustum = 0;

	public static float textSize = 0.35f;

	public static String title = "Voxel ";
	public static String version = "0.0.5a";
}
