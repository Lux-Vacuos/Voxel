package net.guerra24.voxel.client.kernel;

import net.guerra24.voxel.client.kernel.util.Logger;

public class EngineException extends Exception {
	private static final long serialVersionUID = -3801999795158799019L;

	public EngineException() {
	}

	public EngineException(String message) {
		super(message);
		Engine.gameResources.guiRenderer.render(Engine.gameResources.guis5);
		DisplayManager.updateDisplay();
		Logger.log("Closing Game");
		Engine.gameResources.cleanUp();
		DisplayManager.closeDisplay();
	}

}
