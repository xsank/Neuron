package org.neuron.handler;

import java.nio.channels.SelectionKey;

import org.neuron.core.NonBlockingConnection;

public class DispatcherHandler {

	public void handle(SelectionKey key){
		NonBlockingConnection connection=(NonBlockingConnection) key.attachment();
		if(key.isValid() && key.isReadable()){
			handleReadEvent(connection);
		}
		if(key.isValid() && key.isWritable()){
			handleWriteEvent(connection);
		}
	}
	
	public void handleReadEvent(NonBlockingConnection connection){
		connection.getConnectionHandler().handleReadData();
		connection.getLogicAdapter().handle(connection);
		connection.getConnectionHandler().refreshReadData();
	}
	
	public void handleWriteEvent(NonBlockingConnection connection){
		connection.getConnectionHandler().handleWriteData();
	}
}
