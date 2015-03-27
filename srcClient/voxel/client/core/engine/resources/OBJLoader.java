package voxel.client.core.engine.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;

public class OBJLoader {

	public static Model loadModel(File f) throws FileNotFoundException,
			IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		Model m = new Model();
		String line;
		while ((line = reader.readLine()) != null) {
			// parse
			if (line.startsWith("v ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.vertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn ")) {
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				m.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("f ")) {
				Vector3f vertexIndices = new Vector3f(Float.valueOf(line
						.split(" ")[1].split("/")[0]), Float.valueOf(line
						.split(" ")[2].split("/")[0]), Float.valueOf(line
						.split(" ")[3].split("/")[0]));
				Vector3f normalIndices = new Vector3f(Float.valueOf(line
						.split(" ")[1].split("/")[2]), Float.valueOf(line
						.split(" ")[2].split("/")[2]), Float.valueOf(line
						.split(" ")[3].split("/")[2]));
				m.faces.add(new ModelFace(vertexIndices, normalIndices));
			}
		}
		reader.close();
		return m;
	}

}
