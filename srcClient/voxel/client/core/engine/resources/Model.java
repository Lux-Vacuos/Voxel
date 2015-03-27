package voxel.client.core.engine.resources;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Model {

	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<ModelFace> faces = new ArrayList<ModelFace>();

	public Model() {
	}
}
