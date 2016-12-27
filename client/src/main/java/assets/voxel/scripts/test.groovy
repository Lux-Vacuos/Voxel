import net.luxvacuos.voxel.client.ui.UIButton
import org.lwjgl.glfw.GLFW
import net.luxvacuos.voxel.client.ui.OnAction
import net.luxvacuos.voxel.client.ui.UIComponent

if (kb.isKeyPressed(GLFW.GLFW_KEY_R)) {
	UIButton btn = new UIButton(200,-200,100,20, "Groovy!")
	btn.setOnButtonPress(new OnAction() {
		@Override
		public void onAction(UIComponent uIComponent, float delta) {
			println "Click to Groovy!"
		}
	})
	window.addChildren(btn)
}


