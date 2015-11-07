package net.guerra24.voxel.client.resources.models;

import net.guerra24.voxel.client.api.API;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.GuiResources;
import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.entities.Entity;
import net.guerra24.voxel.client.world.entities.IEntity;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class ButtonModel implements IEntity {

	private Entity entity;

	public ButtonModel(TexturedModel model, Vector3f pos, Vector3f rot, float scale) {
		entity = new Entity(model, pos, rot.x, rot.y, rot.z, scale);
	}

	public void changeScale(float dest) {
		if (entity.getScale() < dest)
			entity.setScale(entity.getScale() + 0.0007f);
		else if (entity.getScale() > dest)
			entity.setScale(entity.getScale() - 0.0007f);
	}

	@Override
	public void update(float delta, GameResources gm, GuiResources gi, IWorld world, API api) {
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

}
