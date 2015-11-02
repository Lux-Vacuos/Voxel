package net.guerra24.voxel.resources.models;

import net.guerra24.voxel.api.VAPI;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.GuiResources;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.world.IWorld;
import net.guerra24.voxel.world.entities.Entity;
import net.guerra24.voxel.world.entities.IEntity;

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
	public void update(float delta, GameResources gm, GuiResources gi, IWorld world, VAPI api) {
	}

	@Override
	public Entity getEntity() {
		return entity;
	}

}
