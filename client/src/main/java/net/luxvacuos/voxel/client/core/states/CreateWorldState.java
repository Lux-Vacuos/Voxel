package net.luxvacuos.voxel.client.core.states;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;

import java.io.File;

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.Window;

public class CreateWorldState extends State {
	private Window window;
	private String name = "";
	private Text nameT;
	private Text optionsT;
	private Button createButton;

	public CreateWorldState() {
		window = new Window(20, GameResources.getInstance().getDisplay().getDisplayHeight() - 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40, "Create World");
		nameT = new Text("World Name", window.getWidth() / 2, -window.getHeight() / 2 + 200);
		nameT.setAlign(NVG_ALIGN_CENTER);
		optionsT = new Text("Options", window.getWidth() / 2, -window.getHeight() / 2 + 130);
		optionsT.setAlign(NVG_ALIGN_CENTER);
		createButton = new Button(window.getWidth() / 2 - 100, -window.getHeight() / 2 - 200, 200, 40, "Create");
		createButton.setOnButtonPress((button, delta) -> {
			if (!name.equals("")) {
				new File(VoxelVariables.WORLD_PATH + name).mkdirs();
				switchTo(GameState.SP_SELECTION);
				name = "";
			}
		});
		window.addChildren(nameT);
		window.addChildren(optionsT);
		window.addChildren(createButton);
	}

	@Override
	public void start() {
		window.setFadeAlpha(0);
	}

	@Override
	public void end() {
		window.setFadeAlpha(1);
	}

	@Override
	public void update(Voxel voxel, float delta) {
		window.update(delta);
		if (!switching)
			window.fadeIn(4, delta);
		if (switching)
			if (window.fadeOut(4, delta)) {
				readyForSwitch = true;
			}
	}

	@Override
	public void render(Voxel voxel, float alpha) {
		GameResources gm = voxel.getGameResources();
		MasterRenderer.prepare(0, 0, 0, 1);
		gm.getDisplay().beingNVGFrame();
		window.render();
		while (Keyboard.next())
			name = Keyboard.keyWritten(name);
		UIRendering.renderSearchBox(name, "Roboto-Regular", "Entypo",
				GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 150f,
				GameResources.getInstance().getDisplay().getDisplayHeight() / 2f - 180, 300, 20);
		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

}
