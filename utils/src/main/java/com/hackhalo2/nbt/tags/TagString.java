package com.hackhalo2.nbt.tags;

import java.io.IOException;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;
import com.hackhalo2.util.IOHandler;

public final class TagString extends AbstractTag {
	
	private String value;

	public TagString(String name, String value) {
		super(name, TagType.STRING.getID());
		this.value = value;
	}

	public TagString(NBTInputStream in, boolean anonymous)
			throws NBTException, IOException {
		super(in, anonymous, TagType.STRING.getID());
		
		int size = in.readShort();
		
		byte[] data = new byte[size];
		in.readFully(data);
		
		this.value = new String(data, IOHandler.UTF8);
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		byte[] outputBytes = this.value.getBytes(IOHandler.UTF8);
		out.writeShort(outputBytes.length);
		
		out.write(outputBytes);
	}

}
