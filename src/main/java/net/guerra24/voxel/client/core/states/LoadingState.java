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
public class LoadingState extends State {

	public LoadingState() {
		super(5);
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		Random seed;
		if (VoxelVariables.isCustomSeed) {
			seed = new Random(VoxelVariables.seed.hashCode());
		} else {
			seed = new Random();
		}
		voxel.getWorldsHandler().getActiveWorld().startWorld("World-0", seed, 0, voxel.getApi(),
				voxel.getGameResources());
		gm.getCamera().setMouse();
		gm.getSoundSystem().stop("menu1");
		gm.getSoundSystem().rewind("menu1");
		gm.getSoundSystem().stop("menu2");
		gm.getSoundSystem().rewind("menu2");
		states.setState(GameState.GAME_SP);
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		gm.getGuiRenderer().renderGui(gm.guis3);
	}

}
