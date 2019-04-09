package database.templates;

import java.util.Map;
import java.util.function.Supplier;

import database.Database;
import database.validator.Validator;

public abstract class Template {
	
	protected static final char SEPARATION_CHARACTER = ',';
	
	protected String templateName;
	protected UpdateAction updateAction;
	protected boolean updated;
	
	public Template(String name) {
		this(name, null);
	}
	
	public Template(String name, UpdateAction updateAction) {
		this.templateName = name;
		this.updateAction = updateAction;
		this.updated = true;
	}
	/*
	public void setField(Database database, Field field, String value, Map <String, ObjectTemplate> initialized, boolean wasUpdated) throws Exception {
		Object object = field.get(this);
		if(object instanceof Template) {
			if(object instanceof ObjectTemplateReference && database != null && initialized != null) {
				if(initialized.containsKey(value)) {
					ObjectTemplateReference <?> reference = ((ObjectTemplateReference <?>) field.get(this));
					reference.set(initialized.get(value));
					field.set(this, reference);
					return;
				} else {
					if(!value.equals("null")) {
						((Template) object).parse(database, value, initialized);
						((Template) object).updated = wasUpdated;
						initialized.put(value, (ObjectTemplate) ((ObjectTemplateReference <?>) object).get());
					}
				}
			} else { 
				((Template) object).parse(database, value, initialized);
				((Template) object).updated = wasUpdated;
			}
		}
	}
	*/
	public ObjectTemplate checkIfInitialized(ObjectTemplate input, Database database, String value, Map <String, ObjectTemplate> initialized, boolean wasUpdated, Supplier <?> supplier) throws Exception {
		if(initialized.containsKey(value) && ((supplier != null && initialized.get(value).getClass() == supplier.get().getClass()) || (input != null && initialized.get(value).getClass() == input.getClass()))) {
			return initialized.get(value);
		} else {
			if(!value.equals("null")) {
				if(input == null && supplier != null) {
					input = (ObjectTemplate) supplier.get();
				}
				input.parse(database, value, initialized);
				input.updated = wasUpdated;
				initialized.put(value, input);
				return input;
			} else {
				return null;
			}
		}
	}
	
	public abstract boolean validate(Validator validator);
	public abstract boolean validate();
	public abstract String render(Database database) throws Exception;
	public abstract void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initalized) throws Exception;
	
	public void updated() {
		updated = true;
	}
	
	public void parsed() {
		updated = false;
	}
	
	
	public void parse(Database database, String string, Map <String, ObjectTemplate> initialized) throws Exception {
		parse(database, new StringBuilder(string), initialized);
	}
	
	protected String crop(StringBuilder string) {
		int index = string.indexOf("" + SEPARATION_CHARACTER);
		String output = "";
		if(index >= 0) {
			output = string.substring(0, index);
			string.delete(0, index + 1);
		} else {
			output = string.toString();
			string.setLength(0);
		}
		return output;
	}
	

	public boolean wasUpdated() {
		return updated;
	}
	
}
