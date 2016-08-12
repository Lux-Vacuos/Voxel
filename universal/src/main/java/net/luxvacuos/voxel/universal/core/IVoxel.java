package net.luxvacuos.voxel.universal.core;

public interface IVoxel {

	public void preInit() throws Exception;

	public void init() throws Exception;

	public void postInit() throws Exception;

	public void loop();

	public void update(float delta);

	public void handleError(Throwable e);

}
