package com.hackhalo2.nbt.exceptions;

public class NBTTagNotFoundException extends NBTException {
	private static final long serialVersionUID = -1342455657033033808L;
	
	public NBTTagNotFoundException() {
		super("The NBT Tag could not be found");
	}
	
	public NBTTagNotFoundException(String message) {
		super(message);
	}
	
	public NBTTagNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public NBTTagNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
