package org.neuron.controller;

import java.util.ArrayList;
import java.util.List;


import org.neuron.core.Dispatcher;

public class DispatcherController {
	private static int MAX_SIZE=2;
	
	private List<Dispatcher> dispatchers=new ArrayList<>();
	
	public DispatcherController(){
		this(MAX_SIZE);
	}
	
	public DispatcherController(int size){
		for(int i=0;i<size;i++){
			dispatchers.add(new Dispatcher());
		}
	}
	
	public Dispatcher getDispatcher(){
		int min=Integer.MAX_VALUE;
		int index=0;
		for(int i=0;i<dispatchers.size();i++){
			int tdCount=dispatchers.get(i).getRegisterChannelCount();
			if(tdCount<min){
				min=tdCount;
				index=i;
			}
		}
		return dispatchers.get(index);
	}
	
	public void close(){
		for(Dispatcher dispatcher:dispatchers){
			dispatcher.close();
		}
		dispatchers.clear();
	}
}
