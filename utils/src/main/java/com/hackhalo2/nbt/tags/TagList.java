package com.hackhalo2.nbt.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hackhalo2.nbt.IAnonymousTagContainer;
import com.hackhalo2.nbt.ITag;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.exceptions.UnexpectedTagTypeException;
import com.hackhalo2.nbt.stream.NBTInputStream;
import com.hackhalo2.nbt.stream.NBTOutputStream;

public class TagList extends AbstractTag implements IAnonymousTagContainer {
	
	private List<ITag> tags;
	private TagType listType = TagType.END;

	public TagList(String name) {
		super(name, TagType.LIST.getID());
		this.tags = new ArrayList<>();
	}
	
	public TagList(String name, List<ITag> value) {
		super(name, TagType.LIST.getID());
		
		if(value == null) this.tags = new ArrayList<>();
		else {
			this.tags = new ArrayList<>(value);
			this.listType = TagType.valueOf(value.get(0).getID());
		}
	}

	public TagList(NBTInputStream in, boolean anonymous, byte id)
			throws NBTException, IOException {
		super(in, anonymous, TagType.LIST.getID());
		
		this.tags = new ArrayList<>();
		
		byte type = in.readByte();
		this.listType = TagType.valueOf(type);
		
		int size = in.readInt();
		
		if(this.listType == TagType.END) return;
		
		for (int i = 0; i < size; i++) {
			this.addTag(in.readNBTTag(this.listType, true));
		}
	}

	@Override
	public void removeTag(ITag tag) throws NBTException {
		if(tag == null) throw new NBTException("Tag Cannot be null!");
		
		if(this.listType != TagType.valueOf(tag.getID()))
			throw new NBTException("Invalid TagType for this List! Tried to remove a tag with TagType "+TagType.valueOf(tag.getID()).name()+" from a List of "+this.listType.name());
		
		if(this.tags.contains(tag))
			this.tags.remove(tag);
		else
			System.err.println("Tag "+tag.getName()+" not found in list, ignoring remove...");
		
		if(this.tags.size() == 0) this.listType = TagType.END;
	}

	@Override
	public void addTag(ITag tag) throws NBTException {
		if(tag == null) throw new NBTException("Tag Cannot be null!");
		
		TagType temp = TagType.valueOf(tag.getID());
		
		if(this.listType == TagType.END)
			this.listType = temp;
		else if(this.listType != temp)
			throw new NBTException("Invalid TagType for this List! Tried to add TagType "+temp.name()+" to a List of "+this.listType.name());
		
		this.tags.add(tag);
	}

	@Override
	public List<ITag> getTags() {
		if(this.listType == TagType.END)
			return null;
		
		return Collections.unmodifiableList(this.tags);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends ITag> List<T> getTags(Class<T> tagClass) throws UnexpectedTagTypeException {
		List<T> builder = new ArrayList<T>();
		for(ITag tag : this.tags) {
			if(!tagClass.isInstance(tag))
				throw new UnexpectedTagTypeException(
						"The entry should be '"+ tagClass.getSimpleName()
								+ "', but is '"+ tag.getClass().getSimpleName()+"'");
			builder.add((T)tag);
		}
		return Collections.unmodifiableList(builder);
	}

	@Override
	public void setTag(int i, ITag tag) throws NBTException, ArrayIndexOutOfBoundsException {
		if(tag == null)
			throw new NBTException("Tag Cannot be null!");
		else if(TagType.valueOf(tag.getID()) != this.listType)
			throw new NBTException("Invalid TagType for this List! Tried to modify a Tag at point "+i+" with TagType "+TagType.valueOf(tag.getID()).name()+" to a List of "+this.listType.name());
		
		this.tags.set(i, tag);
	}
	
	@Override
	public void writeNBT(NBTOutputStream out, boolean anonymous)
			throws NBTException, IOException {
		super.writeNBT(out, anonymous);
		
		out.writeByte(this.listType.getID());
		out.writeInt(this.tags.size());
		
		for(ITag tag : this.tags)
			tag.writeNBT(out, true);
	}

}
