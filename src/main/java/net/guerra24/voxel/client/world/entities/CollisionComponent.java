package net.guerra24.voxel.client.world.entities;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.collision.BoundingBox;

public class CollisionComponent implements Component {
	
	public BoundingBox boundingBox = new BoundingBox();

}
