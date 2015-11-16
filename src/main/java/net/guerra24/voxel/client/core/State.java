package net.guerra24.voxel.client.core;

/**
 * State
 * 
 * @author danirod
 * @category Kernel
 */
public interface State {

	void update(Voxel voxel, GlobalStates states, float delta);

	void render(Voxel voxel, GlobalStates states, float delta);

}
