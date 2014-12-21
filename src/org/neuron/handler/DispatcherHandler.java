package org.neuron.handler;

import java.nio.channels.SelectionKey;

public class DispatcherHandler {

	public void handle(SelectionKey key){
		if(key.isValid()){
			if(key.isReadable()){
				handleReadEvent();
			}
			if(key.isWritable()){
				handleWriteEvent();
			}
		}
	}
	
	public void handleReadEvent(){
		
	}
	
	public void handleWriteEvent(){
		
	}
}
