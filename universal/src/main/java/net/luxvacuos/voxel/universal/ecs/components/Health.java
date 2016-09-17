package net.luxvacuos.voxel.universal.ecs.components;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

public class Health implements VoxelComponent {
	
	private float health;
	
	public Health() {
		this.health = 10;
	}
	
	public Health(float health) {
		this.health = health;
	}
	
	public float get() {
		return this.health;
	}
	
	public Health set(float health) {
		this.health = health;
		
		return this;
	}
	
	public Health take(float amount) {
		this.health -= amount;
		
		return this;
	}
	
	public Health give(float amount) {
		this.health += amount;
		
		return this;
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		this.health = compound.getFloat("Health");
	}

	@Override
	public TagCompound save() {
		return new CompoundBuilder().start("HealthCompound").addFloat("Health", this.health).build();
	}

}
