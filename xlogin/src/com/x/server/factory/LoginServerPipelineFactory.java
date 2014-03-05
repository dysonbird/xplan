package com.x.server.factory;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import tutorial.Basemessage.BaseMessage;

import com.x.server.handler.LoginServerHandler;

public class LoginServerPipelineFactory implements ChannelPipelineFactory{
	private static OrderedMemoryAwareThreadPoolExecutor executor = new OrderedMemoryAwareThreadPoolExecutor(32, 0, 0);//32是同步处理线程数，0/0是每个channel的event上限   
	private static ExecutionHandler  executorHandler = new ExecutionHandler(executor); 
 

	@Override
	public ChannelPipeline getPipeline() throws Exception {
//		ChannelPipeline pipeline = Channels.pipeline();
//    	pipeline.addLast("executor", executorHandler);//handler的executor
//    	pipeline.addLast("decoder", new LengthFieldBasedFrameDecoder(10240,0,2,-2,0));
//    	pipeline.addLast("handler", new LoginServerHandler());
//      return pipeline;
        
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("executor", executorHandler);//handler的executor
        pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
        pipeline.addLast("protobufDecoder", new ProtobufDecoder(BaseMessage.getDefaultInstance()));
        pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast("protobufEncoder", new ProtobufEncoder());
        pipeline.addLast("handler", new LoginServerHandler());
		return pipeline;
      
//		ChannelPipeline pipeline = Channels.pipeline();
//		pipeline.addLast("executor", executorHandler);//handler的executor
//  		pipeline.addLast("decoder", new StringDecoder());  
//  		pipeline.addLast("encoder", new StringEncoder());
//  		pipeline.addLast("handler", new LoginServerHandler());
//  		return pipeline;
	}
}
