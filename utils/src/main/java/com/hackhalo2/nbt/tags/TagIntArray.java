package com.hackhalo2.nbt.tags;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public final class TagIntArray extends AbstractTag {
	
	private int[] value;

	public TagIntArray(String name, int[] value) {
		super(name, TagType.INT_ARRAY.getID());
		this.value = value;
	}

	public TagIntArray(NBTInputStream in, boolean anonymous)
			throws NBTException, IOException {
		super(in, anonymous, TagType.INT_ARRAY.getID());
		
		final int size = in.readInt();
		
		int[] data = new int[size];
		
		for(int i = 0; i < size; i++)
			data[i] = in.readInt();
		
		this.value = data;
	}
	
	
	public void setValue(int[] value) {
		this.value = value;
	}
	
	public int[] getValue() {
		return this.value;
	}
	
	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeInt(this.value.length);
		
		for(int i : this.value)
			out.writeInt(i);
	}
}
