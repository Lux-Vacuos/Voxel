package voxel.server.core.glutil;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;

public class Camera3D implements Camera {
	private float x = 0, y = 0, z = 0;
	private float pitch = 0, yaw = 0, roll = 0;
	private float aspectRatio = 1;
	private float fov;
	public final float zNear;
	public final float zFar;

	public Camera3D() {
		this.zNear = 0.3f;
		this.zFar = 100;
	}

	public Camera3D(float aspectRatio) {
		if (aspectRatio <= 0) {
			throw new IllegalArgumentException("aspectRatio " + aspectRatio
					+ " was 0 or was smaller than 0");
		}
		this.aspectRatio = aspectRatio;
		this.zNear = 0.3f;
		this.zFar = 100;
	}

	public Camera3D(CameraBuilder builder) {
		this.x = builder.x;
		this.y = builder.y;
		this.z = builder.z;
		this.pitch = builder.pitch;
		this.yaw = builder.yaw;
		this.roll = builder.roll;
		this.aspectRatio = builder.aspectRatio;
		this.zNear = builder.zNear;
		this.zFar = builder.zFar;
		this.fov = builder.fov;
	}

	public Camera3D(float aspectRatio, float x, float y, float z) {
		this(aspectRatio);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a new camera with the given aspect ratio, location, and
	 * orientation.
	 * 
	 * @param aspectRatio
	 *            the aspect ratio (width/height) of the camera
	 * @param x
	 *            the first location coordinate
	 * @param y
	 *            the second location coordinate
	 * @param z
	 *            the third location coordinate
	 * @param pitch
	 *            the pitch (rotation on the x-axis)
	 * @param yaw
	 *            the yaw (rotation on the y-axis)
	 * @param roll
	 *            the roll (rotation on the z-axis)
	 * 
	 * @throws IllegalArgumentException
	 *             if aspectRatio is 0 or smaller than 0
	 */
	public Camera3D(float aspectRatio, float x, float y, float z, float pitch,
			float yaw, float roll) {
		this(aspectRatio, x, y, z);
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	public static class CameraBuilder {
		private float x = 0, y = 0, z = 0;
		private float pitch = 0, yaw = 0, roll = 0;
		private float aspectRatio = 1;
		private float zNear = 0.001f;
		private float zFar = 100f;
		private float fov = 90;

		public CameraBuilder() {
		}

		/**
		 * Sets the aspect ratio of the camera.
		 * 
		 * @param aspectRatio
		 *            the aspect ratio of the camera (window width / window
		 *            height)
		 * 
		 * @return this
		 */
		public CameraBuilder setAspectRatio(float aspectRatio) {
			if (aspectRatio <= 0) {
				throw new IllegalArgumentException("aspectRatio " + aspectRatio
						+ " was 0 or was smaller than 0");
			}
			this.aspectRatio = aspectRatio;
			return this;
		}

		/**
		 * Sets the distance from the camera to the near clipping pane.
		 * 
		 * @param nearClippingPane
		 *            the distance from the camera to the near clipping pane
		 * 
		 * @return this
		 * 
		 * @throws IllegalArgumentException
		 *             if nearClippingPane is 0 or less
		 */
		public CameraBuilder setNearClippingPane(float nearClippingPane) {
			if (nearClippingPane <= 0) {
				throw new IllegalArgumentException("nearClippingPane "
						+ nearClippingPane + " is 0 or less");
			}
			this.zNear = nearClippingPane;
			return this;
		}

		/**
		 * Sets the distance from the camera to the far clipping pane.
		 * 
		 * @param farClippingPane
		 *            the distance from the camera to the far clipping pane
		 * 
		 * @return this
		 * 
		 * @throws IllegalArgumentException
		 *             if farClippingPane is 0 or less
		 */
		public CameraBuilder setFarClippingPane(float farClippingPane) {
			if (farClippingPane <= 0) {
				throw new IllegalArgumentException("farClippingPane "
						+ farClippingPane + " is 0 or less");
			}
			this.zFar = farClippingPane;
			return this;
		}

		/**
		 * Sets the field of view angle in degrees in the y direction.
		 * 
		 * @param fov
		 *            the field of view angle in degrees in the y direction
		 * 
		 * @return this
		 */
		public CameraBuilder setFieldOfView(float fov) {
			this.fov = fov;
			return this;
		}

		/**
		 * Sets the position of the camera.
		 * 
		 * @param x
		 *            the x-coordinate of the camera
		 * @param y
		 *            the y-coordinate of the camera
		 * @param z
		 *            the z-coordinate of the camera
		 * 
		 * @return this
		 */
		public CameraBuilder setPosition(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}

		/**
		 * Sets the rotation of the camera.
		 * 
		 * @param pitch
		 *            the rotation around the x-axis in degrees
		 * @param yaw
		 *            the rotation around the y-axis in degrees
		 * @param roll
		 *            the rotation around the z-axis in degrees
		 */
		public CameraBuilder setRotation(float pitch, float yaw, float roll) {
			this.pitch = pitch;
			this.yaw = yaw;
			this.roll = roll;
			return this;
		}

		/**
		 * Constructs an instance of EulerCamera from this builder helper class.
		 * 
		 * @return an instance of EulerCamera
		 * 
		 * @throws IllegalArgumentException
		 *             if farClippingPane is the same or less than
		 *             nearClippingPane
		 */
		public Camera3D build() {
			if (zFar <= zNear) {
				throw new IllegalArgumentException("farClippingPane " + zFar
						+ " is the same or less than " + "nearClippingPane "
						+ zNear);
			}
			return new Camera3D(this);
		}

	}

	@Override
	public void updateMouse() {
		final float MAX_UP = 90;
		final float MAX_DOWN = -90;
		float mouseDX = Mouse.getDX() * 0.16f;
		float mouseDY = Mouse.getDY() * 0.16f;

		if (yaw + mouseDX >= 360) {
			yaw = yaw + mouseDX - 360;
		} else if (yaw + mouseDX < 0) {
			yaw = 360 - yaw + mouseDX;
		} else {
			yaw += mouseDX;
		}
		if (pitch - mouseDY >= MAX_DOWN && pitch - mouseDY <= MAX_UP) {
			pitch += -mouseDY;
		} else if (pitch - mouseDY < MAX_DOWN) {
			pitch = MAX_DOWN;
		} else if (pitch - mouseDY > MAX_UP) {
			pitch = MAX_UP;
		}
	}

	@Override
	public void updateMouse(float mouseSpeed) {
		final float MAX_UP = 90;
		final float MAX_DOWN = -90;
		float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
		float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;

		if (yaw + mouseDX >= 360) {
			yaw = yaw + mouseDX - 360;
		} else if (yaw + mouseDX < 0) {
			yaw = 360 - yaw + mouseDX;
		} else {
			yaw += mouseDX;
		}
		if (pitch - mouseDY >= MAX_DOWN && pitch - mouseDY <= MAX_UP) {
			pitch += -mouseDY;
		} else if (pitch - mouseDY < MAX_DOWN) {
			pitch = MAX_DOWN;
		} else if (pitch - mouseDY > MAX_UP) {
			pitch = MAX_UP;
		}
	}

	@Override
	public void updateMouse(float mouseSpeed, float maxUp, float maxDown) {
		float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
		float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;

		if (yaw + mouseDX >= 360) {
			yaw = yaw + mouseDX - 360;
		} else if (yaw + mouseDX < 0) {
			yaw = 360 - yaw + mouseDX;
		} else {
			yaw += mouseDX;
		}
		if (pitch - mouseDY >= maxDown && pitch - mouseDY <= maxUp) {
			pitch += -mouseDY;
		} else if (pitch - mouseDY < maxDown) {
			pitch = maxDown;
		} else if (pitch - mouseDY > maxUp) {
			pitch = maxUp;
		}
	}

	@Override
	public void updateKeys(float delta) {
		if (delta <= 0)
			throw new IllegalArgumentException("Delta must be greater than 0");

		boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_S);
		boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

		if (keyUp && keyRight && !keyLeft && !keyDown) {
			moveLookDir(delta * 0.003f, 0, -delta * 0.003f);
		}
		if (keyUp && keyLeft && !keyRight && !keyDown) {
			moveLookDir(-delta * 0.003f, 0, -delta * 0.003f);
		}
		if (keyUp && !keyLeft && !keyRight && !keyDown) {
			moveLookDir(0, 0, -delta * 0.003f);
		}
		if (keyDown && keyLeft && !keyRight && !keyUp) {
			moveLookDir(-delta * 0.003f, 0, delta * 0.003f);
		}
		if (keyDown && keyRight && !keyLeft && !keyUp) {
			moveLookDir(delta * 0.003f, 0, delta * 0.003f);
		}
		if (keyDown && !keyUp && !keyLeft && !keyRight) {
			moveLookDir(0, 0, delta * 0.003f);
		}
		if (keyLeft && !keyRight && !keyUp && !keyDown) {
			moveLookDir(-delta * 0.003f, 0, 0);
		}
		if (keyRight && !keyLeft && !keyUp && !keyDown) {
			moveLookDir(delta * 0.003f, 0, 0);
		}
		if (space && !shift) {
			y += delta * 0.003f;
		}
		if (shift && !space) {
			y -= delta * 0.003f;
		}
	}

	@Override
	public void updateKeys(float delta, float speed) {
		if (delta <= 0)
			throw new IllegalArgumentException("Delta must be greater than 0");

		boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_S);
		boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean space = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

		if (keyUp && keyRight && !keyLeft && !keyDown) {
			moveLookDir(speed * delta * 0.003f, 0, -speed * delta * 0.003f);
		}
		if (keyUp && keyLeft && !keyRight && !keyDown) {
			moveLookDir(-speed * delta * 0.003f, 0, -speed * delta * 0.003f);
		}
		if (keyUp && !keyLeft && !keyRight && !keyDown) {
			moveLookDir(0, 0, -speed * delta * 0.003f);
		}
		if (keyDown && keyLeft && !keyRight && !keyUp) {
			moveLookDir(-speed * delta * 0.003f, 0, speed * delta * 0.003f);
		}
		if (keyDown && keyRight && !keyLeft && !keyUp) {
			moveLookDir(speed * delta * 0.003f, 0, speed * delta * 0.003f);
		}
		if (keyDown && !keyUp && !keyLeft && !keyRight) {
			moveLookDir(0, 0, speed * delta * 0.003f);
		}
		if (keyLeft && !keyRight && !keyUp && !keyDown) {
			moveLookDir(-speed * delta * 0.003f, 0, 0);
		}
		if (keyRight && !keyLeft && !keyUp && !keyDown) {
			moveLookDir(speed * delta * 0.003f, 0, 0);
		}
		if (space && !shift) {
			y += speed * delta * 0.003f;
		}
		if (shift && !space) {
			y -= speed * delta * 0.003f;
		}
	}

