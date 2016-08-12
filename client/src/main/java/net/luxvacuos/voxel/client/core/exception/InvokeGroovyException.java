package net.luxvacuos.voxel.client.core.exception;

public class InvokeGroovyException extends RuntimeException {
	private static final long serialVersionUID = 4272551520344358231L;

	public InvokeGroovyException() {
		super();
	}

	public InvokeGroovyException(String error) {
		super(error);
	}

	public InvokeGroovyException(Exception e) {
		super(e);
	}

	public InvokeGroovyException(Throwable cause) {
		super(cause);
	}

	public InvokeGroovyException(String message, Throwable cause) {
		super(message, cause);
	}
}
