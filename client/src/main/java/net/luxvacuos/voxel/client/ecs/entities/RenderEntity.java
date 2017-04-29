package net.luxvacuos.voxel.client.ecs.entities;

import net.luxvacuos.voxel.client.ecs.components.Renderable;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.universal.ecs.entities.BasicEntity;

public class RenderEntity extends BasicEntity {

	public RenderEntity(String name, TexturedModel model) {
		super(name);
		add(new Renderable(model));
	}

}
