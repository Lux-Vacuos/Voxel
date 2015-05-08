package net.guerra24.voxel.client.engine.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.entities.types.Player;
import net.guerra24.voxel.client.engine.render.MasterRenderer;
import net.guerra24.voxel.client.engine.render.shaders.types.WaterShader;
import net.guerra24.voxel.client.engine.render.textures.types.GuiTexture;
import net.guerra24.voxel.client.engine.render.types.GuiRenderer;
import net.guerra24.voxel.client.engine.render.types.WaterRenderer;
import net.guerra24.voxel.client.engine.resources.models.WaterTile;
import net.guerra24.voxel.client.engine.util.WaterFrameBuffers;

public class GameResources {
	public static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public static List<GuiTexture> guis2 = new ArrayList<GuiTexture>();
	public static List<GuiTexture> guis3 = new ArrayList<GuiTexture>();
	public static List<GuiTexture> guis4 = new ArrayList<GuiTexture>();
	public static List<GuiTexture> guis5 = new ArrayList<GuiTexture>();

	public static List<Entity> allObjects = new ArrayList<Entity>();
	public static List<Entity> allEntities = new ArrayList<Entity>();
	public static List<Light> lights = new ArrayList<Light>();
	public static List<WaterTile> waters = new ArrayList<WaterTile>();

	public static Random rand;
	public static Player player;
	public static Light sun;
	public static Light spot;
	public static Loader loader;
	public static Camera camera;
	public static MasterRenderer renderer;
	public static WaterShader waterShader;
	public static WaterRenderer waterRenderer;
	public static GuiRenderer guiRenderer;
	public static WaterFrameBuffers fbos;

	public static void init() {
		rand = new Random();
		camera = new Camera();

		loader = new Loader();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix());
		fbos = new WaterFrameBuffers();
	}
	
	public static void cleanUp() {
		waterShader.cleanUp();
		fbos.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
	}
}
