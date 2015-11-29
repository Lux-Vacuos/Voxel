package net.guerra24.voxel.client.api;

public class VersionException extends Exception {
	private static final long serialVersionUID = 1536541208749543634L;

	public VersionException() {
		super();
	}

	public VersionException(String message) {
		super(message);
	}

	public VersionException(String message, Throwable cause) {
		super(message, cause);
	}

	public VersionException(Throwable cause) {
		super(cause);
	}
}
