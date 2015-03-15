package net.voxel.geometry;

import static org.lwjgl.opengl.GL11.*;
import net.voxel.utilites.Spritesheet;

import com.nishu.utils.Color4f;

public class Shape {

	public static void createCube(float x, float y, float z, Color4f color,
			float[] texCoords, float size) {
		if (texCoords.length == 2) {
			glEnable(GL_TEXTURE_2D);
			// bottom face
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[0], texCoords[1]);
			glVertex3f(x, y, z + size);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1]);
			glVertex3f(x + size, y, z + size);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y, z);
			glTexCoord2f(texCoords[0],
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x, y, z);

			// top face
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[0], texCoords[1]);
			glVertex3f(x, y + size, z);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1]);
			glVertex3f(x + size, y + size, z);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y + size, z + size);
			glTexCoord2f(texCoords[0],
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x, y + size, z + size);

			// front face
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[0], texCoords[1]);
			glVertex3f(x, y, z);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1]);
			glVertex3f(x + size, y, z);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y + size, z);
			glTexCoord2f(texCoords[0],
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x, y + size, z);

			// back face
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[0], texCoords[1]);
			glVertex3f(x, y + size, z + size);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1]);
			glVertex3f(x + size, y + size, z + size);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y, z + size);
			glTexCoord2f(texCoords[0],
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x, y, z + size);

			// left face
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[0], texCoords[1]);
			glVertex3f(x + size, y, z);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1]);
			glVertex3f(x + size, y, z + size);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y + size, z + size);
			glTexCoord2f(texCoords[0],
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y + size, z);

			// right face
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[0], texCoords[1]);
			glVertex3f(x, y, z + size);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1]);
			glVertex3f(x, y, z);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x, y + size, z);
			glTexCoord2f(texCoords[0],
					texCoords[1] + Spritesheet.tiles.uniformSize());
			glVertex3f(x, y + size, z + size);
		} else if (texCoords.length > 2) {
			/*
			 * bottom - first top - second front - third back - fourth left -
			 * fifth right - sixth
			 */
			// bottom face (0, 1)
			glEnable(GL_TEXTURE_2D);
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[0], texCoords[1]);
			glVertex3f(x, y, z + size);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1]);
			glVertex3f(x + size, y, z + size);
			glTexCoord2f(texCoords[0] + Spritesheet.tiles.uniformSize(),
					texCoords[1] - Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y, z);
			glTexCoord2f(texCoords[0],
					texCoords[1] - Spritesheet.tiles.uniformSize());
			glVertex3f(x, y, z);

			// top face (2, 3)
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[2], texCoords[3]);
			glVertex3f(x, y + size, z);
			glTexCoord2f(texCoords[2] + Spritesheet.tiles.uniformSize(),
					texCoords[3]);
			glVertex3f(x + size, y + size, z);
			glTexCoord2f(texCoords[2] + Spritesheet.tiles.uniformSize(),
					texCoords[3] - Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y + size, z + size);
			glTexCoord2f(texCoords[2],
					texCoords[3] - Spritesheet.tiles.uniformSize());
			glVertex3f(x, y + size, z + size);

			// front face (4, 5)
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[4], texCoords[5]);
			glVertex3f(x, y, z);
			glTexCoord2f(texCoords[4] + Spritesheet.tiles.uniformSize(),
					texCoords[5]);
			glVertex3f(x + size, y, z);
			glTexCoord2f(texCoords[4] + Spritesheet.tiles.uniformSize(),
					texCoords[5] - Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y + size, z);
			glTexCoord2f(texCoords[4],
					texCoords[5] - Spritesheet.tiles.uniformSize());
			glVertex3f(x, y + size, z);

			// back face (6, 7)
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[6], texCoords[7]);
			glVertex3f(x, y + size, z + size);
			glTexCoord2f(texCoords[6] + Spritesheet.tiles.uniformSize(),
					texCoords[7]);
			glVertex3f(x + size, y + size, z + size);
			glTexCoord2f(texCoords[6] + Spritesheet.tiles.uniformSize(),
					texCoords[7] + Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y, z + size);
			glTexCoord2f(texCoords[6],
					texCoords[7] + Spritesheet.tiles.uniformSize());
			glVertex3f(x, y, z + size);

			// left face (8, 9)
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[8], texCoords[9]);
			glVertex3f(x + size, y, z);
			glTexCoord2f(texCoords[8] + Spritesheet.tiles.uniformSize(),
					texCoords[9]);
			glVertex3f(x + size, y, z + size);
			glTexCoord2f(texCoords[8] + Spritesheet.tiles.uniformSize(),
					texCoords[9] - Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y + size, z + size);
			glTexCoord2f(texCoords[8],
					texCoords[9] - Spritesheet.tiles.uniformSize());
			glVertex3f(x + size, y + size, z);

			// right face (10, 11)
			glColor4f(color.r, color.g, color.b, color.a);
			glTexCoord2f(texCoords[10], texCoords[11]);
			glVertex3f(x, y, z + size);
			glTexCoord2f(texCoords[10] + Spritesheet.tiles.uniformSize(),
					texCoords[11]);
			glVertex3f(x, y, z);
			glTexCoord2f(texCoords[10] + Spritesheet.tiles.uniformSize(),
					texCoords[11] - Spritesheet.tiles.uniformSize());
			glVertex3f(x, y + size, z);
			glTexCoord2f(texCoords[10],
					texCoords[11] - Spritesheet.tiles.uniformSize());
			glVertex3f(x, y + size, z + size);
		}
	}
}