package com.hackhalo2.nbt;

import java.util.Map;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.exceptions.NBTTagNotFoundException;
import com.hackhalo2.nbt.exceptions.UnexpectedTagTypeException;

public interface INamedTagContainer extends ITagContainer {
	
	public void addTag(ITag tag) throws NBTException;
	
	public void removeTag(String tag) throws NBTException;
	
	public ITag getTag(String name) throws NBTException;
	
	public <T extends ITag> T getTag(String name, Class<T> tagClass) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException;
	
	public Map<String, ITag> getTags();

}
