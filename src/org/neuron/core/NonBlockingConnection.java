package org.neuron.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.neuron.handler.ConnectionHandler;
import org.neuron.log.MyLogger;

public class NonBlockingConnection {

	private SocketChannel socketChannel;
	private Dispatcher dispatcher;
	private ConnectionHandler handler;
	
	
	public NonBlockingConnection(SocketChannel socketChannel,Dispatcher dispatcher){
		this.socketChannel=socketChannel;
		this.dispatcher=dispatcher;
		this.handler=new ConnectionHandler(this);
		registerChannel();
	}
	
	public ConnectionHandler getConnectionHandler(){
		return handler;
	}
	
	public SocketChannel getSocketChannel(){
		return socketChannel;
	}
	
	public void registerChannel(){
		try {
			socketChannel.configureBlocking(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dispatcher.registerChannel(this, SelectionKey.OP_READ);
	}
	
	public void close(){
		try {
			socketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.severeLog("connection close failed");
		}
	}
}
