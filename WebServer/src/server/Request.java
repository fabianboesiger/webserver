package server;

import java.util.ArrayList;
import java.util.HashMap;

public class Request {
	
	public Session session;
	public ArrayList <String> groups;
	public HashMap <String, String> parameters;
	
	public Request(Session session, ArrayList <String> groups, HashMap <String, String> parameters) {
		this.session = session;
		this.groups = groups;
		this.parameters = parameters;
	}
	
}
