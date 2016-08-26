package com.hackhalo2.nbt.tags;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public final class TagInt extends AbstractTag {
	private int value;

	public TagInt(String name, int value) {
		super(name, TagType.INT.getID());
		this.value = value;
	}
	
	public TagInt(NBTInputStream in, boolean anonymous) throws NBTException, IOException {
		 super(in, anonymous, TagType.INT.getID());
		 
		 this.value = in.readInt();
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int b) {
		this.value = b;
	}

	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeInt(this.value);
	}
}
