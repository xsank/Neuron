package test;

import org.neuron.core.NonBlockingConnection;
import org.neuron.core.Server;
import org.neuron.handler.ILogicHandler;

public class NIOServer {

	public static class ServerHandler implements ILogicHandler{

		@Override
		public void handle(NonBlockingConnection connection) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static void main(String[] args){
		Server server=new Server("127.0.0.1", 7777, new ServerHandler());
		server.start();
	}
}
