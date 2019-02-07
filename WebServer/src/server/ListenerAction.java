package server;

import java.io.IOException;

public interface ListenerAction {
	
	public Response act(Request request) throws IOException;
	
}
