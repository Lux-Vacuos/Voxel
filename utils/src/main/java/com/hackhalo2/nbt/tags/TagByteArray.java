package com.hackhalo2.nbt.tags;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public final class TagByteArray extends AbstractTag {
	private byte[] value;

	public TagByteArray(String name, byte[] value) {
		super(name, TagType.BYTE_ARRAY.getID());
		this.value = value;
	}

	public TagByteArray(NBTInputStream in, boolean anonymous)
			throws NBTException, IOException {
		super(in, anonymous, TagType.BYTE_ARRAY.getID());
		
		final int size = in.readInt();
		
		byte[] data = new byte[size];
		in.readFully(data);
		
		this.value = data;
	}
	
	public byte[] getValue() {
		return this.value;
	}
	
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeInt(this.value.length);
		out.write(this.value);
	}

}
