package com.hackhalo2.nbt.stream;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

import com.hackhalo2.nbt.ITag;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.*;

public class NBTInputStream extends DataInputStream {
	
	public NBTInputStream(BufferedInputStream in) {
		super(in);
	}
	
	public ITag readNBTTag() throws NBTException, IOException {
		final byte type = this.readByte();
		
		TagType tagType = TagType.valueOf(type);
		
		if(tagType == null) throw new NBTException("Invalid NBT tag: Unknown type '"+type+"'.");
		
		if(tagType == TagType.END) return null;
		
		return this.readNBTTag(tagType, false);
	}
	
	public ITag readNBTTag(TagType type, boolean anonymous) throws NBTException {
		Constructor<? extends ITag> constructor = null;
		
		try {
			constructor = type.getTagClass().getConstructor(NBTInputStream.class, boolean.class);
			return constructor.newInstance(this, anonymous);
		} catch(NoSuchMethodException e) {
			throw new NBTException("Invalid NBT Implementation! Missing deserialization constructor for Tag "+type.name(), e);
		} catch(Exception e) {
			throw new NBTException("Unknown NBT Error! Tag "+type.name()+" threw an error while deserializing!", e);
		}
	}

}
