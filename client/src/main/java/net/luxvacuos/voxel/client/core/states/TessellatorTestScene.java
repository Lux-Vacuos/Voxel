package net.luxvacuos.voxel.client.core.states;

import org.lwjgl.glfw.GLFW;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleDomain;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.Sun;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.world.dimension.PhysicsSystem;

public class TessellatorTestScene extends AbstractState {

	private PhysicsSystem physicsSystem;
	private Engine engine;
	private Sun sun;
	private ClientWorldSimulation worldSimulation;
	private Camera camera;
	private Tessellator tess;

	public TessellatorTestScene() {
		super(StateNames.TEST);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		ResourceLoader loader = window.getResourceLoader();

		Matrix4d shadowProjectionMatrix = Maths.orthographic(-ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		Matrix4d projectionMatrix = Renderer.createProjectionMatrix(window.getWidth(), window.getHeight(),
				ClientVariables.FOV, ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE);

		camera = new PlayerCamera(projectionMatrix, window);
		camera.setPosition(new Vector3d(0, 2, 0));
		sun = new Sun(shadowProjectionMatrix);

		worldSimulation = new ClientWorldSimulation();

		worldSimulation = new ClientWorldSimulation();
		engine = new Engine();
		physicsSystem = new PhysicsSystem(null);
		physicsSystem.addBox(new BoundingBox(new Vector3(-50, -1, -50), new Vector3(50, 0, 50)));
		engine.addSystem(physicsSystem);
		BlocksResources.createBlocks(loader);

		TessellatorShader.getShader();
		TessellatorBasicShader.getShader();

		TessellatorShader.getShader().start();
		TessellatorShader.getShader().loadProjectionMatrix(camera.getProjectionMatrix());
		TessellatorShader.getShader().loadBiasMatrix(sun.getCamera().getProjectionMatrix());
		TessellatorShader.getShader().stop();

		tess = new Tessellator(BlocksResources.getMaterial());
		RenderBlock t = new RenderBlock(new BlockMaterial("test"), new BlockFaceAtlas("stone"));
		t.setID(1);

		tess.begin();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				tess.generateCube(x, -1, z, 1, true, true, true, true, true, true, t);
			}
		}
		tess.end();

		Renderer.setShadowPass((camera, sunCamera, frustum, shadowMap) -> {
			tess.drawShadow(sunCamera);
		});

		Renderer.setDeferredPass((camera, sunCamera, frustum, shadowMap) -> {
			tess.draw(camera, sunCamera, worldSimulation, shadowMap);
		});

	}

	@Override
	public void start() {
		physicsSystem.getEngine().addEntity(camera);
		((PlayerCamera) camera).setMouse();
	}

	@Override
	public void end() {
		physicsSystem.getEngine().removeAllEntities();
	}

	@Override
	public void dispose() {
		tess.cleanUp();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		KeyboardHandler kbh = ClientInternalSubsystem.getInstance().getGameWindow().getKeyboardHandler();

		engine.update(delta);
		sun.update(camera.getPosition(), worldSimulation.update(delta), delta);

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
			((PlayerCamera) camera).unlockMouse();
			StateMachine.setCurrentState(StateNames.MAIN_MENU);
		}

	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Renderer.render(physicsSystem.getEngine().getEntities(), ParticleDomain.getParticles(), camera, sun.getCamera(),
				worldSimulation, sun.getSunPosition(), sun.getInvertedSunPosition(), alpha);
	}

}
