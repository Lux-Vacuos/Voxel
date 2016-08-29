package net.luxvacuos.voxel.universal.core.exception;

public class CompileGroovyException extends RuntimeException {

	private static final long serialVersionUID = 4761096455473153214L;

	public CompileGroovyException() {
		super();
	}

	public CompileGroovyException(String error) {
		super(error);
	}

	public CompileGroovyException(Exception e) {
		super(e);
	}

	public CompileGroovyException(Throwable cause) {
		super(cause);
	}

	public CompileGroovyException(String message, Throwable cause) {
		super(message, cause);
	}

}
