package com.hackhalo2.nbt.tags;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public final class TagShort extends AbstractTag {
	
	private short value;

	public TagShort(String name, short value) {
		super(name, TagType.SHORT.getID());
		this.value = value;
	}
	
	public TagShort(NBTInputStream in, boolean anonymous) throws NBTException, IOException {
		 super(in, anonymous, TagType.SHORT.getID());
		 
		 this.value = in.readShort();
	}
	
	public short getValue() {
		return this.value;
	}
	
	public void setValue(short b) {
		this.value = b;
	}

	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeShort(this.value);
	}

}
