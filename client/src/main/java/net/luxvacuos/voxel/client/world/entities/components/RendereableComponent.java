package net.luxvacuos.voxel.client.world.entities.components;

import com.badlogic.ashley.core.Component;

import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;

public class RendereableComponent implements Component {

	public TexturedModel model;
	public Material material;

}
