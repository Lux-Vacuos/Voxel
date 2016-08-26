package com.hackhalo2.nbt;

import java.io.IOException;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public interface ITag {
	
	public String getName();
	
	public byte[] getNameAsBytes();
	
	public void setName(String name);
	
	public byte getID();
	
	public ITagContainer getParent();
	
	public void setParent(ITagContainer parent);
	
	public boolean hasParent();
	
	public void writeNBT(NBTOutputStream out, boolean anonymous) throws NBTException, IOException;

}
