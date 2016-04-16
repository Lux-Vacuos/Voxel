package net.luxvacuos.voxel.client.world.entities.components;

import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.world.items.ItemDrop;
import net.luxvacuos.voxel.client.world.physics.PositionComponent;

public class DropComponent implements Component {

	private List<ItemDrop> drop;

	public DropComponent(List<ItemDrop> drop) {
		this.drop = drop;
	}

	public void drop(Engine system, Vector3f pos) {
		for (ItemDrop itemDrop : drop) {
			itemDrop.getComponent(PositionComponent.class).position = pos;
			system.addEntity(itemDrop);
		}
		drop.clear();
	}

}
