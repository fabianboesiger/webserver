package server.handlers;

import com.sun.net.httpserver.HttpExchange;

public class MultiHandler extends Handler {
		
	public MultiHandler() {
	}
	
	public Response getResponse(HttpExchange httpExchange) {
		String method = httpExchange.getRequestMethod();
		String path = httpExchange.getRequestURI().getPath();
		
		return null;
	}
	
}
