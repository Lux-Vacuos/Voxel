package net.guerra24.voxel.client.resources;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.kernel.render.textures.types.EntityTexture;
import net.guerra24.voxel.client.kernel.util.Logger;
import net.guerra24.voxel.client.resources.models.RawModel;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVAO(float[] positions, float[] textureCoords,
			float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}

	public int loadTextureBlocks(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(
					"assets/textures/blocks/" + fileName + ".png"), GL_NEAREST);
			Logger.log("Loading Texture: " + fileName + ".png");
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
					GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("Couldn' load texture file" + fileName);
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}

	public int loadTextureGui(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(
					"assets/textures/menu/" + fileName + ".png"), GL_NEAREST);
			Logger.log("Loading Texture: " + fileName + ".png");
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
					GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("Couldn' load texture file" + fileName);
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}

	public void cleanUp() {
		for (int vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			glDeleteTextures(texture);
		}
	}

	public int loadCubeMap(String[] textureFiles) {
		int texID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			EntityTexture data = decodeTextureFile("assets/textures/skybox/"
					+ textureFiles[i] + ".png");
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA,
					data.getWidth(), data.getHeight(), 0, GL_RGBA,
					GL_UNSIGNED_BYTE, data.getBuffer());
		}

		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		textures.add(texID);
		return texID;
	}

	private EntityTexture decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new EntityTexture(buffer, width, height);
	}

	private int createVAO() {
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);
		glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber,
			int coordinateSize, float[] data) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false,
				0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO() {
		glBindVertexArray(0);
	}

	private void bindIndicesBuffer(int[] indices) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}

	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}