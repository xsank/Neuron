package test;

import org.neuron.core.Client;
import org.neuron.core.IServer;
import org.neuron.core.NonBlockingConnection;
import org.neuron.handler.ILogicHandler;
import org.neuron.log.MyLogger;

public class NIOClient {

	public static class ClientHandler implements ILogicHandler{

		@Override
		public void handle(NonBlockingConnection connection) {
			byte[] data=connection.getConnectionData().getReadDataBytes();
			MyLogger.infoLog("client recieve result:",new String(data));
		}
	}
	
	public static void main(String[] args) throws InterruptedException{
		Client client=new Client("127.0.0.1", 7777, new ClientHandler());
		//then the client will reconnect to the server
		client.setReconnect();
		client.start();
		
		//wait for the connection established
		Thread.sleep(2048);
		client.syncWrite("test");
	}
}
