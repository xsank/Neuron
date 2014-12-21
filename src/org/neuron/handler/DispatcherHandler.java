package org.neuron.handler;

import java.nio.channels.SelectionKey;

import org.neuron.core.NonBlockingConnection;

public class DispatcherHandler {

	public void handle(SelectionKey key){
		if(key.isValid()){
			NonBlockingConnection connection=(NonBlockingConnection) key.attachment();
			if(key.isReadable()){
				handleReadEvent(connection);
			}
			if(key.isWritable()){
				handleWriteEvent(connection);
			}
		}else{
			key.cancel();
		}
	}
	
	public void handleReadEvent(NonBlockingConnection connection){
		connection.getConnectionHandler().handleReadData();
	}
	
	public void handleWriteEvent(NonBlockingConnection connection){
		connection.getConnectionHandler().handleWriteData();
	}
}
