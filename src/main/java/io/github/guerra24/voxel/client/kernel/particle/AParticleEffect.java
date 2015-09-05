package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

import java.util.List;

public abstract class AParticleEffect {
	private abstract class Vertex {
		Vector3f m_Pos;
		Vector3f m_Diffuse;
		Vector3f m_Tex0;
	};

	List<Vertex> VertexBuffer;
	List<Vector3f> ColorInterpolator;

	public abstract void SetCamera(Camera Camera);

	public abstract void SetParticleEmitter(ParticleEmitter pEmitter);

	public abstract void RandomizeParticles();

	public abstract void EmitParticles();

	public abstract void Update(float DeltaTime);

	public abstract void Render();

	public abstract boolean LoadTexture(String fileName);

	public abstract void Resize(int numParticles);

	protected abstract void RandomizeParticle(Particle particle);

	protected abstract void EmitParticle(Particle particle);

	public abstract void BuildVertexBuffer();
}
