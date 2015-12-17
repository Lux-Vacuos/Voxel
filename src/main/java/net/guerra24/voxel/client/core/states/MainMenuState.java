package net.guerra24.voxel.client.core.states;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import net.guerra24.voxel.client.core.GlobalStates;
import net.guerra24.voxel.client.core.GlobalStates.GameState;
import net.guerra24.voxel.client.core.State;
import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.graphics.MenuRendering;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Main Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class MainMenuState extends State {

	public MainMenuState() {
		super(1);
	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		Display display = voxel.getDisplay();
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		gm.getRenderer().prepare();
		display.beingNVGFrame();
		gm.getMenuSystem().mainMenu.render();
		display.endNVGFrame();
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		if (gm.getMenuSystem().mainMenu.getPlayButton().pressed()) {
			gm.getMenuSystem().gameSP.load(gm);
			states.setState(GameState.LOADING_WORLD);
		} else if (gm.getMenuSystem().mainMenu.getExitButton().pressed()) {
			states.loop = false;
		} else if (gm.getMenuSystem().mainMenu.getOptionsButton().pressed()) {
			gm.getMenuSystem().optionsMenu.load(gm);
			gm.getCamera().setPosition(new Vector3f(-1.4f, -3.4f, 1.4f));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			states.setState(GameState.OPTIONS);
		}
	}

}
