package test;

import org.neuron.core.NonBlockingConnection;
import org.neuron.core.Server;
import org.neuron.handler.ILogicHandler;
import org.neuron.log.MyLogger;

public class NIOServer {

	public static class ServerHandler implements ILogicHandler{

		@Override
		public void handle(NonBlockingConnection connection) {
			// TODO Auto-generated method stub
			byte[] data=connection.getConnectionData().getReadDataBytes();
			MyLogger.infoLog("server recieve data:",new String(data));
		}
		
	}
	
	public static void main(String[] args){
		Server server=new Server("127.0.0.1", 7777, new ServerHandler());
		server.start();
	}
}
