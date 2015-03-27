package voxel.server.core.glutil;

public interface Camera {

	public void updateMouse();

	public void updateMouse(float mouseSpeed);

	public void updateMouse(float mouseSpeed, float maxUp, float maxDown);

	public void updateKeys(float delta);

	public void updateKeys(float delta, float speed);

	public void updateKeys(float delta, float speedX, float speedY, float speedZ);

	public void moveLookDir(float dx, float dy, float dz);

	public void setPos(float x, float y, float z);

	public void setFOV(float fov);

	public void setAspectRation(float ratio);

	public void applyOrtho();

	public void applyProjection();

	public void applyTranslations();

	public void setRotation(float pitch, float yaw, float roll);

	public float getX();

	public float getY();

	public float getZ();

	public float getPitch();

	public float getYaw();

	public float getRoll();

	public float getFOV();

	public float getAspectRatio();

	public float getNearClippingPlane();

	public float getFarClippingPlane();

}
