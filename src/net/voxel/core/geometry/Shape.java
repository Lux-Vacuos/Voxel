package net.voxel.core.geometry;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import com.nishu.utils.Color4f;

public class Shape {
	public static void createCube(float x, float y, float z, Color4f color, float size) {
        // bottom face
        glColor4f(color.r, color.g, color.b, color.a);
        glVertex3f(x, y, z + size);
        glVertex3f(x + size, y, z + size);
        glVertex3f(x + size, y, z);
        glVertex3f(x, y, z);

        // top face
        glColor4f(color.r, color.g, color.b, color.a);
        glVertex3f(x, y + size, z);
        glVertex3f(x + size, y + size, z);
        glVertex3f(x + size, y + size, z + size);
        glVertex3f(x, y + size, z + size);

        // front face
        glColor4f(color.r, color.g, color.b, color.a);
        glVertex3f(x, y, z);
        glVertex3f(x + size, y, z);
        glVertex3f(x + size, y + size, z);
        glVertex3f(x, y + size, z);

        // back face
        glColor4f(color.r, color.g, color.b, color.a);
        glVertex3f(x, y + size, z + size);
        glVertex3f(x + size, y + size, z + size);
        glVertex3f(x + size, y, z + size);
        glVertex3f(x, y, z + size);

        // left face
        glColor4f(color.r, color.g, color.b, color.a);
        glVertex3f(x + size, y, z);
        glVertex3f(x + size, y, z + size);
        glVertex3f(x + size, y + size, z + size);
        glVertex3f(x + size, y + size, z);

        // right face
        glColor4f(color.r, color.g, color.b, color.a);
        glVertex3f(x, y, z + size);
        glVertex3f(x, y, z);
        glVertex3f(x, y + size, z);
        glVertex3f(x, y + size, z + size);
	}
}