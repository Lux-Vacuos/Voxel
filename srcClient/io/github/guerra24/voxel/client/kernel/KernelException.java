package io.github.guerra24.voxel.client.kernel;

import io.github.guerra24.voxel.client.kernel.util.Logger;

public class KernelException extends Exception {
	private static final long serialVersionUID = -3801999795158799019L;

	public KernelException() {
	}

	public KernelException(String message) {
		super(message);
		Kernel.gameResources.guiRenderer.render(Kernel.gameResources.guis5);
		DisplayManager.updateDisplay();
		Logger.log(Kernel.currentThread(), "Closing Game");
		Kernel.gameResources.cleanUp();
		DisplayManager.closeDisplay();
	}

}
