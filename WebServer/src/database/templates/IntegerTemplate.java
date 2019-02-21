package database.templates;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IntegerTemplate implements ObjectTemplateField {
	
	private Integer value;
	private String name;
	private transient Integer minimum;
	private transient Integer maximum;
	private transient boolean notNull;
	
	public IntegerTemplate(String name, Integer minimum, Integer maximum, boolean notNull) {
		this.name = name;
		this.minimum = minimum;
		this.maximum = maximum;
		this.notNull = notNull;
	}
	
	public IntegerTemplate(Integer minimum, Integer maximum, boolean notNull) {
		this(null, minimum, maximum, notNull);
	}
	
	public IntegerTemplate(String name, Integer minimum, Integer maximum) {
		this(name, minimum, maximum, true);
	}
	
	public IntegerTemplate(Integer minimum, Integer maximum) {
		this(minimum, maximum, true);
	}
	
	public IntegerTemplate() {
		this(null, null);
	}

	@Override
	public boolean validate(List <Map <String, String>> errors) {
		boolean valid = true;
		if(value == null) {
			if(notNull) {
				valid = false;
			}
		} else {
			if(minimum != null) {
				if(value < minimum) {
					return false;
				}
			}
			if(maximum != null) {
				if(value > maximum) {
					valid = false;
				}
			}
		}
		return valid;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public void set(Object object) {
		if(object instanceof String) {
			value = Integer.parseInt((String) object);
		} else {
			value = (Integer) object;
		}
	}

	@Override
	public Object get() {
		return value;
	}
	
}
