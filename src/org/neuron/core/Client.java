package org.neuron.core;

import java.net.InetSocketAddress;

import org.neuron.handler.ILogicHandler;
import org.neuron.log.MyLogger;

public class Client implements IServer{

	private Connecter connecter;
	private LogicAdapter logicAdapter;
	private ILogicHandler logicHanlder;
	
	private String addr;
	private int port;
	
	public Client(String addr,int port){
		this.addr=addr;
		this.port=port;
		this.logicAdapter=new LogicAdapter(new WorkerPool());
		this.connecter=new Connecter(new InetSocketAddress(addr, port));
	}
	
	public Client(String addr,int port,ILogicHandler logicHandler){
		this(addr, port);
		this.logicHanlder=logicHandler;
		this.logicAdapter.setLogicHandler(logicHandler);
		this.connecter.addLogicAdapter(logicAdapter);
	}
	
	public void setLogicHandler(ILogicHandler logicHandler){
		this.logicAdapter.setLogicHandler(logicHandler);
		this.logicHanlder=logicHandler;
		this.connecter.addLogicAdapter(logicAdapter);
	}
	
	@Override
	public void run(){
		ShutdownHook shutdownHook=new ShutdownHook(this);
		try{
			shutdownHook.register();
			connecter.listen();
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
	
	public void syncWrite(String s){
		connecter.getConnection().syncWrite(s);
	}
	
	public void close(){
		logicAdapter.close();
		connecter.close();
	}
}
