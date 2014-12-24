package org.neuron.core;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.neuron.handler.DispatcherHandler;
import org.neuron.log.MyLogger;

public class Dispatcher implements Runnable{

	private static final int MAX_SELECT=5000;
	private Selector selector;
	private DispatcherHandler handler;
	private int selectNum;
	
	public Dispatcher(){
		this(MAX_SELECT);
	}
	
	public Dispatcher(int num){
		try {
			selectNum=num;
			selector=Selector.open();
			handler=new DispatcherHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.severeLog("create selector failed");
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

	@Override
	public void run() {
		while(selector.isOpen()){
			try {
				int eventCount=selector.select(MAX_SELECT);
				if(eventCount>0){
					handle();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MyLogger.severeLog("selector process error");
			}
		}
		
	}
	
	public void registerChannel(NonBlockingConnection connection,int ops){
		try {
			connection.getSocketChannel().register(selector, ops, connection);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MyLogger.severeLog("channel register failed");
		}
	}
	
	public int getRegisterChannelCount(){
		return selector.keys().size();
	}
	
	private void handle(){
		Set<SelectionKey> selectedKeys=selector.selectedKeys();
		Iterator<SelectionKey> iterator=selectedKeys.iterator();
		while(iterator.hasNext()){
			SelectionKey key=iterator.next();
			iterator.remove();
			handler.handle(key);
		}
	}
}
