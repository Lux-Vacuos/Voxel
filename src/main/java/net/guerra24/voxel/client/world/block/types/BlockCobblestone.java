package net.guerra24.voxel.client.world.block.types;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.guerra24.voxel.client.resources.models.WaterTile;
import net.guerra24.voxel.client.world.block.BlockBase;
import net.guerra24.voxel.client.world.block.BlockEntity;
import net.guerra24.voxel.client.world.block.BlocksResources;
import net.guerra24.voxel.universal.util.vector.Vector3f;
import net.guerra24.voxel.universal.util.vector.Vector8f;

public class BlockCobblestone extends BlockBase {

	@Override
	public byte getId() {
		return 15;
	}

	@Override
	public Vector8f texCoordsUp() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Cobblestone");
	}

	@Override
	public Vector8f texCoordsDown() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Cobblestone");
	}

	@Override
	public Vector8f texCoordsFront() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Cobblestone");
	}

	@Override
	public Vector8f texCoordsBack() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Cobblestone");
	}

	@Override
	public Vector8f texCoordsRight() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Cobblestone");
	}

	@Override
	public Vector8f texCoordsLeft() {
		return BlocksResources.getTessellatorTextureAtlas().getTextureCoords("Cobblestone");
	}

	@Override
	public BoundingBox getBoundingBox(Vector3f pos) {
		return new BoundingBox(new Vector3(pos.x, pos.y, pos.z), new Vector3(pos.x + 1, pos.y + 1, pos.z + 1));
	}

	@Override
	public WaterTile getWaterTitle(Vector3f pos) {
		return null;
	}

	@Override
	public BlockEntity getSingleModel(Vector3f pos) {
		return null;
	}

	@Override
	public boolean usesSingleModel() {
		return false;
	}

	@Override
	public boolean isTransparent() {
		return false;
	}

}
