package org.neuron.core;

import org.neuron.log.MyLogger;

public class ShutdownHook extends Thread{

	private Runtime runtime;
	private IServer server;
	
	public ShutdownHook(IServer server){
		this.server=server;
	}
	
	public void run(){
		if(server!=null){
			server.close();
		}
	}
	
	public void register(){
		runtime=runtime.getRuntime();
		runtime.addShutdownHook(this);
		MyLogger.fineLog("add the server shut down hook");
	}
	
	public void unregister(){
		if(runtime!=null){
			runtime.removeShutdownHook(this);
			MyLogger.fineLog("remove the server shut down hook");
		}
	}
}
