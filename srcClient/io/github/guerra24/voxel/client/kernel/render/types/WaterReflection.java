package io.github.guerra24.voxel.client.kernel.render.types;

import io.github.guerra24.voxel.client.kernel.Kernel;

public class WaterReflection {
	public static void reflectionCam() {
		Kernel.gameResources.localLoop();
		Kernel.gameResources.camera.getPosition().y -= Kernel.gameResources.distance;
		Kernel.gameResources.camera.invertPitch();
	}

	public static void restoreCam() {
		Kernel.gameResources.camera.getPosition().y += Kernel.gameResources.distance;
		Kernel.gameResources.camera.invertPitch();
	}
}
