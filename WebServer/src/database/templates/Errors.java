package database.templates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Errors {
	
	LinkedList <Map <String, String>> errors;
	
	public Errors() {
		errors = new LinkedList <Map <String, String>> ();
	}
	
	public void add(String name, String message) {
		HashMap <String, String> error = new HashMap <String, String> ();
		error.put("name", name);
		error.put("message", message);
		errors.add(error);
	}
	
	public List <Map <String, String>> get() {
		return errors;
	}
	
}
