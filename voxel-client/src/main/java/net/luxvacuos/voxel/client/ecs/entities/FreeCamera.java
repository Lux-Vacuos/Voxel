package net.luxvacuos.voxel.client.ecs.entities;

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import net.luxvacuos.lightengine.client.core.ClientVariables;
import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.ecs.entities.CameraEntity;
import net.luxvacuos.lightengine.client.input.KeyboardHandler;
import net.luxvacuos.lightengine.client.input.MouseHandler;
import net.luxvacuos.lightengine.client.rendering.glfw.Window;
import net.luxvacuos.lightengine.client.rendering.opengl.Renderer;
import net.luxvacuos.lightengine.client.util.Maths;
import net.luxvacuos.lightengine.universal.core.subsystems.EventSubsystem;
import net.luxvacuos.lightengine.universal.util.IEvent;
import net.luxvacuos.lightengine.universal.util.registry.Key;

public class FreeCamera extends CameraEntity {

	private int mouseSpeed = 8;
	private final int maxLookUp = 90;
	private final int maxLookDown = -90;
	private IEvent resize;
	protected Vector2f center;

	public FreeCamera(String name, String uuid) {
		super(name, uuid);
	}

	@Override
	public void init() {
		int width = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width"));
		int height = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height"));

		super.setProjectionMatrix(Renderer.createProjectionMatrix(width, height,
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Core/fov")), ClientVariables.NEAR_PLANE,
				ClientVariables.FAR_PLANE, true));
		super.setViewMatrix(Maths.createViewMatrix(this));
		center = new Vector2f(width / 2f, height / 2f);

		resize = EventSubsystem.addEvent("lightengine.renderer.resize", () -> {
			super.setProjectionMatrix(Renderer.createProjectionMatrix(
					(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")),
					(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")),
					(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Core/fov")),
					ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE, true));
			center.set(width / 2f, height / 2f);
		});
		super.init();
	}

	@Override
	public void update(float delta) {
		Window window = GraphicalSubsystem.getMainWindow();
		KeyboardHandler kbh = window.getKeyboardHandler();
		MouseHandler mh = window.getMouseHandler();

		float mouseDX = mh.getDX() * mouseSpeed * delta;
		float mouseDY = mh.getDY() * mouseSpeed * delta;
		if (localRotation.x() - mouseDY >= maxLookDown && localRotation.x() - mouseDY <= maxLookUp)
			localRotation.x -= mouseDY;
		else if (localRotation.x() - mouseDY < maxLookDown)
			localRotation.x = maxLookDown;
		else if (localRotation.x() - mouseDY > maxLookUp)
			localRotation.x = maxLookUp;

		localRotation.y += mouseDX;
		localRotation.y %= 360;

		Vector3f walkDirection = new Vector3f(0.0f, 0.0f, 0.0f);
		float walkVelocity = 1.1f * 2.0f;
		if (kbh.isCtrlPressed())
			walkVelocity *= 100f;
		float walkSpeed = walkVelocity * delta;

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_W)) {
			walkDirection.z += (float) -Math.cos(Math.toRadians(localRotation.y()));
			walkDirection.x += (float) Math.sin(Math.toRadians(localRotation.y()));
		} else if (kbh.isKeyPressed(GLFW.GLFW_KEY_S)) {
			walkDirection.z += (float) Math.cos(Math.toRadians(localRotation.y()));
			walkDirection.x += (float) -Math.sin(Math.toRadians(localRotation.y()));
		}

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_D)) {
			walkDirection.z += (float) Math.sin(Math.toRadians(localRotation.y()));
			walkDirection.x += (float) Math.cos(Math.toRadians(localRotation.y()));
		} else if (kbh.isKeyPressed(GLFW.GLFW_KEY_A)) {
			walkDirection.z += (float) -Math.sin(Math.toRadians(localRotation.y()));
			walkDirection.x += (float) -Math.cos(Math.toRadians(localRotation.y()));
		}

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			walkDirection.y = 1;
		}
		if (kbh.isShiftPressed()) {
			walkDirection.y = -1;
		}
		walkDirection.mul(walkSpeed);
		localPosition.add(walkDirection);
		super.update(delta);
	}

	@Override
	public void dispose() {
		EventSubsystem.removeEvent("lightengine.renderer.resize", resize);
		super.dispose();
	}

}