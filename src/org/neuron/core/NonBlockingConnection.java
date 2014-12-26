package org.neuron.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.neuron.handler.ConnectionHandler;
import org.neuron.handler.ILogicHandler;
import org.neuron.log.MyLogger;

public class NonBlockingConnection {

	private SocketChannel socketChannel;
	private Dispatcher dispatcher;
	private ConnectionHandler handler;
	private LogicAdapter callback;
	
	
	public NonBlockingConnection(LogicAdapter callback,SocketChannel socketChannel,Dispatcher dispatcher){
		this.socketChannel=socketChannel;
		this.dispatcher=dispatcher;
		this.handler=new ConnectionHandler(this);
		this.callback=callback;
		registerChannel();
	}
	
	public ConnectionHandler getConnectionHandler(){
		return handler;
	}
	
	public LogicAdapter getLogicAdapter(){
		return callback;
	}
	
	public SocketChannel getSocketChannel(){
		return socketChannel;
	}
	
	public SelectionKey getSelectionKey(){
		Selector selector=dispatcher.getSelector();
		SelectionKey key=socketChannel.keyFor(selector);
		return key;
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
