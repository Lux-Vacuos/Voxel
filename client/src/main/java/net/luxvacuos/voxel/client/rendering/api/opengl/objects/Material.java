package net.luxvacuos.voxel.client.rendering.api.opengl.objects;

import net.luxvacuos.igl.vector.Vector3f;

public class Material {

	private Vector3f baseColor;
	private float roughness, metallic;

	public Material(Vector3f baseColor, float roughness, float metallic) {
		this.baseColor = baseColor;
		this.roughness = roughness;
		this.metallic = metallic;
	}

	public Vector3f getBaseColor() {
		return baseColor;
	}

	public float getMetallic() {
		return metallic;
	}

	public float getRoughness() {
		return roughness;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Material)
			return false;
		Material t = (Material) obj;
		return t.getBaseColor().getX() == baseColor.getX() && t.getBaseColor().getY() == baseColor.getY()
				&& t.getBaseColor().getZ() == baseColor.getZ() && t.getRoughness() == roughness
				&& t.getMetallic() == metallic;
	}

}
