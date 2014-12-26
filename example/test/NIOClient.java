package test;

import org.neuron.core.Client;
import org.neuron.core.IServer;
import org.neuron.core.NonBlockingConnection;
import org.neuron.handler.ILogicHandler;

public class NIOClient {

	public static class ClientHandler implements ILogicHandler{

		@Override
		public void handle(NonBlockingConnection connection) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static void main(String[] args){
		Client client=new Client("127.0.0.1", 7777, new ClientHandler());
		client.start();
	}
}
