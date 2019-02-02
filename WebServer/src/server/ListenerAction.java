package server;

import java.io.IOException;

public interface ListenerAction {
	
	public Response act(Session session) throws IOException;
	
}
