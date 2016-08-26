package com.hackhalo2.nbt.exceptions;

public class UnexpectedTagTypeException extends NBTException {
	private static final long serialVersionUID = -9039018397736879516L;

	public UnexpectedTagTypeException() {
		super("The tag is not of the expected type");
	}

	public UnexpectedTagTypeException(String message) {
		super(message);
	}

	public UnexpectedTagTypeException(Throwable cause) {
		super(cause);
	}

	public UnexpectedTagTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}
