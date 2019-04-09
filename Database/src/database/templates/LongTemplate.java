package database.templates;

import java.util.Map;

import database.Database;
import database.validator.Validator;

public class LongTemplate extends PrimitiveTemplate implements Identifiable {
	
	private Long value;
	private Long minimum;
	private Long maximum;
	private boolean notNull;
	
	public LongTemplate(String name, Long minimum, Long maximum, boolean notNull) {
		super(name);
		this.minimum = minimum;
		this.maximum = maximum;
		this.notNull = notNull;
	}
	
	public LongTemplate(String name, Long minimum, Long maximum) {
		this(name, minimum, maximum, true);
	}
	
	public LongTemplate(String name) {
		this(name, null, null);
	}
	
	public LongTemplate() {
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
	public void set(Object object) {
		updated();
		value = (Long) object;
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
		value = Long.parseLong(crop(string));
	}
	
}
