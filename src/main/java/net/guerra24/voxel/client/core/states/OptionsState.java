package net.guerra24.voxel.client.core.states;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
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

		if (gm.getMenuSystem().optionsMenu.getExitButton().insideButton())
			gm.getMenuSystem().optionsMenu.getList().get(0).changeScale(0.074f);
		else
			gm.getMenuSystem().optionsMenu.getList().get(0).changeScale(0.07f);
		gm.getMenuSystem().optionsMenu.getList().get(1).changeScale(0.07f);
		gm.getMenuSystem().optionsMenu.getList().get(2).changeScale(0.07f);
		gm.getMenuSystem().optionsMenu.getList().get(3).changeScale(0.07f);
		while (Mouse.next()) {
			if (gm.getMenuSystem().optionsMenu.getShadowsButton().pressed())
				VoxelVariables.useShadows = !VoxelVariables.useShadows;
			if (gm.getMenuSystem().optionsMenu.getWaterButton().pressed())
				VoxelVariables.useHQWater = !VoxelVariables.useHQWater;
			if (gm.getMenuSystem().optionsMenu.getGodraysButton().pressed())
				VoxelVariables.useVolumetricLight = !VoxelVariables.useVolumetricLight;
		}
		if (gm.getMenuSystem().optionsMenu.getExitButton().pressed()) {
			gm.getMenuSystem().mainMenu.load(gm);
			gm.getCamera().setPosition(new Vector3f(0, 0, 1));
			gm.getGameSettings().updateSetting();
			gm.getGameSettings().save();
			states.setState(GameState.MAINMENU);
		}
		if (VoxelVariables.useVolumetricLight)
			gm.getMenuSystem().optionsMenu.getList().get(1).changeScale(0.078f);
		if (VoxelVariables.useShadows)
			gm.getMenuSystem().optionsMenu.getList().get(2).changeScale(0.078f);
		if (VoxelVariables.useHQWater)
			gm.getMenuSystem().optionsMenu.getList().get(3).changeScale(0.078f);

	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getMenuSystem().optionsMenu.update(gm);
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		gm.getRenderer().prepare();
		gm.getRenderer().renderGui(gm.getMenuSystem().optionsMenu.getList(), gm);
	}

}
