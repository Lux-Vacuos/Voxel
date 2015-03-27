package voxel.client.core.engine;


public abstract class Screen implements ScreenObject {
	@Override
	public abstract void init();

	@Override
	public abstract void initGL();

	@Override
	public abstract void update();

	@Override
	public abstract void render();

	@Override
	public abstract void dispose();
}
