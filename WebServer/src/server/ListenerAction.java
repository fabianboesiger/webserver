package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface ListenerAction {
	
	public Response act(Session session, ArrayList <String> groups, HashMap <String, String> parameters) throws IOException;
	
}
