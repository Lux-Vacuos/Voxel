package com.hackhalo2.nbt.tags;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hackhalo2.nbt.INamedTagContainer;
import com.hackhalo2.nbt.ITag;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.exceptions.NBTTagNotFoundException;
import com.hackhalo2.nbt.exceptions.UnexpectedTagTypeException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public final class TagCompound extends AbstractTag implements INamedTagContainer {

	private Map<String, ITag> tags;
	
	public TagCompound(String name) {
		super(name, TagType.COMPOUND.getID());
		
		this.tags = new HashMap<>();
	}

	public TagCompound(NBTInputStream in, boolean anonymous) throws NBTException, IOException {
		super(in, anonymous, TagType.COMPOUND.getID());
		
		this.tags = new HashMap<>();
		
		do {
			byte type = in.readByte();
			
			TagType tagType = TagType.valueOf(type);
			
			if(tagType == null)
				throw new NBTException("Invalid type ID '" + type + "'");
			
			if(tagType == TagType.END) break;
			
			this.addTag(in.readNBTTag(tagType, false));
		} while(true);
	}

	@Override
	public void removeTag(ITag tag) throws NBTException {
		if(tag == null)
			throw new NBTException("Tag cannot be null!");
		
		this.removeTag(tag.getName());
	}

	@Override
	public void addTag(ITag tag) throws NBTException {
		if(tag == null)
			throw new NBTException("Tag cannot be null!");
		
		if(this.tags.containsKey(tag.getName()))
			this.tags.get(tag.getName()).setParent(null);
		
		//Remove the tag's old parent if it has one and it isn't this
		if(tag.hasParent() && tag.getParent() != this)
			tag.getParent().removeTag(tag);
		
		tag.setParent(this);
		
		this.tags.put(tag.getName(), tag);
		
	}

	@Override
	public void removeTag(String tag) throws NBTException {
		if(tag == null)
			throw new NBTException("Tag Name cannot be null!");
		
		if(this.tags.containsKey(tag)) this.tags.remove(tag);
		
	}

	@Override
	public ITag getTag(String name) throws NBTException {
		if(name == null)
			throw new NBTException("Tag Name cannot be null!");
		
		return this.tags.get(name);
	}

	@Override
	public Map<String, ITag> getTags() {
		return Collections.unmodifiableMap(this.tags);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ITag> T getTag(String name, Class<T> tagClass)
			throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		
		ITag tag = this.getTag(name);
		
		if(tag == null)
			throw new NBTTagNotFoundException("No entry of '"+name+"' in this Compound Tag!");
		
		if (!tagClass.isInstance(tag))
			throw new UnexpectedTagTypeException("The entry '"+name
					+"' should be '"+tagClass.getSimpleName()
					+"', but is '"+tag.getClass().getSimpleName()+"'");
		
		return ((T)tag);
	}
	
	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		for(Map.Entry<String, ITag> entry : this.tags.entrySet()) {
			out.writeByte(entry.getValue().getID());
			entry.getValue().writeNBT(out, false);
		}
		
		out.writeByte(TagType.END.getID());
	}
	
	//Helper Functions
	
	public boolean hasTagByName(String name) {
		return this.tags.containsKey(name);
	}
	
	public byte getByte(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagByte.class).getValue();
	}
	
	public short getShort(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagShort.class).getValue();
	}
	
	public int getInt(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagInt.class).getValue();
	}
	
	public long getLong(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagLong.class).getValue();
	}
	
	public float getFloat(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagFloat.class).getValue();
	}
	
	public double getDouble(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagDouble.class).getValue();
	}
	
	public byte[] getByteArray(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagByteArray.class).getValue();
	}
	
	public int[] getIntArray(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagIntArray.class).getValue();
	}
	
	public long[] getLongArray(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagLongArray.class).getValue();
	}
	
	public String getString(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagString.class).getValue();
	}
	
	public <T extends ITag> List<T> getList(String name, Class<T> itemClass) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagList.class).getTags(itemClass);
	}
	
	public TagCompound getCompound(String name) throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		return this.getTag(name, TagCompound.class);
	}
}
