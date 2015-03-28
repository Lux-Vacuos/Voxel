package voxel.server.core.engine.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;

import voxel.client.core.util.Logger;
import voxel.server.core.engine.net.codecs.Encoder;
import voxel.server.core.engine.net.codecs.LoginDecoder;

public class PipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("encoder", new Encoder());
		pipeline.addLast("decoder", new LoginDecoder());
		Logger.log("Pipes Constructed");
		return pipeline;
	}

}
