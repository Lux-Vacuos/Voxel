package com.hackhalo2.nbt.tags;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public final class TagByte extends AbstractTag {
	
	private byte value;

	public TagByte(String name, byte value) {
		super(name, TagType.BYTE.getID());
		this.value = value;
	}
	
	public TagByte(NBTInputStream in, boolean anonymous) throws NBTException, IOException {
		 super(in, anonymous, TagType.BYTE.getID());
		 
		 this.value = in.readByte();
	}
	
	public byte getValue() {
		return this.value;
	}
	
	public void setValue(byte b) {
		this.value = b;
	}

	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeByte(this.value);
	}

}
