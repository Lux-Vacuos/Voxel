package net.luxvacuos.voxel.client.rendering.world;

import net.luxvacuos.voxel.client.rendering.api.opengl.Frustum;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.IWorld;

public interface IRenderWorld extends IWorld {

	public void render(Camera camera, Camera sunCamera, Frustum frustum, int shadowMap);
	
	public void renderOcclusion(Camera camera, Frustum frustum);
	
	public void renderShadow(Camera sunCamera, Frustum frustum);
}
