package org.neuron.handler;

import org.neuron.core.NonBlockingConnection;

public interface ILogicHandler {
	public void handle(NonBlockingConnection connection);
}
