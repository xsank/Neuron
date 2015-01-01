package org.neuron.core;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class WatchDog {

	private ScheduledExecutorService scheduler;
	private static final int DURATION=1;
	
	public WatchDog(){
		scheduler=new ScheduledThreadPoolExecutor(1);
	}
	
	public ScheduledFuture<?> watch(Runnable target){
		return scheduler.scheduleAtFixedRate(target, 0, DURATION, TimeUnit.SECONDS);
	}
	
	public void cancel(ScheduledFuture<?> task){
		if(task!=null && !(task.isCancelled() || task.isDone())){
			task.cancel(true);
		}
	}
	
	public void close(){
		scheduler.shutdown();
	}
}
