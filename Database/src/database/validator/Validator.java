package database.validator;

import java.util.HashMap;
import java.util.LinkedList;

import database.Database;
import database.templates.ListTemplate;
import database.templates.ObjectTemplate;
import database.templates.StringTemplate;

public class Validator extends ObjectTemplate {
	
	public static final String NAME = "validators";
	
	private StringTemplate name;
	private ListTemplate <Message> messages;
	
	public Validator() {
		name = new StringTemplate("name");
		messages = new ListTemplate <Message> ("messages", Message::new);
	}
	
	public Validator(String name) {
		this();
		this.name.set(name);
	}
	
	public void addMessage(String name, String message) {
		Message newMessage = new Message();
		newMessage.set(name, message);
		messages.add(newMessage);
	}

	public void addToVariables(HashMap <String, Object> variables) {
		LinkedList <HashMap <String, Object>> output = new LinkedList <HashMap <String, Object>> ();
		for(Message message : messages) {
			output.add(message.get());
		}
		variables.put((String) name.get(), output);
	}

	public String getName() {
		return (String) name.get();
	}

	public void delete(Database database) {
		for(Message message : messages) {
			database.deleteId(Message.class, message.getId());
		}
		database.deleteId(Validator.class, getId());
	}
	
}
