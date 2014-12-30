package org.neuron.handler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.neuron.log.MyLogger;

public class RejectHandler implements RejectedExecutionHandler{

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		MyLogger.infoLog(r," is rejected\n","work pool status:",executor);
	}

}
