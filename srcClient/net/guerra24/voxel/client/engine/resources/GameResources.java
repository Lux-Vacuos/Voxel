package net.guerra24.voxel.client.engine.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

import com.google.gson.Gson;

import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.entities.types.Player;
import net.guerra24.voxel.client.engine.network.ID;
import net.guerra24.voxel.client.engine.render.MasterRenderer;
import net.guerra24.voxel.client.engine.render.shaders.types.WaterShader;
import net.guerra24.voxel.client.engine.render.textures.types.GuiTexture;
import net.guerra24.voxel.client.engine.render.types.GuiRenderer;
import net.guerra24.voxel.client.engine.render.types.WaterRenderer;
import net.guerra24.voxel.client.engine.resources.models.WaterTile;
import net.guerra24.voxel.client.engine.util.MousePicker;
import net.guerra24.voxel.client.engine.util.WaterFrameBuffers;
import net.guerra24.voxel.client.engine.world.Blocks;

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
	public static MousePicker mouse;
	public static Gson gson;
	public static ID id;
	public static SoundSystem SoundSystem;

	public static void init() {
		rand = new Random();
		camera = new Camera();
		gson = new Gson();
		loader = new Loader();
		// id = new ID();

		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			throw new RuntimeException("Unable to bind");
		}
		SoundSystem = new SoundSystem();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix());
		fbos = new WaterFrameBuffers();
		mouse = new MousePicker(camera, renderer.getProjectionMatrix());

	}

	public static void music() {
		SoundSystem.backgroundMusic("MainMenuMusic", "Water_Lily.ogg", false);
	}

	public static void addRes() {
		player = new Player(Blocks.cubeGlass, new Vector3f(-10, 68, -10), 0, 0,
				90, 1);
		sun = new Light(new Vector3f(-7000, 0f, -7000),
				new Vector3f(1f, 1f, 1f));
		spot = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0),
				new Vector3f(1, 0.01f, 0.002f));
		// lights.add(spot);
		lights.add(sun);
		allObjects.add(player);
		allEntities.addAll(allObjects);
	}

	public static void cleanUp() {
		waterShader.cleanUp();
		fbos.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		SoundSystem.cleanup();

	}
}
