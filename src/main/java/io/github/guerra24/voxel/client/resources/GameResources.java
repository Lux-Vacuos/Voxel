/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.resources;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;
import io.github.guerra24.voxel.client.kernel.GameStates;
import io.github.guerra24.voxel.client.kernel.render.GuiRenderer;
import io.github.guerra24.voxel.client.kernel.render.MasterRenderer;
import io.github.guerra24.voxel.client.kernel.render.WaterRenderer;
import io.github.guerra24.voxel.client.kernel.render.shaders.WaterShader;
import io.github.guerra24.voxel.client.kernel.render.textures.GuiTexture;
import io.github.guerra24.voxel.client.kernel.util.ArrayList3;
import io.github.guerra24.voxel.client.kernel.util.MousePicker;
import io.github.guerra24.voxel.client.kernel.util.WaterFrameBuffers;
import io.github.guerra24.voxel.client.resources.models.WaterTile;
import io.github.guerra24.voxel.client.world.block.BlocksResources;
import io.github.guerra24.voxel.client.world.entities.Entity;
import io.github.guerra24.voxel.client.world.entities.types.Camera;
import io.github.guerra24.voxel.client.world.entities.types.Light;
import io.github.guerra24.voxel.client.world.entities.types.Player;

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
	public ArrayList3<GuiTexture> guis = new ArrayList3<GuiTexture>();
	public ArrayList3<GuiTexture> guis2 = new ArrayList3<GuiTexture>();
	public ArrayList3<GuiTexture> guis3 = new ArrayList3<GuiTexture>();
	public ArrayList3<GuiTexture> guis4 = new ArrayList3<GuiTexture>();
	public ArrayList3<GuiTexture> guis5 = new ArrayList3<GuiTexture>();

	public ArrayList3<Entity> allObjects = new ArrayList3<Entity>();
	public ArrayList3<Entity> cubes = new ArrayList3<Entity>();
	public ArrayList3<Light> lights = new ArrayList3<Light>();
	public ArrayList3<WaterTile> waters = new ArrayList3<WaterTile>();

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
	//public SoundSystem SoundSystem;
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
/*
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			Logger.error(Thread.currentThread(),
					"Unable to bind SoundSystem Libs");
		}
		SoundSystem = new SoundSystem();
		*/renderer = new MasterRenderer(loader);
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
		//spot.setPosition(player.getPosition());
	}

	public void glEn() {
		glEnable(GL_CLIP_DISTANCE0);
	}

	public void glDi() {
		glDisable(GL_CLIP_DISTANCE0);
	}

	public void music() {
		//SoundSystem.backgroundMusic("MainMenuMusic", "Water_Lily.ogg", false);
	}

	public void addRes() {
		player = new Player(BlocksResources.cubeGlassUP, new Vector3f(0, 80, -4),
				0, 0, 0, 1);
		sun = new Light(new Vector3f(-7000, 0f, -7000),
				new Vector3f(1f, 1f, 1f));
		spot = new Light(new Vector3f(20, 70, 20), new Vector3f(1, 1, 1),
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
		//SoundSystem.cleanup();
	}
}
