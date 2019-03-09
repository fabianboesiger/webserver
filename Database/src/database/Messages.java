package database;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Messages {
	
	private LinkedList <Map <String, String>> messages;
	
	public Messages() {
		messages = new LinkedList <Map <String, String>> ();
	}
	
	public void add(String name, String message) {
		HashMap <String, String> pair = new HashMap <String, String> ();
		pair.put("name", name);
		pair.put("message", message);
		messages.add(pair);
	}
	
	public List <Map <String, String>> get() {
		return messages;
	}
	
	public void addToVariables(HashMap <String, Object> variables, String name) {
		List <Map <String, String>> list = get();
		if(list.size() > 0) {
			variables.put(name, list);
		}
	}
	
	@Override
	public String toString() {
		return messages.toString();
	}
	
}
