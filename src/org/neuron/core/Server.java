package org.neuron.core;

import java.net.InetSocketAddress;
import org.neuron.handler.ILogicHandler;
import org.neuron.log.MyLogger;

public class Server implements IServer{

	private Accepter acceptor;
	private LogicAdapter logicAdapter;
	private ILogicHandler logicHanlder;
	
	private String addr;
	private int port;
	
	public Server(String addr,int port){
		this.addr=addr;
		this.port=port;
		this.logicAdapter=new LogicAdapter(new WorkerPool());
		this.acceptor=new Accepter(new InetSocketAddress(addr,port));
	}

	public Server(String addr,int port,ILogicHandler logicHandler){
		this(addr, port);
		this.logicHanlder=logicHandler;
		this.logicAdapter.setLogicHandler(logicHandler);
		this.acceptor.addLogicAdapter(logicAdapter);
	}
	
	public void setLogicHandler(ILogicHandler logicHandler){
		this.logicHanlder=logicHandler;
		this.logicAdapter.setLogicHandler(logicHandler);
		this.acceptor.addLogicAdapter(logicAdapter);
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
		MyLogger.infoLog("server has been stated");
	}
	
	public void close(){
		logicAdapter.close();
		acceptor.close();
	}
}
