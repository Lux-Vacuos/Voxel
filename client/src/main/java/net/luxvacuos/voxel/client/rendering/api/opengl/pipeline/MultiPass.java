package net.luxvacuos.voxel.client.rendering.api.opengl.pipeline;

import net.luxvacuos.voxel.client.rendering.api.opengl.RenderingPipeline;
import net.luxvacuos.voxel.client.resources.GameResources;

public class MultiPass extends RenderingPipeline {

	public MultiPass() throws Exception {
		super("MultiPass");
	}

	private LightingPass lightingPass;
	private SunPass sunPass;
	private BloomMaskPass bloomMaskPass;
	private BloomHorizonal bloomHorizontal;
	private BloomVertical bloomVertical;
	private SSRPass ssrPass;

	@Override
	public void init(GameResources gm) throws Exception {
		sunPass = new SunPass(width, height);
		sunPass.setName("Sun");
		sunPass.init();
		super.imagePasses.add(sunPass);
		lightingPass = new LightingPass(width, height);
		lightingPass.setName("Lighting");
		lightingPass.init();
		super.imagePasses.add(lightingPass);
		bloomMaskPass = new BloomMaskPass(width, height);
		bloomMaskPass.setName("BloomMask");
		bloomMaskPass.init();
		super.imagePasses.add(bloomMaskPass);
		bloomHorizontal = new BloomHorizonal(width, height);
		bloomHorizontal.setName("BloomHorizontal");
		bloomHorizontal.init();
		super.imagePasses.add(bloomHorizontal);
		bloomVertical = new BloomVertical(width, height);
		bloomVertical.setName("BloomVertical");
		bloomVertical.init();
		super.imagePasses.add(bloomVertical);
		ssrPass = new SSRPass(width, height);
		ssrPass.setName("SSR");
		ssrPass.init();
		super.imagePasses.add(ssrPass);
	}

	@Override
	public void dispose() {
	}

}
