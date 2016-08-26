package com.hackhalo2.nbt;

import java.util.List;

import com.hackhalo2.nbt.exceptions.NBTException;

public interface IAnonymousTagContainer extends ITagContainer {
	
	public void addTag(ITag tag) throws NBTException;
	
	public List<ITag> getTags();
	
	public void setTag(int i, ITag tag) throws NBTException, ArrayIndexOutOfBoundsException;
}
