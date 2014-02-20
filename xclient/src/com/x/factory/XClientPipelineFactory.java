package com.x.factory;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.x.handler.XClinetHandler;
import com.x.protobuffer.Message.SendMessage;

public class XClientPipelineFactory implements ChannelPipelineFactory{

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipleline = Channels.pipeline();  
		pipleline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		pipleline.addLast("protobufDecoder", new ProtobufDecoder(SendMessage.getDefaultInstance()));
		pipleline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		pipleline.addLast("protobufEncoder", new ProtobufEncoder());
        pipleline.addLast("handler", new XClinetHandler());  
        return pipleline;  
	}

}
