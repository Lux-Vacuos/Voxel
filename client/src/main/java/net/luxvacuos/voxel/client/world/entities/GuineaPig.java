package net.luxvacuos.voxel.client.world.entities;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.entities.components.DropComponent;
import net.luxvacuos.voxel.client.world.entities.components.LifeComponent;
import net.luxvacuos.voxel.client.world.items.ItemDrop;
import net.luxvacuos.voxel.universal.resources.UniversalResources;

public class GuineaPig extends GameEntity {

	public GuineaPig(Vector3f position, float rotX, float rotY, float rotZ) {
		super(position, rotX, rotY, rotZ, 1);
	}

	public GuineaPig(Vector3f position) {
		super(position, 0, 0, 0, 1);
	}

	@Override
	public void init() {
		setModel(UniversalResources.player);
		setAABB(new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
		super.add(new LifeComponent(10));
		List<ItemDrop> drop = new ArrayList<>();
		drop.add(new ItemDrop(Block.Glass));
		super.add(new DropComponent(drop));
	}

	@Override
	public void update(float delta) {
	}

}
