package net.voxel.core.geometry;

import static org.lwjgl.opengl.GL11.*;
import net.voxel.core.util.SpriteSheets;

import com.nishu.utils.Color4f;

public class Shape {
	public static void createCube(float x, float y, float z, Color4f color, float[] texCoords, float size) {
        //bottom face
        glColor4f(color.r, color.g, color.b, color.a);
        glTexCoord2f(texCoords[0], texCoords[1]);
        glVertex3f(x, y, z + size);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1]);
        glVertex3f(x + size, y, z + size);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x + size, y, z);
        glTexCoord2f(texCoords[0], texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x, y, z);

        // top face
        glColor4f(color.r, color.g, color.b, color.a);
        glTexCoord2f(texCoords[0], texCoords[1]);
        glVertex3f(x, y + size, z);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1]);
        glVertex3f(x + size, y + size, z);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x + size, y + size, z + size);
        glTexCoord2f(texCoords[0], texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x, y + size, z + size);

        // front face
        glColor4f(color.r, color.g, color.b, color.a);
        glTexCoord2f(texCoords[0], texCoords[1]);
        glVertex3f(x, y, z);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1]);
        glVertex3f(x + size, y, z);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x + size, y + size, z);
        glTexCoord2f(texCoords[0], texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x, y + size, z);

        // back face
        glColor4f(color.r, color.g, color.b, color.a);
        glTexCoord2f(texCoords[0], texCoords[1]);
        glVertex3f(x, y + size, z + size);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1]);
        glVertex3f(x + size, y + size, z + size);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x + size, y, z + size);
        glTexCoord2f(texCoords[0], texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x, y, z + size);

        // left face
        glColor4f(color.r, color.g, color.b, color.a);
        glTexCoord2f(texCoords[0], texCoords[1]);
        glVertex3f(x + size, y, z);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1]);
        glVertex3f(x + size, y, z + size);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x + size, y + size, z + size);
        glTexCoord2f(texCoords[0], texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x + size, y + size, z);

        // right face
        glColor4f(color.r, color.g, color.b, color.a);
        glTexCoord2f(texCoords[0], texCoords[1]);
        glVertex3f(x, y, z + size);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1]);
        glVertex3f(x, y, z);
        glTexCoord2f(texCoords[0] + SpriteSheets.blocks.uniformSize(), texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x, y + size, z);
        glTexCoord2f(texCoords[0], texCoords[1] + SpriteSheets.blocks.uniformSize());
        glVertex3f(x, y + size, z + size);
}
}