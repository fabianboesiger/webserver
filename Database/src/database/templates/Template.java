package database.templates;

import java.lang.reflect.Field;
import java.util.Map;

import database.Database;
import database.Messages;

public abstract class Template {	
	
	protected transient String name;
	protected transient SaveAction saveAction;
	
	public Template(String name) {
		this(name, null);
	}
	
	public Template(String name, SaveAction saveAction) {
		this.name = name;
		this.saveAction = saveAction;
	}
	
	public void setField(Database database, Field field, String value, Map <String, ObjectTemplate> initialized) throws IllegalArgumentException, IllegalAccessException, Exception {
		Object object = field.get(this);
		if(object instanceof Template) {
			if(object instanceof ObjectTemplate && database != null && initialized != null) {
				if(initialized.containsKey(value)) {
					field.set(this, initialized.get(value));
					return;
				} else {
					((Template) object).parse(database, value, initialized);
					initialized.put(value, (ObjectTemplate) object);
				}
			} else {
				((Template) object).parse(database, value, initialized);
			}
		}
	}
	
	public abstract boolean validate(Messages messages);
	public abstract String render(Database database) throws Exception;
	public abstract void parse(Database database, String string, Map <String, ObjectTemplate> initalized) throws Exception;
	
}
