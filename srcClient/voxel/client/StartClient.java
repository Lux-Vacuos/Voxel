package voxel.client;

import org.lwjgl.opengl.Display;

import voxel.client.engine.DisplayManager;
import voxel.client.engine.util.Logger;

public class StartClient {

	public static void StartGame() {
		DisplayManager.createDisplay();

		while (!Display.isCloseRequested()) {
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		while (!Display.isCloseRequested()) {
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		DisplayManager.closeDisplay();
	}
}
