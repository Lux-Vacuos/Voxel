package com.hackhalo2.nbt.exceptions;

import java.io.IOException;

public class NBTException extends IOException {
	private static final long serialVersionUID = -7334470444037411479L;
	
	public NBTException() {
		super("An unknown NBT Error has occured.");
	}
	
	public NBTException(String message) {
		super(message);
	}
	
	public NBTException(Throwable cause) {
		super(cause);
	}
	
	public NBTException(String message, Throwable cause) {
		super(message, cause);
	}

}
