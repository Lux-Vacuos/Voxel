package net.guerra24.voxel.client.core.states;

import java.util.Random;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.core.GlobalStates.GameState;

/**
 * Loading Screen State
 * 
 * @author danirod
 * @category Kernel
 */
public class LoadingState implements State {

	private boolean switchToGameSP = false;

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		switchToGameSP = true;
		Random seed;
		if (VoxelVariables.isCustomSeed) {
			seed = new Random(VoxelVariables.seed.hashCode());
		} else {
			seed = new Random();
		}
		voxel.getWorldsHandler().getActiveWorld().startWorld("World-0", seed, 0, voxel.getApi(),
				voxel.getGameResources());
		voxel.getGameResources().getCamera().setMouse();
		voxel.getGameResources().getSoundSystem().stop("menu1");
		voxel.getGameResources().getSoundSystem().rewind("menu1");
		voxel.getGameResources().getSoundSystem().stop("menu2");
		voxel.getGameResources().getSoundSystem().rewind("menu2");
		states.state = GameState.GAME_SP;
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		gm.getGuiRenderer().renderGui(gm.guis3);
		if (switchToGameSP) {
			gm.getMenuSystem().gameSP.load(gm);
			switchToGameSP = false;
		}
	}

}
