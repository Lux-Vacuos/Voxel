package net.voxel.core.world;

import net.logger.Logger;
import net.voxel.core.Main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.nishu.utils.Camera;
import com.nishu.utils.Camera3D;
import com.nishu.utils.Screen;

public class World extends Screen {	
	private Camera camera;
	
	public World() {
		initGL();
		init();
	}

	@Override
	public void init() {
		Logger.log("Initializing Camera");
		camera = new Camera3D.CameraBuilder().setAspectRatio(Main.aspect).setRotation(0, 0, 0).setPosition(0, 0, 0).setFieldOfView(Main.fov).build();
	}

	@Override
	public void initGL() {
	}

	@Override
	public void render() {
	}

	@Override
	public void update() {
		camera.updateKeys(32, 1);
		camera.updateMouse(1, 90, -90);
		if(Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		}else if(Mouse.isButtonDown(1)) {
			Mouse.setGrabbed(false);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			dispose();
		}
	}
	@Override
	public void dispose() {
		Logger.log("Sistem closed");
		Display.destroy();
		System.exit(0);
	}
}