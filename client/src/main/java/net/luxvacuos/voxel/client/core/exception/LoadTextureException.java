package net.luxvacuos.voxel.client.core.exception;

import net.luxvacuos.igl.Logger;

public class LoadTextureException extends RuntimeException {

	private static final long serialVersionUID = 960750507253812538L;

	public LoadTextureException(Exception cause) {
		super(cause);
	}

	public LoadTextureException(String texture, Exception cause) {
		super(cause);
		Logger.error("Couldn' load texture file " + texture);
	}

}
