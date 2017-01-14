package com.hackhalo2.nbt.tags;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public final class TagLong extends AbstractTag {
	private long value;

	public TagLong(String name, long value) {
		super(name, TagType.LONG.getID());
		this.value = value;
	}
	
	public TagLong(NBTInputStream in, boolean anonymous) throws NBTException, IOException {
		 super(in, anonymous, TagType.LONG.getID());
		 
		 this.value = in.readLong();
	}
	
	public long getValue() {
		return this.value;
	}
	
	public void setValue(long b) {
		this.value = b;
	}

	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeLong(this.value);
	}
}
