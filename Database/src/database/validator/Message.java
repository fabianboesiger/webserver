package database.validator;

import java.util.HashMap;

import database.templates.ObjectTemplate;
import database.templates.StringTemplate;

public class Message extends ObjectTemplate {
	
	public static final String NAME = "messages";
	
	private StringTemplate name;
	private StringTemplate message;
	
	public Message() {
		name = new StringTemplate("name");
		message = new StringTemplate("message");
	}
	
	public void set(String name, String message) {
		this.name.set(name);
		this.message.set(message);
	}

	public HashMap <String, Object> get() {
		HashMap <String, Object> output = new HashMap <String, Object> ();
		output.put("name", name.get());
		output.put("message", message.get());
		return output;
	}
	
}
