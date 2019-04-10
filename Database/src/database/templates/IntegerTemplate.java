package database.templates;

import java.util.Map;

import database.Database;
import database.validator.Validator;

public class IntegerTemplate extends NumberTemplate <Integer> implements Identifiable {
	
	public IntegerTemplate(String name, Integer minimum, Integer maximum, boolean notNull) {
		super(name);
		this.minimum = minimum;
		this.maximum = maximum;
		this.notNull = notNull;
	}
	
	public IntegerTemplate(String name, Integer minimum, Integer maximum) {
		this(name, minimum, maximum, true);
	}
	
	public IntegerTemplate(String name) {
		this(name, null, null);
	}
	
	public IntegerTemplate() {
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
			} else {
				if(minimum != null) {
					if(value < minimum) {
						valid = false;
						if(validator != null) {
							validator.addMessage(templateName, "minimum-exceeded");
						}
					}
				}
				if(maximum != null) {
					if(value > maximum) {
						valid = false;
						if(validator != null) {
							validator.addMessage(templateName, "maximum-exceeded");
						}
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
	public void set(Integer object) {
		updated();
		value = object;
	}

	@Override
	public Integer get() {
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
		value = Integer.parseInt(crop(string));
	}
	
}
