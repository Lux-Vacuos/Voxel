package net.luxvacuos.voxel.client.core.exception;

public class LoadChunkException extends Exception {

	private static final long serialVersionUID = -4872686166091646688L;

	public LoadChunkException() {
		super();
	}

	public LoadChunkException(String error) {
		super(error);
	}

	public LoadChunkException(Exception e) {
		super(e);
	}

	public LoadChunkException(Throwable cause) {
		super(cause);
	}

	public LoadChunkException(String message, Throwable cause) {
		super(message, cause);
	}

}
