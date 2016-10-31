/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.resources;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.nanovg.NanoVG.nvgCreateImageMem;
import static org.lwjgl.nanovg.NanoVG.nvgDeleteImage;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_is_hdr_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.exception.DecodeTextureException;
import net.luxvacuos.voxel.client.core.exception.LoadTextureException;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.resources.models.EntityTexture;
import net.luxvacuos.voxel.client.resources.models.RawModel;

/**
 * This objects handles all loading methods from any type of data, models,
 * textures, fonts, etc.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class ResourceLoader {
	/**
	 * VAOs List
	 */
	private List<Integer> vaos = new ArrayList<Integer>();
	/**
	 * VBOs List
	 */
	private List<Integer> vbos = new ArrayList<Integer>();
	/**
	 * NanoVG Data
	 */
	private List<Integer> nvgData = new ArrayList<Integer>();
	/**
	 * Texture List
	 */
	private List<Integer> textures = new ArrayList<Integer>();
	/*
	 * NanoVG Fonts
	 */
	private List<ByteBuffer> nvgFont = new ArrayList<ByteBuffer>();
	private OBJLoader objLoader;
	private long windowID;

	/*
	 * public Loader(Display display) { objLoader = new OBJLoader(this);
	 * this.display = display; }
	 */

	public ResourceLoader(long windowID) {
		this.objLoader = new OBJLoader(this);
		this.windowID = windowID;
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

	public int createEmptyVBO(int floatCount) {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, floatCount * 4, GL_STREAM_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		return vbo;
	}

	public void updateVBO(int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, GL_STREAM_DRAW);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void addInstacedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLenght,
			int offset) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindVertexArray(vao);
		glVertexAttribPointer(attribute, dataSize, GL_FLOAT, false, instancedDataLenght * 4, offset * 4);
		glVertexAttribDivisor(attribute, 1);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
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
			Logger.log("Loading Texture: " + fileName + ".png");
			texture_id = loadTexture("assets/" + ClientVariables.assets + "/textures/blocks/" + fileName + ".png",
					GL_NEAREST, GL_REPEAT, GL_SRGB_ALPHA);
		} catch (Exception e) {
			throw new LoadTextureException(fileName, e);
		}
		textures.add(texture_id);
		return texture_id;
	}

	public int loadTextureMisc(String fileName) {
		int texture_id = 0;
		try {
			Logger.log("Loading Texture: " + fileName + ".png");
			texture_id = loadTexture("assets/" + ClientVariables.assets + "/textures/blocks/" + fileName + ".png",
					GL_NEAREST, GL_REPEAT, GL_RGBA);
		} catch (Exception e) {
			throw new LoadTextureException(fileName, e);
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
			Logger.log("Loading Texture: " + fileName + ".png");
			texture_id = loadTexture("assets/" + ClientVariables.assets + "/textures/particles/" + fileName + ".png",
					GL_NEAREST, GL_REPEAT, GL_SRGB_ALPHA);
		} catch (Exception e) {
			throw new LoadTextureException(fileName, e);
		}
		textures.add(texture_id);
		return texture_id;
	}

	/**
	 * Load Entity Texture
	 * 
	 * @param fileName
	 *            Block Texture Name
	 * @return Texture ID
	 */
	public int loadTextureEntity(String fileName) {
		int texture = 0;
		try {
			Logger.log("Loading Texture: " + fileName + ".png");
			texture = loadTexture("assets/" + ClientVariables.assets + "/textures/entity/" + fileName + ".png",
					GL_NEAREST, GL_REPEAT, GL_SRGB_ALPHA);
			glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (Exception e) {
			throw new LoadTextureException(fileName, e);
		}
		textures.add(texture);
		return texture;
	}

	private int loadTexture(String file, int filter, int textureWarp, int format) throws DecodeTextureException {
		int texture_id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture_id);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, textureWarp);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, textureWarp);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		EntityTexture data = decodeTextureFile(file);
		if (data.getComp() == 3) {
			if ((data.getWidth() & 3) != 0)
				glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (data.getWidth() & 1));
			glTexImage2D(GL_TEXTURE_2D, 0, format, data.getWidth(), data.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE,
					data.getBuffer());
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, format, data.getWidth(), data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
					data.getBuffer());
		}
		// glTexImage2D(GL_TEXTURE_2D, 0, format, data.getWidth(),
		// data.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
		// data.getBuffer());
		return texture_id;
	}

	public void loadNVGFont(String filename, String name) throws LoadTextureException {
		loadNVGFont(filename, name, 150);
	}

	public void loadNVGFont(String filename, String name, int size) throws LoadTextureException {
		Window window = WindowManager.getWindow(this.windowID);
		Logger.log("Loading NVGFont: " + filename + ".ttf");
		int font = 0;
		try {
			ByteBuffer buffer = ioResourceToByteBuffer(
					"assets/" + ClientVariables.assets + "/fonts/" + filename + ".ttf", size * 1024);
			nvgFont.add(buffer);
			font = nvgCreateFontMem(window.getNVGID(), name, buffer, 0);
		} catch (IOException e) {
			throw new LoadTextureException(filename, e);
		}
		if (font == -1)
			Logger.error("Fail to load Font");
	}

	public int loadNVGTexture(String file) throws LoadTextureException {
		Window window = WindowManager.getWindow(this.windowID);
		ByteBuffer buffer = null;
		int tex = 0;
		try {
			Logger.log("Loading NVGTexture: " + file + ".png");
			buffer = ioResourceToByteBuffer("assets/" + ClientVariables.assets + "/textures/menu/" + file + ".png",
					8 * 1024);
			tex = nvgCreateImageMem(window.getNVGID(), 0, buffer);
		} catch (Exception e) {
			throw new LoadTextureException(file, e);
		}
		nvgData.add(tex);
		return tex;
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

		Window window = WindowManager.getWindow(this.windowID);
		long nvgID = window.getNVGID();
		for (int texture : nvgData) {
			nvgDeleteImage(nvgID, texture);
		}
	}

	public int loadCubeMap(String[] textureFiles) throws DecodeTextureException {
		int texID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			EntityTexture data = decodeTextureFile(
					"assets/" + ClientVariables.assets + "/textures/skybox/" + textureFiles[i] + ".png");
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, data.getWidth(), data.getHeight(), 0, GL_RGBA,
					GL_UNSIGNED_BYTE, data.getBuffer());
		}

		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		textures.add(texID);
		return texID;
	}

	private EntityTexture decodeTextureFile(String file) throws DecodeTextureException {
		int width = 0;
		int height = 0;
		int component = 0;
		ByteBuffer imageBuffer;
		ByteBuffer image;
		try {
			imageBuffer = ioResourceToByteBuffer(file, 8 * 1024);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		if (!stbi_info_from_memory(imageBuffer, w, h, comp))
			throw new DecodeTextureException("Failed to read image information: " + stbi_failure_reason());

		Logger.log("Image width: " + w.get(0), "Image height: " + h.get(0), "Image components: " + comp.get(0),
				"Image HDR: " + stbi_is_hdr_from_memory(imageBuffer));

		image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
		if (image == null)
			throw new DecodeTextureException("Failed to load image: " + stbi_failure_reason());
		width = w.get(0);
		height = h.get(0);
		component = comp.get(0);

		return new EntityTexture(image, width, height, component);
	}

	/**
	 * Reads the specified resource and returns the raw data as a ByteBuffer.
	 *
	 * @param resource
	 *            the resource to read
	 * @param bufferSize
	 *            the initial buffer size
	 *
	 * @return the resource data
	 *
	 * @throws IOException
	 *             if an IO error occurs
	 */
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		File file = new File(resource);
		if (file.isFile()) {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();

			buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);

			while (fc.read(buffer) != -1)
				;

			fis.close();
			fc.close();
		} else {
			buffer = BufferUtils.createByteBuffer(bufferSize);

			InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if (source == null)
				throw new FileNotFoundException(resource);

			try {
				ReadableByteChannel rbc = Channels.newChannel(source);
				try {
					while (true) {
						int bytes = rbc.read(buffer);
						if (bytes == -1)
							break;
						if (buffer.remaining() == 0)
							buffer = resizeBuffer(buffer, buffer.capacity() * 2);
					}
				} finally {
					rbc.close();
				}
			} finally {
				source.close();
			}
		}

		buffer.flip();
		return buffer;
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
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