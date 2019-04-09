package database.templates;

import java.util.Map;

import database.Database;
import database.validator.Validator;

public class BooleanTemplate extends PrimitiveTemplate implements Identifiable {
	
	private Boolean value;
	private boolean notNull;
	
	public BooleanTemplate(String name, boolean notNull) {
		super(name);
		this.notNull = notNull;
	}
	
	public BooleanTemplate(String name) {
		this(name, true);
	}
	
	public BooleanTemplate() {
		this(null);
	}

	@Override
	public boolean validate(Validator validator) {
		boolean valid = true;
		if(updated) {
			if(value == null) {
				if(notNull) {
					valid = false;
					if(validator != null) {
						validator.addMessage(templateName, "not-initialized");
					}
				}
			}
		}
		return valid;
	}
	
	@Override
	public boolean validate() {
		return validate(null);
	}

	@Override
	public void set(Object object) {
		updated();
		value = (Boolean) object;
	}

	@Override
	public Object get() {
		return value;
	}

	@Override
	public String getId() {
		return value.toString();
	}

	@Override
	public String render(Database database) {
		return value.toString();
	}

	@Override
	public void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initialized) {
		parsed();
		value = Boolean.parseBoolean(crop(string));
	}
	
}

