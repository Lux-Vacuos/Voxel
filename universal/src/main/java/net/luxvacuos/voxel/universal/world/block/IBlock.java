package net.luxvacuos.voxel.universal.world.block;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.world.utils.BlockNode;

public interface IBlock extends IBlockHandle {
	
	public int getPackedMetadata();
	
	public void setPackedMetadata(int packedMetadata);
	
	public boolean hasCollision();
	
	public BoundingBox getBoundingBox(BlockNode pos);
	
	public void setComplexMetadata(TagCompound metadata);
	
	public void onBlockUpdate(BlockNode node, IBlock block);
}
