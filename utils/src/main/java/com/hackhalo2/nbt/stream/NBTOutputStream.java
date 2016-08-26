package com.hackhalo2.nbt.stream;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import com.hackhalo2.nbt.ITag;
import com.hackhalo2.nbt.exceptions.NBTException;

public class NBTOutputStream extends DataOutputStream {
	
	/**
	 * Wraps a BufferedOutputStream to accept writing NBT Tags
	 * @param out The BufferedOutputStream to write to
	 */
	public NBTOutputStream(BufferedOutputStream out) {
		super(out);
	}
	
	/**
	 * Writes the NBT Tag to the Stream
	 * @param tag The NBT Tag to write
	 * @throws NBTException
	 * @throws IOException
	 */
	public void writeNBT(ITag tag) throws NBTException, IOException {
		this.writeByte(tag.getID());
		
		tag.writeNBT(this, false);
	}

}
