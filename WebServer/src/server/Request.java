package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Request {
	
	public Session session;
	public ArrayList <String> groups;
	public HashMap <String, String> parameters;
	public LinkedList <String> languages;
	
	public Request(Session session, ArrayList <String> groups, HashMap <String, String> parameters, LinkedList <String> languages) {
		this.session = session;
		this.groups = groups;
		this.parameters = parameters;
		this.languages = languages;
	}
	
}
