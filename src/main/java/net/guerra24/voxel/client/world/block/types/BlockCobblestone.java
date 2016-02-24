package net.guerra24.voxel.client.world.block.types;

import net.guerra24.voxel.client.world.block.BlockBase;
import net.guerra24.voxel.client.world.block.BlocksResources;
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

}
