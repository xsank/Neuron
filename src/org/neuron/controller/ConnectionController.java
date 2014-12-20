package org.neuron.controller;

import java.util.ArrayList;
import java.util.List;

import org.neuron.core.NonBlockingConnection;

public class ConnectionController {

	private List<NonBlockingConnection> connections;
	
	public ConnectionController(){
		connections=new ArrayList<>();
	}
	
	public void addConnection(NonBlockingConnection connection){
		connections.add(connection);
	}
	
	public void close(){
		for(NonBlockingConnection connection:connections){
			connection.close();
		}
		connections.clear();
	}
}
