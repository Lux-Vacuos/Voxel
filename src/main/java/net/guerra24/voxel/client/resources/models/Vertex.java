package net.guerra24.voxel.client.resources.models;

public class Vertex {
	public float pos_x, pos_y, pos_z;
	public float tex_coord_x, tex_coord_y;
	public float tex_coord_offset_x, tex_coord_offset_y;
	public float color_r, color_g, color_b;

	public Vertex(float pos_x, float pos_y, float pos_z, float tex_coord_x, float tex_coord_y, float tex_coord_offset_x,
			float tex_coord_offset_y, float color_r, float color_g, float color_b) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.pos_z = pos_z;

		this.tex_coord_x = tex_coord_x;
		this.tex_coord_y = tex_coord_y;

		this.tex_coord_offset_x = tex_coord_offset_x;
		this.tex_coord_offset_y = tex_coord_offset_y;

		this.color_r = color_r;
		this.color_g = color_g;
		this.color_b = color_b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(color_b);
		result = prime * result + Float.floatToIntBits(color_g);
		result = prime * result + Float.floatToIntBits(color_r);
		result = prime * result + Float.floatToIntBits(pos_x);
		result = prime * result + Float.floatToIntBits(pos_y);
		result = prime * result + Float.floatToIntBits(pos_z);
		result = prime * result + Float.floatToIntBits(tex_coord_offset_x);
		result = prime * result + Float.floatToIntBits(tex_coord_offset_y);
		result = prime * result + Float.floatToIntBits(tex_coord_x);
		result = prime * result + Float.floatToIntBits(tex_coord_y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (Float.floatToIntBits(color_b) != Float.floatToIntBits(other.color_b))
			return false;
		if (Float.floatToIntBits(color_g) != Float.floatToIntBits(other.color_g))
			return false;
		if (Float.floatToIntBits(color_r) != Float.floatToIntBits(other.color_r))
			return false;
		if (Float.floatToIntBits(pos_x) != Float.floatToIntBits(other.pos_x))
			return false;
		if (Float.floatToIntBits(pos_y) != Float.floatToIntBits(other.pos_y))
			return false;
		if (Float.floatToIntBits(pos_z) != Float.floatToIntBits(other.pos_z))
			return false;
		if (Float.floatToIntBits(tex_coord_offset_x) != Float.floatToIntBits(other.tex_coord_offset_x))
			return false;
		if (Float.floatToIntBits(tex_coord_offset_y) != Float.floatToIntBits(other.tex_coord_offset_y))
			return false;
		if (Float.floatToIntBits(tex_coord_x) != Float.floatToIntBits(other.tex_coord_x))
			return false;
		if (Float.floatToIntBits(tex_coord_y) != Float.floatToIntBits(other.tex_coord_y))
			return false;
		return true;
	}
}
