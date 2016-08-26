package com.hackhalo2.nbt.tags;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.hackhalo2.nbt.ITag;

public enum TagType {
	END(0, null),
	BYTE(1, TagByte.class),
	SHORT(2, TagShort.class),
	INT(3, TagInt.class),
	LONG(4, TagLong.class),
	FLOAT(5, TagFloat.class),
	DOUBLE(6, TagDouble.class),
	BYTE_ARRAY(7, TagByteArray.class),
	STRING(8, TagString.class),
	LIST(9, TagList.class),
	COMPOUND(10, TagCompound.class),
	INT_ARRAY(11, TagIntArray.class);
	
	private final byte id;
	private final Class<? extends ITag> tagClass;
	private static final Map<Byte, TagType> types;
	
	private TagType(int id, Class<? extends ITag> clazz) {
		this.id = ((byte)(id));
		this.tagClass = clazz;
	}
	
	static {
		Map<Byte, TagType> builder = new HashMap<>();
		
		for(TagType type : TagType.values())
			builder.put(type.getID(), type);
		
		types = Collections.unmodifiableMap(builder);
	}
	
	public byte getID() {
		return this.id;
	}
	
	public Class<? extends ITag> getTagClass() {
		return this.tagClass;
	}
	
	public static TagType valueOf(byte id) {
		return types.get(id);
	}

}
