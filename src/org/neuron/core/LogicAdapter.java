package org.neuron.core;

import java.util.concurrent.ExecutorService;

import org.neuron.handler.ILogicHandler;

public class LogicAdapter {
	private ExecutorService workerpool;
	private ILogicHandler callback;
	
	public LogicAdapter(ExecutorService workerpool){
		this.workerpool=workerpool;
	}
	
	public void setLogicHandler(ILogicHandler logicHandler){
		this.callback=logicHandler;
	}
	
	public LogicAdapter(ExecutorService workerpool,ILogicHandler logicHandler){
		this(workerpool);
		this.callback=logicHandler;
	}
	
	public void handle(NonBlockingConnection connection){
		if(callback!=null){
			workerpool.execute(new Task(connection,callback));
		}
	}
	
	public void close(){
		workerpool.shutdown();
	}
}
