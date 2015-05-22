package net.guerra24.voxel.client.kernel.render.types;

import net.guerra24.voxel.client.kernel.Engine;

public class WaterReflection {
	public static void reflectionCam() {
		Engine.gameResources.localLoop();
		Engine.gameResources.camera.getPosition().y -= Engine.gameResources.distance;
		Engine.gameResources.camera.invertPitch();
	}

	public static void restoreCam() {
		Engine.gameResources.camera.getPosition().y += Engine.gameResources.distance;
		Engine.gameResources.camera.invertPitch();
	}
}
