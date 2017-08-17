package net.luxvacuos.voxel.universal.world.block;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.BlockNode;
import net.luxvacuos.voxel.universal.world.utils.IHandleable;

public interface IBlock extends IBlockHandle, IHandleable<IBlockHandle> {
	
	public int getPackedMetadata();
	
	public void setPackedMetadata(int packedMetadata);
	
	public boolean hasCollision();
	
	public BoundingBox getBoundingBox(BlockNode pos);
	
	public void setComplexMetadata(TagCompound metadata);
	
	public void setChunk(IChunk chunk);
	
	public void setPosition(int x, int y, int z);
	
	public void setPosition(BlockNode node);
	
	@Override
	public IChunk getChunk();
	
	@Override
	public IDimension getDimension();
	
	public void onBlockUpdate(BlockNode node, IBlock block);
}
