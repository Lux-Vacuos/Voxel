package net.luxvacuos.voxel.client.core.exception;

import net.luxvacuos.igl.Logger;

public class LoadTextureException extends Exception {

	private static final long serialVersionUID = 1967156739405761095L;

	public LoadTextureException(String texture, Exception cause) {
		super(cause);
		Logger.error("Couldn' load texture file" + texture);
	}

}
