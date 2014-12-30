package org.neuron.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.neuron.handler.RejectHandler;

public class WorkerPool extends ThreadPoolExecutor{
	private static final int CORE_SIZE=Runtime.getRuntime().availableProcessors();
	private static final int MAX_SIZE=CORE_SIZE*10;
	private static final int KEEP_ALIVE_TIME=60;
	
	public WorkerPool(){
		this(CORE_SIZE, MAX_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new TaskQueue());
	}
	
	public WorkerPool(int corePoolSize,int maximumPoolSize,long keepAliveTime,int capacity){
		this(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new TaskQueue(capacity));
	}

	public WorkerPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,new RejectHandler());
		((TaskQueue)getQueue()).init(this);
	}
}
