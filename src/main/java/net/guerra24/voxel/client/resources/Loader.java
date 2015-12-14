/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.resources;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import net.guerra24.voxel.client.resources.models.EntityTexture;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.util.Logger;

/**
 * Loader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class Loader {
	/**
	 * VAOs List
	 */
	private List<Integer> vaos = new ArrayList<Integer>();
	/**
	 * VBOs List
	 */
	private List<Integer> vbos = new ArrayList<Integer>();
	/**
	 * Texture List
	 */
	private List<Integer> textures = new ArrayList<Integer>();
	private OBJLoader objLoader;

	public Loader() {
		objLoader = new OBJLoader();
	}

	/**
	 * Load a multiple arrays of positions, texture coords, normals and indices
	 * 
	 * @param positions
	 *            Array of Positions
	 * @param textureCoords
	 *            Array of Tex Coords
	 * @param normals
	 *            Array of Normals
	 * @param indices
	 *            Array of Indices
	 * @return A RawModel
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	public int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}

	/**
	 * Load an array of positions and a dimension
	 * 
	 * @param positions
	 *            Array of Positions
	 * @param dimensions
	 *            Dimension
	 * @return RawModel
	 */
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}

	/**
	 * Load Block Texture
	 * 
	 * @param fileName
	 *            Block Texture Name
	 * @return Texture ID
	 */
	public int loadTextureBlocks(String fileName) {
		int texture_id = 0;
		try {
			InputStream file = getClass().getClassLoader()
					.getResourceAsStream("assets/textures/blocks/" + fileName + ".png");
			texture_id = loadTexture(file, GL_NEAREST, GL_REPEAT);
			Logger.log("Loading Texture: " + fileName + ".png");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("Couldn' load texture file " + fileName);
		}
		textures.add(texture_id);
		return texture_id;
	}
	/**
	 * Load Particle Texture
	 * 
	 * @param fileName
	 *            Particle Texture Name
	 * @return Texture ID
	 */
	public int loadTextureParticle(String fileName) {
		int texture_id = 0;
		try {
			InputStream file = getClass().getClassLoader()
					.getResourceAsStream("assets/textures/particles/" + fileName + ".png");
			Logger.log("Loading Texture: " + fileName + ".png");
			texture_id = loadTexture(file, GL_NEAREST, GL_REPEAT);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("Couldn' load texture file " + fileName);
		}
		textures.add(texture_id);
		return texture_id;
	}

	public int loadTextureFont(String fileName) {
		int texture = 0;
		try {
			InputStream file = getClass().getClassLoader().getResourceAsStream("assets/fonts/" + fileName + ".png");
			Logger.log("Loading Texture: " + fileName + ".png");
			texture = loadTexture(file, GL_NEAREST, GL_CLAMP_TO_EDGE);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("Couldn' load texture file " + fileName);
		}
		textures.add(texture);
		return texture;
	}

	/**
	 * Load Block Texture
	 * 
	 * @param fileName
	 *            Block Texture Name
	 * @return Texture ID
	 */
	public int loadTextureEntity(String fileName) {
		int texture = 0;
		try {
			InputStream file = getClass().getClassLoader()
					.getResourceAsStream("assets/textures/entity/" + fileName + ".png");
			Logger.log("Loading Texture: " + fileName + ".png");
			texture = loadTexture(file, GL_NEAREST, GL_REPEAT);
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("Couldn' load texture file" + fileName);
		}
		textures.add(texture);
		return texture;
	}

	/**
	 * Load Gui Texture
	 * 
	 * @param fileName
	 *            Gui Texture Name
	 * @return Texture ID
	 */
	public int loadTextureGui(String fileName) {
		int texture = 0;
		try {
			InputStream file = getClass().getClassLoader()
					.getResourceAsStream("assets/textures/menu/" + fileName + ".png");
			Logger.log("Loading Texture: " + fileName + ".png");
			texture = loadTexture(file, GL_NEAREST, GL_REPEAT);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("Couldn' load texture file" + fileName);
		}
		textures.add(texture);
		return texture;
	}

	private int loadTexture(InputStream file, int filter, int textureWarp) throws IOException {
		int texture_id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture_id);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, textureWarp);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, textureWarp);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		EntityTexture data = decodeTextureFile(file);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
				data.getBuffer());
		return texture_id;
	}

	/**
	 * Clear All VAOs, VBOs and Textures
	 * 
	 */
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

	/**
	 * Load Skybox Texture
	 * 
	 * @param textureFiles
	 *            Array of Texture Names
	 * @return Texture ID
	 */
	public int loadCubeMap(String[] textureFiles) {
		int texID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			InputStream file = getClass().getClassLoader()
					.getResourceAsStream("assets/textures/skybox/" + textureFiles[i] + ".png");
			EntityTexture data = decodeTextureFile(file);
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA,
					GL_UNSIGNED_BYTE, data.getBuffer());
		}

		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		textures.add(texID);
		return texID;
	}

	/**
	 * Decodes the Texture
	 * 
	 * @param file.
	 *            Name
	 * @return EntityTexture
	 */
	private EntityTexture decodeTextureFile(InputStream file) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			InputStream in = file;
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("Tried to load texture " + file + ", didn't work");
		}
		return new EntityTexture(buffer, width, height);
	}

	/**
	 * Create VAO
	 * 
	 * @return VaoID
	 */
	private int createVAO() {
		int vaoID = glGenVertexArrays();
		vaos.add(vaoID);
		glBindVertexArray(vaoID);
		return vaoID;
	}

	/**
	 * Store The Data in Attribute List
	 * 
	 * @param attributeNumber
	 *            Number
	 * @param coordinateSize
	 *            Coord Size
	 * @param data
	 *            Data
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Unbids the VAO
	 * 
	 */
	private void unbindVAO() {
		glBindVertexArray(0);
	}

	/**
	 * Bind Indices Buffer
	 * 
	 * @param indices
	 *            Array of Indices
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = glGenBuffers();
		vbos.add(vboID);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}

	/**
	 * Store Data in IntBuffer
	 * 
	 * @param data
	 *            Array of data
	 * @return IntBuffer
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Store Data in FloatBuffer
	 * 
	 * @param data
	 *            Array of data
	 * @return FloatBuffer
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Get OBJLoader
	 * 
	 * @return OBJLoader
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public OBJLoader getObjLoader() {
		return objLoader;
	}

}