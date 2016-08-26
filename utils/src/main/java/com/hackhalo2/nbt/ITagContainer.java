package com.hackhalo2.nbt;

import com.hackhalo2.nbt.exceptions.NBTException;

public interface ITagContainer extends ITag {
	
	public void removeTag (ITag tag) throws NBTException;

}
