package io.github.guerra24.voxel.client.kernel;

public interface IKernel {
	
	public void init();

	public void mainLoop();

	public void render();
	
	public void update();

	public void dispose();
}