	@Override
	public void updateKeys(float delta, float speedX, float speedY, float speedZ) {
	}

	@Override
	public void moveLookDir(float dx, float dy, float dz) {
		this.z += dx * (float) cos(toRadians(yaw - 90)) + dz
				* cos(toRadians(yaw));
		this.x -= dx * (float) sin(toRadians(yaw - 90)) + dz
				* sin(toRadians(yaw));
		this.y += dy * (float) sin(toRadians(pitch - 90)) + dz
				* sin(toRadians(pitch));
	}

	@Override
	public void setPos(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void setFOV(float fov) {
		this.fov = fov;
	}

	@Override
	public void setAspectRation(float ratio) {
		this.aspectRatio = ratio;
	}

	@Override
	public void applyOrtho() {
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-aspectRatio, aspectRatio, -1, 1, 0, zFar);
		glPopAttrib();
	}

	@Override
	public void applyProjection() {
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov, aspectRatio, zNear, zFar);
		glPopAttrib();
	}

	@Override
	public void applyTranslations() {
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_MODELVIEW);
		glRotatef(pitch, 1, 0, 0);
		glRotatef(yaw, 0, 1, 0);
		glRotatef(roll, 0, 0, 1);
		glTranslatef(-x, -y, -z);
		glPopAttrib();
	}

	@Override
	public void setRotation(float pitch, float yaw, float roll) {
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	@Override
	public float getPitch() {
		return pitch;
	}

	@Override
	public float getYaw() {
		return yaw;
	}

	@Override
	public float getRoll() {
		return roll;
	}

	@Override
	public float getFOV() {
		return fov;
	}

	@Override
	public float getAspectRatio() {
		return aspectRatio;
	}

	@Override
	public float getNearClippingPlane() {
		return zNear;
	}

	@Override
	public float getFarClippingPlane() {
		return zFar;
	}
}
