package voxel.client.core.engine.resources;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import voxel.client.core.engine.color.Color4f;
import voxel.client.core.engine.shaders.Shader;
import voxel.client.core.engine.shaders.ShaderProgram;

public class SpriteBatch {

	public static int TYPE_DYNAMIC = 0;
	public static int TYPE_STATIC = 1;

	private FloatBuffer vertices, colorVertices, textureVertices;

	private ShaderProgram currentShader;
	@SuppressWarnings("unused")
	private Texture currentTexture, defaultTexture = Texture
			.createEmptyTexture();
	@SuppressWarnings("unused")
	private Color4f currentColor, defaultColor = Color4f.DEFAULT;

	private int type, size, currentSize, vID, cID, tID, shaderProgram;
	private boolean render2D, active;

	public SpriteBatch() {
		this(TYPE_STATIC, 100000, true);
	}

	public SpriteBatch(int type, int size, boolean render2D) {
		this.type = type;
		this.size = size;
		this.render2D = render2D;
		this.active = false;
		this.currentSize = 0;

		createBuffers();
	}

	private void createBuffers() {
		if (render2D) {
			vertices = BufferUtils.createFloatBuffer(2 * size);
		} else if (!render2D) {
			vertices = BufferUtils.createFloatBuffer(3 * size);
		}
		colorVertices = BufferUtils.createFloatBuffer(4 * size);
		textureVertices = BufferUtils.createFloatBuffer(2 * size);

		if (type == TYPE_STATIC) {
			vID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vID);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			cID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, cID);
			glBufferData(GL_ARRAY_BUFFER, colorVertices, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);

			tID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, cID);
			glBufferData(GL_ARRAY_BUFFER, textureVertices, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}

	public void begin() {
		if (active)
			throw new IllegalStateException("Must call end() before begin()!");
		active = true;
	}

	public void render() {
		glEnable(GL_TEXTURE_2D);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		if (type == TYPE_STATIC)
			renderStatic();
		if (type == TYPE_DYNAMIC)
			renderDynamic();

		glDrawArrays(GL_TRIANGLES, 0,
				vertices.remaining() + colorVertices.remaining()
						+ textureVertices.remaining());

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}

	private void renderStatic() {
		if (render2D) {
			glBindBuffer(GL_ARRAY_BUFFER, vID);
			glVertexPointer(2, GL_FLOAT, 0, 0);
		} else if (!render2D) {
			glBindBuffer(GL_ARRAY_BUFFER, vID);
			glVertexPointer(3, GL_FLOAT, 0, 0);
		}

		glBindBuffer(GL_ARRAY_BUFFER, cID);
		glColorPointer(4, GL_FLOAT, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, tID);
		glTexCoordPointer(2, GL_FLOAT, 0, 0);
	}

	private void renderDynamic() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		if (render2D) {
			glVertexPointer(2, 0, vertices);
		} else if (!render2D) {
			glVertexPointer(3, 0, vertices);
		}
		glColorPointer(4, 0, colorVertices);
		glTexCoordPointer(2, 0, textureVertices);
	}

	public void end() {
		if (!active)
			throw new IllegalStateException("Must call begin() before end()!");

		vertices.flip();
		colorVertices.flip();
		textureVertices.flip();

		render();

		vertices.clear();
		colorVertices.clear();
		textureVertices.clear();

		active = false;
	}

	public void useShader() {
		if (currentShader != null)
			glUseProgram(shaderProgram);
	}

	// not recommended to use shaders this way. Should first add the shader to
	// the batcher and then call batcher.useShader()
	public void useShader(ShaderProgram program) {
		program.use();
	}

	// not recommended to use shaders this way. Should first add the shader to
	// the batcher and then call batcher.useShader()
	@SuppressWarnings("static-access")
	public void useShader(ResourceManager rm, String name) {
		rm.loadShaderProgram(name).use();
	}

	public void releaseShader() {
		glUseProgram(0);
	}

	public void addShader(String vLoc, String fLoc) {
		Shader temp = new Shader(vLoc, fLoc);
		ShaderProgram tempProgram = new ShaderProgram(temp.getvShader(),
				temp.getfShader());
		if (currentShader != null) {
			currentShader.release();
			currentShader.dispose();
		}
		currentShader = tempProgram;
	}

	public void addShader(ShaderProgram shader) {
		if (currentShader != null) {
			currentShader.release();
			currentShader.dispose();
		}
		currentShader = shader;
	}

	@SuppressWarnings("static-access")
	public void addShader(ResourceManager rm, String name) {
		if (currentShader != null) {
			currentShader.release();
			currentShader.dispose();
		}
		currentShader = rm.loadShaderProgram(name);
	}

	public void putData(float x, float y) {
		putData(x, y, 0, defaultColor.r, defaultColor.g, defaultColor.b,
				defaultColor.a, 0, 0);
	}

	public void putData(float x, float y, float z) {
		putData(x, y, z, defaultColor.r, defaultColor.g, defaultColor.b,
				defaultColor.a, 0, 0);
	}

	public void putData(float x, float y, float r, float g, float b, float a) {
		putData(x, y, 0, r, g, b, a, 0, 0);
	}

	public void putData(float x, float y, float z, float r, float g, float b,
			float a) {
		putData(x, y, z, r, g, b, a, 0, 0);
	}

	public void putData(float x, float y, Color4f color) {
		putData(x, y, 0, color.r, color.g, color.b, color.a, 0, 0);
	}

	public void putData(float x, float y, float z, Color4f color) {
		putData(x, y, z, color.r, color.g, color.b, color.a, 0, 0);
	}

	public void putData(float x, float y, Color4f color, float u, float v) {
		putData(x, y, 0, color.r, color.g, color.b, color.a, u, v);
	}

	public void putData(float x, float y, float z, Color4f color, float u,
			float v) {
		putData(x, y, z, color.r, color.g, color.b, color.a, u, v);
	}

	public void putData(float x, float y, float z, float r, float g, float b,
			float a, float u, float v) {
		if (z != 0)
			putData(new VertexData(x, y, z, r, g, b, a, u, v));
		else if (z == 0)
			putData(new VertexData(x, y, r, g, b, a, u, v));
	}

	public void putData(VertexData data) {
		if (currentSize >= size - 1)
			// restartBatch();

			if (render2D) {
				vertices.put(data.x).put(data.y);
			} else if (!render2D) {
				vertices.put(data.x).put(data.y).put(data.z);
			}
		colorVertices.put(data.r).put(data.g).put(data.b).put(data.a);
		textureVertices.put(data.u).put(data.v);

		currentSize++;
	}

	@SuppressWarnings("unused")
	private void restartBatch() {
		end();
		begin();
	}

	public void dispose() {
		glDeleteBuffers(vID);
		glDeleteBuffers(cID);
		glDeleteBuffers(tID);
	}

	private static class VertexData {
		public float x, y, z, r, g, b, a, u, v;

		public VertexData(float x, float y, float z, float r, float g, float b,
				float a, float u, float v) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
			this.u = u;
			this.v = v;
		}

		public VertexData(float x, float y, float r, float g, float b, float a,
				float u, float v) {
			super();
			this.x = x;
			this.y = y;
			this.z = 0;
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
			this.u = u;
			this.v = v;
		}

	}
}
