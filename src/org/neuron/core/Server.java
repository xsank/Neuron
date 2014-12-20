package org.neuron.core;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.neuron.controller.ConnectionController;
import org.neuron.log.MyLogger;

public class Server implements Runnable{

	private Acceptor acceptor;
	private ExecutorService workerpool;
	private LogicHanlder logicHanlder;
	
	private String addr;
	private int port;
	
	public Server(String addr,int port){
		this.addr=addr;
		this.port=port;
		this.workerpool=new WorkerPool();
		this.acceptor=new Acceptor(new InetSocketAddress(addr,port));
	}

	public Server(String addr,int port,LogicHanlder logicHanlder){
		this(addr, port);
		this.logicHanlder=logicHanlder;
	}
	
	public void setLogicHandler(LogicHanlder logicHanlder){
		this.logicHanlder=logicHanlder;
	}

	@Override
	public void run() {
		ShutdownHook shutdownHook=new ShutdownHook(this);
		try{
			shutdownHook.register();
			acceptor.listen();
		}finally{
			shutdownHook.unregister();
			close();
		}
	}
	
	public void start(){
		if(logicHanlder==null){
			MyLogger.severeLog("no logic handler has been set");
			return;
		}
		Thread thread=new Thread(this);
		thread.start();
	}
	
	public void close(){
		
	}
}
