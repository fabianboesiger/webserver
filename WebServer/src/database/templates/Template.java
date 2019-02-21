package database.templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Template {
	
	public void set(Object object);
	public Object get();
	public boolean validate(List <Map <String, String>> errors);

	default void addError(List <Map <String, String>> errors, String name, String message) {
		HashMap <String, String> error = new HashMap <String, String> ();
		error.put("name", name);
		error.put("message", message);
		errors.add(error);
	}
	
}
