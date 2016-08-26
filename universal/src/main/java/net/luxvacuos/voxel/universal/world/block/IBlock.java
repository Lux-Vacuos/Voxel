package net.luxvacuos.voxel.universal.world.block;

import com.badlogic.gdx.math.collision.BoundingBox;

public interface IBlock {
	public int getID();
	
	public String getName();
	
	public BoundingBox getBoundingBox();
	
	public boolean isAffectedByGravity();
	
	public boolean hasCollision();
	
	public boolean isFluid();

	public boolean hasMetadata();
}
