package net.luxvacuos.voxel.universal.ecs.components;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

public class Scale implements VoxelComponent {
	
	private float scale;
	
	public Scale() {
		this.scale = 1;
	}
	
	public Scale(float scale) {
		this.scale = scale;
	}
	
	public float getScale() {
		return this.scale;
	}
	
	public Scale setScale(float scale) {
		this.scale = scale;
		
		return this;
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		this.scale = compound.getFloat("Scale");
		
	}

	@Override
	public TagCompound save() {
		return new CompoundBuilder().start("ScaleCompound").addFloat("Scale", this.scale).build();
	}

}
