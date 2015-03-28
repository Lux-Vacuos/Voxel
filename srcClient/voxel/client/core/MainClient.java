package voxel.client.core;

import org.lwjgl.Sys;

import voxel.client.core.engine.GameLoop;
import voxel.client.core.engine.Screen;
import voxel.client.core.engine.Window;
import voxel.client.core.engine.util.Logger;

public class MainClient extends Screen {

	private GameLoop gameLoop;

	public MainClient() {
		gameLoop = new GameLoop();
		gameLoop.setScreen(this);
		gameLoop.start(30);
	}

	@Override
	public void init() {
		Window.createWindow(1280, 720, "Voxel", false);
		
	}

	@Override
	public void initGL() {
		Logger.log("LWJGL Version: " + Sys.getVersion());
		// GLCaps.checkGLCaps();
	}

	@Override
	public void update() {
	}

	@Override
	public void render() {
	}

	@Override
	public void dispose() {
		Logger.log("Disposing");
	}

	public static void LaunchVoxel() {
		new MainClient();
	}

	public static void main(String[] args) {
		new MainClient();
	}
}
