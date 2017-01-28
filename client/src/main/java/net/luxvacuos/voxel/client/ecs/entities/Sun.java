package net.luxvacuos.voxel.client.ecs.entities;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;

public class Sun {

	private Vector3d rotation = new Vector3d(5, 0, -35);
	private Vector3d sunPosition = new Vector3d(0, 0, 0);
	private Vector3d invertedSunPosition = new Vector3d(0, 0, 0);
	private SunCamera camera;

	public Sun(Matrix4d[] shadowProjectionMatrix) {
		camera = new SunCamera(shadowProjectionMatrix);
	}

	public Sun(Vector3d rotation, Matrix4d[] shadowProjectionMatrix) {
		this.rotation = rotation;
		camera = new SunCamera(shadowProjectionMatrix);
	}

	public void update(Vector3d cameraPosition, float rot, float delta) {
		camera.setPosition(cameraPosition);
		rotation.setY(rot);
		camera.setRotation(new Vector3d(rotation.y, rotation.x, rotation.z));
		camera.updateShadowRay(false);
		sunPosition.set(camera.getDRay().direction.x * 10, camera.getDRay().direction.y * 10,
				camera.getDRay().direction.z * 10);

		camera.updateShadowRay(true);
		invertedSunPosition.set(camera.getDRay().direction.x * 10, camera.getDRay().direction.y * 10,
				camera.getDRay().direction.z * 10);
	}

	public CameraEntity getCamera() {
		return camera;
	}

	public Vector3d getInvertedSunPosition() {
		return invertedSunPosition;
	}

	public Vector3d getSunPosition() {
		return sunPosition;
	}

}
