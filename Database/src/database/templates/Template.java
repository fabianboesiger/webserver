package database.templates;

import java.lang.reflect.Field;
import java.util.Map;

import database.Database;
import database.Messages;

public abstract class Template {
	
	protected static final char SEPARATION_CHARACTER = ',';
	
	protected transient String name;
	protected transient UpdateAction updateAction;
	protected transient boolean updated;
	
	public Template(String name) {
		this(name, null);
	}
	
	public Template(String name, UpdateAction updateAction) {
		this.name = name;
		this.updateAction = updateAction;
		this.updated = false;
	}
	
	public void setField(Database database, Field field, String value, Map <String, ObjectTemplate> initialized, boolean wasUpdated) throws IllegalArgumentException, IllegalAccessException, Exception {
		Object object = field.get(this);
		if(object instanceof Template) {
			if(object instanceof ObjectTemplate && database != null && initialized != null) {
				if(initialized.containsKey(value)) {
					field.set(this, initialized.get(value));
					return;
				} else {
					((Template) object).parse(database, value, initialized);
					((Template) object).updated = wasUpdated;
					initialized.put(value, (ObjectTemplate) object);
				}
			} else {
				((Template) object).parse(database, value, initialized);
				((Template) object).updated = wasUpdated;
			}
		}
	}
	
	public abstract boolean validate(Messages messages);
	public abstract String render(Database database) throws Exception;
	public abstract void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initalized) throws Exception;
	
	public void parse(Database database, String string, Map <String, ObjectTemplate> initialized) throws Exception {
		parse(database, new StringBuilder(string), initialized);
	}
	
	protected String crop(StringBuilder string) {
		int index = string.indexOf("" + SEPARATION_CHARACTER);
		String output = "";
		if(index >= 0) {
			output = string.substring(0, index);
			string.delete(0, index);
		} else {
			output = string.toString();
			string.setLength(0);
		}
		return output;
	}
	
}
