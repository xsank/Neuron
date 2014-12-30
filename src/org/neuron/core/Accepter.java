package org.neuron.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.neuron.controller.ConnectionController;
import org.neuron.controller.DispatcherController;
import org.neuron.log.MyLogger;


public class Accepter {
	private ServerSocketChannel serverChannel;
	private DispatcherController dispatcherController;
	private ConnectionController connectionController;
	private LogicAdapter callback;
	private Selector selector;
	
	public Accepter(InetSocketAddress address){
		connectionController=new ConnectionController();
		try {
			serverChannel=ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(address);
			
			selector=Selector.open();
			serverChannel.register(selector,SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.severeLog("create server socket failed");
		}
		dispatcherController=new DispatcherController();
	}
	
	public void addLogicAdapter(LogicAdapter logicAdapter){
		this.callback=logicAdapter;
	}

	public void listen(){
		while(serverChannel.isOpen()){
			try {
				selector.select();
				Iterator<SelectionKey> iterator=selector.selectedKeys().iterator();
				while(iterator.hasNext()){
					SelectionKey key=iterator.next();
					iterator.remove();
					
					if(key.isAcceptable()){
						SocketChannel channel=serverChannel.accept();
						Dispatcher dispatcher=dispatcherController.getDispatcher();
						connectionController.addConnection(new NonBlockingConnection(callback,channel, dispatcher));
						MyLogger.infoLog("a new connection established");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MyLogger.severeLog("server socket process error");
			}
		}
	}
	
	public void close(){
		try {
			serverChannel.close();
			dispatcherController.close();
			connectionController.close();
		} catch (IOException e) {
			e.printStackTrace();
			MyLogger.severeLog("acceptor close failed");
		}
	}
}
