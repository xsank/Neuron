package org.neuron.handler;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import org.neuron.core.NonBlockingConnection;
import org.neuron.core.NonblockingData;
import org.neuron.log.MyLogger;


public class ConnectionHandler {
	private NonBlockingConnection connection;
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;
	private NonblockingData data;
	private static final int BUFFER_SIZE=6;
	
	public ConnectionHandler(NonBlockingConnection connection){
		this.connection=connection;
		this.data=connection.getConnectionData();
	}

	public void handleReadData(){
		int read=0;
		readBuffer=ByteBuffer.allocate(BUFFER_SIZE);
		try {
			read=connection.getSocketChannel().read(readBuffer);
			data.appendDataToReadQueue(readBuffer);
			MyLogger.infoLog("read data size:",read);
		} catch (IOException e) {
			SelectionKey key=connection.getSelectionKey();
			if(key!=null && key.isValid()){
				key.cancel();
				MyLogger.infoLog("client close socket forcely");
			}
			readBuffer.clear();
			e.printStackTrace();
			MyLogger.severeLog("read from socket channel error");
		}
	}
	
	public void handleWriteData(){
		
	}
	
	public void refreshReadData(){
		this.data.refreshReadData();
	}
}
