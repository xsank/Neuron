package org.neuron.core;

import java.util.concurrent.LinkedBlockingDeque;

import org.neuron.log.MyLogger;


@SuppressWarnings("serial")
public class TaskQueue extends LinkedBlockingDeque<Runnable>{

	private static final int QUEUE_SIZE=100;
	
	private WorkerPool workerPool;
	
	public TaskQueue(){
		super(QUEUE_SIZE);
	}
	
	public TaskQueue(int capacity){
		super(capacity);
	}
	
	public void init(WorkerPool workerPool){
		this.workerPool=workerPool;
	}
	
	public boolean offer(Runnable task){
		if(workerPool.getActiveCount()<workerPool.getPoolSize()){
			MyLogger.fineLog("add task to work pool");
			return super.offer(task);
		}
		if(workerPool.getPoolSize()>=workerPool.getMaximumPoolSize()){
			MyLogger.infoLog("add task to queue but waiting");
			return super.offer(task);
		}else{
			MyLogger.warningLog("reject the task for no worker idel");
			return false;
		}
	}
}
