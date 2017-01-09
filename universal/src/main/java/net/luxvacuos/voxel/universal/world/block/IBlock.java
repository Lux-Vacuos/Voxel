package net.luxvacuos.voxel.universal.world.block;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.world.utils.BlockNode;

public interface IBlock {
	public int getID();
	
	public int getPackedMetadata();
	
	public void setPackedMetadata(int packedMetadata);
	
	public String getName();
	
	public BoundingBox getBoundingBox(BlockNode pos);
	
	public boolean isAffectedByGravity();
	
	public boolean hasCollision();
	
	public boolean isFluid();

	public boolean hasComplexMetadata();
	
	public void setComplexMetadata(TagCompound metadata);
}
