package net.luxvacuos.voxel.client.core.exception;

public class CompileShaderException extends Exception {

	private static final long serialVersionUID = -8459235864100073938L;

	public CompileShaderException() {
		super();
	}

	public CompileShaderException(String error) {
		super(error);
	}

	public CompileShaderException(Exception e) {
		super(e);
	}

	public CompileShaderException(Throwable cause) {
		super(cause);
	}

	public CompileShaderException(String message, Throwable cause) {
		super(message, cause);
	}

}
