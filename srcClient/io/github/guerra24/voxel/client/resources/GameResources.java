package io.github.guerra24.voxel.client.resources;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;
import io.github.guerra24.voxel.client.kernel.GameStates;
import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.render.MasterRenderer;
import io.github.guerra24.voxel.client.kernel.render.shaders.types.WaterShader;
import io.github.guerra24.voxel.client.kernel.render.textures.GuiTexture;
import io.github.guerra24.voxel.client.kernel.render.types.GuiRenderer;
import io.github.guerra24.voxel.client.kernel.render.types.WaterRenderer;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.kernel.util.MousePicker;
import io.github.guerra24.voxel.client.kernel.util.WaterFrameBuffers;
import io.github.guerra24.voxel.client.resources.models.WaterTile;
import io.github.guerra24.voxel.client.world.block.BlocksResources;
import io.github.guerra24.voxel.client.world.entities.Entity;
import io.github.guerra24.voxel.client.world.entities.types.Camera;
import io.github.guerra24.voxel.client.world.entities.types.Light;
import io.github.guerra24.voxel.client.world.entities.types.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	public WaterFrameBuffers fbos2;
	public GameStates gameStates;
	public MousePicker mouse;
	public Gson gson;
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

		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			Logger.log(Kernel.currentThread(), "Unable to bind SounSystem Libs");
		}
		SoundSystem = new SoundSystem();
		renderer = new MasterRenderer(loader);
		waterShader = new WaterShader();
		fbos = new WaterFrameBuffers();
		fbos2 = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix(), fbos, fbos2);
		mouse = new MousePicker(camera, renderer.getProjectionMatrix());
		gameStates = new GameStates();
	}

	public void localLoop() {
		distance = 2 * (camera.getPosition().y - 64.4f);
		spot.setPosition(player.getPosition());
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
		player = new Player(BlocksResources.cubeGlass,
				new Vector3f(10, 80, 10), 0, 0, 90, 1);
		sun = new Light(new Vector3f(-7000, 0f, -7000),
				new Vector3f(1f, 1f, 1f));
		spot = new Light(new Vector3f(16, 64, 16), new Vector3f(1, 1, 1),
				new Vector3f(1, 0.01f, 0.002f));
		lights.add(spot);
		lights.add(sun);
		allObjects.add(player);
		plane = new Vector4f(0, -1, 0, 128 + 16);
	}

	public void cleanUp() {
		waterShader.cleanUp();
		fbos.cleanUp();
		fbos2.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		SoundSystem.cleanup();
	}
}
