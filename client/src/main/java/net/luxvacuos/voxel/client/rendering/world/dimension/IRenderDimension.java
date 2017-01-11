package net.luxvacuos.voxel.client.rendering.world.dimension;

import net.luxvacuos.voxel.client.rendering.api.opengl.Frustum;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public interface IRenderDimension extends IDimension {
	
	public void render(Camera camera, Camera sunCamera, Frustum frustum, int shadowMap);
	
	public void renderOcclusion(Camera camera, Frustum frustum);
	
	public void renderShadow(Camera sunCamera, Frustum frustum);
	
}
