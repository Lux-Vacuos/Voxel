package net.luxvacuos.voxel.client.world.entities.components;

import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;

import net.luxvacuos.voxel.client.world.items.ItemDrop;

public class DropComponent implements Component {

	private List<ItemDrop> drop;

	public DropComponent(List<ItemDrop> drop) {
		this.drop = drop;
	}

	public void drop(Engine system) {
		for (ItemDrop itemDrop : drop) {
			system.addEntity(itemDrop);
		}
		drop.clear();
	}

}
