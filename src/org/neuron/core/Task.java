package org.neuron.core;

import org.neuron.handler.ILogicHandler;

public class Task implements Runnable{
	private NonBlockingConnection connection;
	private ILogicHandler logicHandler;
	
	public Task(NonBlockingConnection connection,ILogicHandler logicHandler){
		this.connection=connection;
		this.logicHandler=logicHandler;
	}

	@Override
	public void run() {
		logicHandler.handle(connection);
	}

}
