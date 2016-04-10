package net.luxvacuos.voxel.client.core.exception;

public class LoadShaderException extends Exception {

	private static final long serialVersionUID = -5645376169749026843L;

	public LoadShaderException() {
		super();
	}

	public LoadShaderException(String error) {
		super(error);
	}

	public LoadShaderException(Exception e) {
		super(e);
	}

	public LoadShaderException(Throwable cause) {
		super(cause);
	}

	public LoadShaderException(String message, Throwable cause) {
		super(message, cause);
	}

}
