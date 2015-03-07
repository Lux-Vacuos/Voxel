package net.voxel.core.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	
	public int id, width, height;
	
	private Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}
	public static Texture loadTexture(String location) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream(location));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getHeight());
		
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) ((pixel) & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		buffer.flip();
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0 , GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		return new Texture(id, image.getWidth(), image.getHeight());
	}
	public void bind() {
		glEnable(GL_TEXTURE_2D);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glBindTexture(GL_TEXTURE_2D, id);
	}
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void delete() {
		glDeleteTextures(id);
	}
}
