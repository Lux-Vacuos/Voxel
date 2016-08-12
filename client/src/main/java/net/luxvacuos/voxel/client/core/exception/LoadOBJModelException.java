package net.luxvacuos.voxel.client.core.exception;

public class LoadOBJModelException extends RuntimeException {

	private static final long serialVersionUID = 6202284750530615822L;

	public LoadOBJModelException() {
		super();
	}

	public LoadOBJModelException(String error) {
		super(error);
	}

	public LoadOBJModelException(Exception e) {
		super(e);
	}

	public LoadOBJModelException(Throwable cause) {
		super(cause);
	}

	public LoadOBJModelException(String message, Throwable cause) {
		super(message, cause);
	}

}
