package com.hackhalo2.nbt.tags;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public class TagLongArray extends AbstractTag {
	
	private long[] value;

	public TagLongArray(String name, long[] value) {
		super(name, TagType.LONG_ARRAY.getID());
		this.value = value;
	}

	public TagLongArray(NBTInputStream in, boolean anonymous, byte id) throws NBTException, IOException {
		super(in, anonymous, TagType.LONG_ARRAY.getID());
		
		final int size = in.readInt();
		
		long[] data = new long[size];
		
		for(int i = 0; i < size; i++)
			data[i] = in.readLong();
		
		this.value = data;
	}
	
	public void setValue(long[] value) {
		this.value = value;
	}
	
	public long[] getValue() {
		return this.value;
	}
	
	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeInt(this.value.length);
		
		for(long i : this.value)
			out.writeLong(i);
	}

}
