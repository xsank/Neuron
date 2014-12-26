package org.neuron.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.neuron.log.MyLogger;


public class Connecter{

	private Dispatcher dispatcher;
	private LogicAdapter callback;
	private NonBlockingConnection connection;
	private Selector selector;
	
	public Connecter(InetSocketAddress address){
		try {
			dispatcher=new Dispatcher(1);
			SocketChannel channel=SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(address);
			
			selector=Selector.open();
			channel.register(selector, SelectionKey.OP_CONNECT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.severeLog("client socket registed failed");
		}
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
					if(key.isConnectable()){
						SocketChannel channel=(SocketChannel) key.channel();
						if(channel.isConnectionPending()){
							channel.finishConnect();
							connection=new NonBlockingConnection(callback, channel, dispatcher);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
