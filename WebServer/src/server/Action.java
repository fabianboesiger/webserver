package server;

import server.handlers.Response;

public interface Action {
	
	public Response act(Session session);
	
}
