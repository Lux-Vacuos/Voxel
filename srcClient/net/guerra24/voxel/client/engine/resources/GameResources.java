package net.guerra24.voxel.client.engine.resources;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.guerra24.voxel.client.engine.GameStates;
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
import net.guerra24.voxel.client.engine.world.chunks.Chunk;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

import com.google.gson.Gson;

public class GameResources {
	public List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis2 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis3 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis4 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis5 = new ArrayList<GuiTexture>();

	public List<Entity> allObjects = new ArrayList<Entity>();
	public List<Entity> allEntities = new ArrayList<Entity>();
	public List<Light> lights = new ArrayList<Light>();
	public List<WaterTile> waters = new ArrayList<WaterTile>();

	public Random rand;
	public Player player;
	public Light sun;
	public Light spot;
	public Loader loader;
	public Camera camera;
	public MasterRenderer renderer;
	public WaterShader waterShader;
	public WaterRenderer waterRenderer;
	public GuiRenderer guiRenderer;
	public WaterFrameBuffers fbos;
	public WaterFrameBuffers fbos1;
	public GameStates gameStates;
	public MousePicker mouse;
	public Gson gson;
	public ID id;
	public SoundSystem SoundSystem;
	public Vector4f plane;
	public float distance;

	public GameResources() {
		loader = new Loader();
		guiRenderer = new GuiRenderer(loader);
	}

	public void init() {
		rand = new Random();
		camera = new Camera();
		gson = new Gson();
		// id = new ID();

		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			throw new RuntimeException("Unable to bind");
		}
		SoundSystem = new SoundSystem();
		renderer = new MasterRenderer(loader);
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix());
		fbos = new WaterFrameBuffers();
		fbos1 = new WaterFrameBuffers();
		mouse = new MousePicker(camera, renderer.getProjectionMatrix());
		gameStates = new GameStates();
	}

	public void localLoop() {
		distance = 2 * (camera.getPosition().y - Chunk.water.getHeight());
	}

	public void glEn() {
		glEnable(GL_CLIP_DISTANCE0);
	}

	public void glDi() {
		glDisable(GL_CLIP_DISTANCE0);
	}

	public void music() {
		SoundSystem.backgroundMusic("MainMenuMusic", "Water_Lily.ogg", false);
	}

	public void addRes() {
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
		plane = new Vector4f(0, -1, 0, 128 + Chunk.getyOffset());
	}

	public void cleanUp() {
		waterShader.cleanUp();
		fbos.cleanUp();
		fbos1.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		SoundSystem.cleanup();

	}
}
