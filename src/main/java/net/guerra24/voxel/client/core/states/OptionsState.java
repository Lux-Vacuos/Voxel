package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Options Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class OptionsState extends State {

	public OptionsState() {
		super(3);
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		while (Mouse.next()) {
			if (gm.getMenuSystem().optionsMenu.getShadowsButton().pressed())
				VoxelVariables.useShadows = !VoxelVariables.useShadows;
			if (gm.getMenuSystem().optionsMenu.getDofButton().pressed())
				VoxelVariables.useDOF = !VoxelVariables.useDOF;
			if (gm.getMenuSystem().optionsMenu.getGodraysButton().pressed())
				VoxelVariables.useVolumetricLight = !VoxelVariables.useVolumetricLight;
		}
		if (gm.getMenuSystem().optionsMenu.getExitButton().pressed()) {
			gm.getCamera().setPosition(new Vector3f(0, 0, 1));
			gm.getGameSettings().updateSetting();
			gm.getGameSettings().save();
			states.setState(GameState.MAINMENU);
		}
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		gm.getRenderer().prepare();
		Display.beingNVGFrame();
		gm.getMenuSystem().optionsMenu.render();
		Display.endNVGFrame();
	}

}
