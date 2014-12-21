package org.neuron.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.neuron.controller.ConnectionController;
import org.neuron.controller.DispatcherController;
import org.neuron.log.MyLogger;

public class Acceptor {
	private ServerSocketChannel serverSocketChannel;
	private DispatcherController dispatcherController;
	private ConnectionController connectionController;
	
	public Acceptor(InetSocketAddress address){
		connectionController=new ConnectionController();
		try {
			serverSocketChannel=ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true);
			serverSocketChannel.socket().setSoTimeout(0);
			serverSocketChannel.socket().setReuseAddress(true);
			serverSocketChannel.socket().bind(address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.severeLog("create server socket failed");
		}
		dispatcherController=new DispatcherController();
	}

	public void listen(){
		while(serverSocketChannel.isOpen()){
			try {
				SocketChannel channel=serverSocketChannel.accept();
				Dispatcher dispatcher=dispatcherController.getDispatcher();
				connectionController.addConnection(new NonBlockingConnection(channel, dispatcher));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MyLogger.severeLog("server socket process error");
			}
		}
	}
	
	public void close(){
		try {
			serverSocketChannel.close();
			dispatcherController.close();
			connectionController.close();
		} catch (IOException e) {
			e.printStackTrace();
			MyLogger.severeLog("acceptor close failed");
		}
	}
}
