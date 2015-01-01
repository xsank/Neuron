package org.neuron.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;

import org.neuron.log.MyLogger;


public class Connecter{

	private Dispatcher dispatcher;
	private LogicAdapter callback;
	private NonBlockingConnection connection;
	private Selector selector;
	private WatchDog watchDog;
	private SocketChannel socketChannel;
	private ScheduledFuture<?> checkStatus;
	
	private volatile boolean reconnected=false;
	private boolean watched=false;
	private boolean connected=false;
	private InetSocketAddress address;
	
	public Connecter(InetSocketAddress address){
		try {
			this.address=address;
			selector=Selector.open();
			
			//connect to the server
			connect();
			
			dispatcher=new Dispatcher(1);
			Thread thread=new Thread(dispatcher);
			thread.setDaemon(true);
			thread.start();
			
			watchDog=new WatchDog();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.severeLog("client socket registed failed");
		}
	}
	
	public void setReconnect(){
		reconnected=true;
	}
	
	public void addLogicAdapter(LogicAdapter logicAdapter){
		this.callback=logicAdapter;
	}
	
	public void listen(){
		while(selector.isOpen()){
			try {
				selector.select();
				Iterator<SelectionKey> iterator=selector.selectedKeys().iterator();
				while(iterator.hasNext()){
					SelectionKey key=iterator.next();
					iterator.remove();
					
					if(key.isConnectable()){
						SocketChannel channel=(SocketChannel) key.channel();
						if(channel.isConnectionPending()){
							channel.finishConnect();
							connection=new NonBlockingConnection(callback, channel, dispatcher);
							connected=true;
							
							MyLogger.infoLog("client connect to the server",address);
						}
					}
				}
			} catch (IOException e) {
				MyLogger.infoLog("client connect to the server failed");
				if(reconnected){connect();}
			}
		}
	}
	
	public class ReConnect implements Runnable{

		@Override
		public void run() {
			connect();
		}
	}
	
	public boolean isConnected(){
		return connection!=null;
	}
	
	private void connect(){
		try {
			socketChannel=SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(address);
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public NonBlockingConnection getConnection(){
		return connection;
	}
	
	public void close(){
		if(selector!=null){
			try {
				selector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
