package net.luxvacuos.voxel.client.rendering.api.opengl;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;

public class ItemsGuiRenderer {

	private Tessellator tess;
	private Camera camera;
	private Matrix4f projectionMatrix;
	private float aspectRatio;

	public ItemsGuiRenderer(GameResources gm) throws Exception {
		tess = new Tessellator();
		projectionMatrix = Maths.orthographic(-11 * aspectRatio, 11 * aspectRatio, -11, 11, -11, 11, false);
		camera = new Camera(projectionMatrix, new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
		camera.setPosition(new Vector3f(13.58f, 13.58f, 13.58f));
		camera.setPitch(45);
		camera.setYaw(45);
		aspectRatio = (float) gm.getDisplay().getDisplayWidth() / (float) gm.getDisplay().getDisplayHeight();
	}

	public void render(GameResources gm) {
		projectionMatrix = Maths.orthographic(-11 * aspectRatio, 11 * aspectRatio, -11, 11, -11, 11, false);
		tess.draw(camera, projectionMatrix, gm);
	}

	public void cleanUp() {
		tess.cleanUp();
	}

	public Tessellator getTess() {
		return tess;
	}

}
