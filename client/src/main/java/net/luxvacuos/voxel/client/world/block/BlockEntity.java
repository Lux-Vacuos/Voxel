package net.luxvacuos.voxel.client.world.block;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.RendereableTexturedModel;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;
import net.luxvacuos.voxel.client.world.Dimension;
@Deprecated
public abstract class BlockEntity extends BlockBase {

	protected int x, y, z;
	private transient RendereableTexturedModel model;

	public BlockEntity() {
	}

	public BlockEntity(Integer x, Integer y, Integer z, TexturedModel model) {
		this.x = x;
		this.y = y;
		this.z = z;
		setModel(model);
	}

	public BlockEntity(Integer x, Integer y, Integer z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void init() {
	}

	public void update(Dimension dimension, float delta) {
	}

	public void render() {
		model.render(GameResources.getInstance().getRenderer().getShader());
	}

	protected void setModel(TexturedModel model) {
		this.model = new RendereableTexturedModel(new Vector3f(x + 0.5f, y + 0.5f, z + 0.5f), model);
	}

	protected RendereableTexturedModel getModel() {
		return model;
	}

}
