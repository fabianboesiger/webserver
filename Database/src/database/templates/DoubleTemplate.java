package database.templates;

import java.util.Map;

import database.Database;
import database.validator.Validator;

public class DoubleTemplate extends NumberTemplate <Double> {
	
	public DoubleTemplate(String name, Double minimum, Double maximum, boolean notNull) {
		super(name);
		this.minimum = minimum;
		this.maximum = maximum;
		this.notNull = notNull;
	}
	
	public DoubleTemplate(String name, Double minimum, Double maximum) {
		this(name, minimum, maximum, true);
	}
		
	public DoubleTemplate(String name) {
		this(name, null, null);
	}
	
	public DoubleTemplate() {
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
	public void set(Double object) {
		updated();
		value = object;
	}

	@Override
	public Double get() {
		return value;
	}

	@Override
	public String render(Database database) {
		return value.toString();
	}

	@Override
	public void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initialized) {
		parsed();
		value = Double.parseDouble(crop(string));
	}
	

}
