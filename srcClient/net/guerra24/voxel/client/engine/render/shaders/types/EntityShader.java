package net.guerra24.voxel.client.engine.render.shaders.types;

import java.util.List;

import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.render.shaders.ShaderProgram;
import net.guerra24.voxel.client.engine.util.Maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class EntityShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;

	private static final String VERTEX_FILE = "VertexShaderEntity.glsl";
	private static final String FRAGMENT_FILE = "FragmentShaderEntity.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_viewMatrix;
	private int location_skyColour;

	public EntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(3, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super
				.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super
				.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_skyColour = super.getUniformLocation("skyColour");

		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super
					.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour["
					+ i + "]");
		}
	}

	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r, g, b));
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i)
						.getPosition());
				super.loadVector(location_lightColour[i], lights.get(i)
						.getColour());
			} else {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0,
						0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
			}
		}
	}

	public void loadviewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
}
