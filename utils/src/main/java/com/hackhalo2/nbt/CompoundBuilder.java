package com.hackhalo2.nbt;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagByte;
import com.hackhalo2.nbt.tags.TagByteArray;
import com.hackhalo2.nbt.tags.TagCompound;
import com.hackhalo2.nbt.tags.TagDouble;
import com.hackhalo2.nbt.tags.TagInt;
import com.hackhalo2.nbt.tags.TagIntArray;
import com.hackhalo2.nbt.tags.TagList;
import com.hackhalo2.nbt.tags.TagLong;
import com.hackhalo2.nbt.tags.TagShort;
import com.hackhalo2.nbt.tags.TagString;

public class CompoundBuilder {
	private TagCompound compound = null;

	public CompoundBuilder() { }

	/* Initializers */

	//Start a new unnamed Compound
	public CompoundBuilder start() {
		if(this.compound != null) throw new IllegalStateException("The Compound has been initialized already!");

		this.compound = new TagCompound("");

		return this;
	}

	//Start a new named Compound
	public CompoundBuilder start(String name) {
		if(this.compound != null) throw new IllegalStateException("The Compound has been initialized already!");

		this.compound = new TagCompound(name);

		return this;
	}

	//Modify an existing Compound
	public CompoundBuilder modify(TagCompound tag) {
		if(this.compound != null) throw new IllegalStateException("The Compound has been initialized already!");

		this.compound = tag;

		return this;
	}

	/* Mutators */

	//Add a Boolean to the Compound, referenced as a byte
	public CompoundBuilder addBoolean(String name, boolean value) {
		this.checkIfInitialized();

		try {
			TagByte tag = new TagByte(name, (byte)(value ? 1 : 0));
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing Boolean (which is a Byte) in the compound by name
	public CompoundBuilder modifyBoolean(String name, boolean value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagByte tag = this.compound.getTag(name, TagByte.class);
				tag.setValue((byte)(value ? 1 : 0));
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a Byte tag to the Compound
	public CompoundBuilder addByte(String name, byte value) {
		this.checkIfInitialized();

		try {
			TagByte tag = new TagByte(name, value);
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing Byte in the compound by name
	public CompoundBuilder modifyByte(String name, byte value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagByte tag = this.compound.getTag(name, TagByte.class);
				tag.setValue(value);
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a Short Tag to the Compound
	public CompoundBuilder addShort(String name, short value) {
		this.checkIfInitialized();

		try {
			TagShort tag = new TagShort(name, value);
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing Short in the compound by name
	public CompoundBuilder modifyShort(String name, short value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagShort tag = this.compound.getTag(name, TagShort.class);
				tag.setValue(value);
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add an Integer Tag to the Compound
	public CompoundBuilder addInteger(String name, int value) {
		this.checkIfInitialized();

		try {
			TagInt tag = new TagInt(name, value);
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing Integer in the compound by name
	public CompoundBuilder modifyInteger(String name, int value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagInt tag = this.compound.getTag(name, TagInt.class);
				tag.setValue(value);
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a Long Tag to the Compound
	public CompoundBuilder addLong(String name, long value) {
		this.checkIfInitialized();

		try {
			TagLong tag = new TagLong(name, value);
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing Long in the compound by name
	public CompoundBuilder modifyLong(String name, long value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagLong tag = this.compound.getTag(name, TagLong.class);
				tag.setValue(value);
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a Double Tag to the Compound
	public CompoundBuilder addDouble(String name, double value) {
		this.checkIfInitialized();

		try {
			TagDouble tag = new TagDouble(name, value);
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing Double in the compound by name
	public CompoundBuilder modifyDouble(String name, double value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagDouble tag = this.compound.getTag(name, TagDouble.class);
				tag.setValue(value);
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a String Tag to the Compound
	public CompoundBuilder addString(String name, String value) {
		this.checkIfInitialized();

		try {
			TagString tag = new TagString(name, value);
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing String in the compound by name
	public CompoundBuilder modifyString(String name, String value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagString tag = this.compound.getTag(name, TagString.class);
				tag.setValue(value);
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a Byte Array Tag to the Compound
	public CompoundBuilder addByteArray(String name, byte[] value) {
		this.checkIfInitialized();

		try {
			TagByteArray tag = new TagByteArray(name, value);
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing Byte Array in the compound by name
	public CompoundBuilder modifyByteArray(String name, byte[] value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagByteArray tag = this.compound.getTag(name, TagByteArray.class);
				tag.setValue(value);
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add an Integer Array to the Compound
	public CompoundBuilder addIntegerArray(String name, int[] value) {
		this.checkIfInitialized();

		try {
			TagIntArray tag = new TagIntArray(name, value);
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Modify an existing Integer Array in the compound by name
	public CompoundBuilder modifyIntegerArray(String name, int[] value) {
		this.checkIfInitialized();

		try {
			if(this.compound.hasTagByName(name)) {
				TagIntArray tag = this.compound.getTag(name, TagIntArray.class);
				tag.setValue(value);
			}
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a List Tag to the Compound
	public CompoundBuilder addList(TagList tag) {
		this.checkIfInitialized();

		try {
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a Compound Tag to this Compound
	public CompoundBuilder addCompound(TagCompound tag) {
		this.checkIfInitialized();

		try {
			this.compound.addTag(tag);
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	//Add a Compound Tag from a Builder to this Compound
	public CompoundBuilder addCompound(CompoundBuilder builder) {
		this.checkIfInitialized();

		try {
			this.compound.addTag(builder.build());
		} catch(NBTException e) {
			e.printStackTrace();
		}

		return this;
	}

	public TagCompound build() {
		TagCompound tag = this.compound;
		this.compound = null;
		return tag;
	}

	/* Utility Functions */

	private void checkIfInitialized() {
		if(this.compound != null) return;

		throw new IllegalStateException("The Compound hasn't been initialized!");
	}

}
