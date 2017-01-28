package net.luxvacuos.voxel.client.ecs.components;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.universal.ecs.components.VoxelComponent;

public class RendereableComponent implements VoxelComponent {

	private TexturedModel model;
	
	public RendereableComponent(TexturedModel model) {
		this.model = model;
	}
	
	public TexturedModel getModel() {
		return this.model;
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TagCompound save() {
		// TODO Auto-generated method stub
		return null;
	}

}
