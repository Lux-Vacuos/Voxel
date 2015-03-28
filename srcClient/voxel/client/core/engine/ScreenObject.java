package voxel.client.core.engine;

public interface ScreenObject {

	public void init();

	public void initGL();

	public void update();

	public void render();

	public void dispose();

}