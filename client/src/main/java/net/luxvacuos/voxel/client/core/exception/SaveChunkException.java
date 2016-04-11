package net.luxvacuos.voxel.client.core.exception;

public class SaveChunkException extends Exception {

	private static final long serialVersionUID = 6232402839094408468L;

	public SaveChunkException() {
		super();
	}

	public SaveChunkException(String error) {
		super(error);
	}

	public SaveChunkException(Exception e) {
		super(e);
	}

	public SaveChunkException(Throwable cause) {
		super(cause);
	}

	public SaveChunkException(String message, Throwable cause) {
		super(message, cause);
	}

}
