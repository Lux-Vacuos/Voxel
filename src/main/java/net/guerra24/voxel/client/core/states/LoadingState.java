package net.guerra24.voxel.client.core.states;

import java.util.Random;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.core.GlobalStates.GameState;

public class LoadingState implements State {

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		Random seed;
		if (VoxelVariables.isCustomSeed) {
			seed = new Random(VoxelVariables.seed.hashCode());
		} else {
			seed = new Random();
		}
		voxel.getWorldsHandler().getActiveWorld().startWorld("World-0", seed, 0, voxel.getApi(), voxel.getGameResources());
		voxel.getGameResources().getCamera().setMouse();
		voxel.getGameResources().getSoundSystem().stop("menu1");
		voxel.getGameResources().getSoundSystem().rewind("menu1");
		states.state = GameState.GAME_SP;
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		voxel.getGameResources().getRenderer().prepare();
		voxel.getGameResources().getGuiRenderer().renderGui(voxel.getGameResources().guis3);
	}

	
	
}
