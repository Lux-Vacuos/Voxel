package io.github.guerra24.voxel.client.kernel.core;

public interface IKernel {
	
	public void init();

	public void mainLoop();
	
	public void errorTest();

	public void render();
	
	public void update();
	
	public void error();

	public void dispose();
}
