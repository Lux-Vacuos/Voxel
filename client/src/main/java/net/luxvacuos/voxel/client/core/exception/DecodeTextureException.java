package net.luxvacuos.voxel.client.core.exception;

public class DecodeTextureException extends RuntimeException {

	private static final long serialVersionUID = -8629683316677759946L;

	public DecodeTextureException(String e) {
		super(e);
	}

	public DecodeTextureException(Exception e) {
		super(e);
	}

}
