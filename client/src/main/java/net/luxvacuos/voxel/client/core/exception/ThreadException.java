package net.luxvacuos.voxel.client.core.exception;

public class ThreadException extends Exception {

	private static final long serialVersionUID = 4013981199790877927L;

	public ThreadException() {
		super();
	}

	public ThreadException(String error) {
		super(error);
	}

	public ThreadException(Exception e) {
		super(e);
	}

	public ThreadException(Throwable cause) {
		super(cause);
	}

	public ThreadException(String message, Throwable cause) {
		super(message, cause);
	}

}
