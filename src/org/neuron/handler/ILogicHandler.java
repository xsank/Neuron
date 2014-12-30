package org.neuron.handler;

import org.neuron.core.NonBlockingConnection;

public interface ILogicHandler {
	/*
	 * All the logic handler should implement this interface and
	 * should be added to the client or the server.
	 */
	public void handle(NonBlockingConnection connection);
}
