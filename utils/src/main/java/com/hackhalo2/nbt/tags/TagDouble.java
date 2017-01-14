package com.hackhalo2.nbt.tags;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public final class TagDouble extends AbstractTag {
	private double value;

	public TagDouble(String name, double value) {
		super(name, TagType.DOUBLE.getID());
		this.value = value;
	}
	
	public TagDouble(NBTInputStream in, boolean anonymous) throws NBTException, IOException {
		 super(in, anonymous, TagType.DOUBLE.getID());
		 
		 this.value = in.readDouble();
	}
	
	public double getValue() {
		return this.value;
	}
	
	public void setValue(double b) {
		this.value = b;
	}

	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeDouble(this.value);
	}
}
