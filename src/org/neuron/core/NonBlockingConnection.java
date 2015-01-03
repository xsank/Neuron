package org.neuron.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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
	private NonblockingData data;
	private LogicAdapter callback;
	
	
	public NonBlockingConnection(LogicAdapter callback,SocketChannel socketChannel,Dispatcher dispatcher){
		this.socketChannel=socketChannel;
		this.dispatcher=dispatcher;
		this.data=new NonblockingData();
		this.callback=callback;
		this.handler=new ConnectionHandler(this);
		registerChannel();
	}
	
	public NonblockingData getConnectionData(){
		return data;
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
	
	public void syncWrite(String s){
		ByteBuffer buffer=ByteBuffer.wrap(s.getBytes());
		try {
			socketChannel.write(buffer);
		} catch (IOException e) {
			MyLogger.severeLog("socket channel write failed");
		}
	}
	
	public void write(String message,String encoding){
		if(message!=null && !message.equals("")){
			byte[] bs=message.getBytes();
			try {
				String s=new String(bs,encoding);
				ByteBuffer buffer=ByteBuffer.wrap(s.getBytes());
				data.appendDataToWriteQueue(buffer);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MyLogger.severeLog("encoding string:",message,"error,charset:",encoding);
			}
			
		}
	}
	
	public void write(String message){
		this.write(message, "UTF-8");
	}

	public void write(byte[] message){
		ByteBuffer buffer=ByteBuffer.wrap(message);
		data.appendDataToWriteQueue(buffer);
	}
	
	@Override
	public String toString(){
		return socketChannel.toString();
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
