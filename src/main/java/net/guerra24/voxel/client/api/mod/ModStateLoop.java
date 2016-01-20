package net.guerra24.voxel.client.api.mod;

import net.guerra24.voxel.client.core.State;

public interface ModStateLoop {

	public void update(State state, float delta);

	public void render(State state, float alpha);

}
