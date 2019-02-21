package database.templates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StringTemplate implements ObjectTemplateField {
		
	private String value;
	private String name;
	private transient Integer minimumLength;
	private transient Integer maximumLength;
	private transient boolean notNull;
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength, boolean notNull) {
		this.name = name;
		this.minimumLength = minimumLength;
		this.maximumLength = maximumLength;
		this.notNull = notNull;
	}
	
	public StringTemplate(Integer minimumLength, Integer maximumLength, boolean notNull) {
		this(null, minimumLength, maximumLength, notNull);
	}
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength) {
		this(name, minimumLength, maximumLength, true);
	}

	
	public StringTemplate(Integer minimumLength, Integer maximumLength) {
		this(minimumLength, maximumLength, true);
	}
	
	public StringTemplate() {
		this(null, null);
	}

	@Override
	public boolean validate(List <Map <String, String>> errors) {
		boolean valid = true;
		if(value == null) {
			if(notNull) {
				addError(errors, name, "not-initialized");
				return false;
			}
		} else {
			if(minimumLength != null) {
				if(value.length() < minimumLength) {
					addError(errors, name, "minimum-length-exceeded");
					valid = false;
				}
			}
			if(maximumLength != null) {
				if(value.length() > maximumLength) {
					addError(errors, name, "maximum-length-exceeded");
					valid = false;
				}
			}
		}
		return valid;
	}
	
	@Override
	public String toString() {
		return value;
	}

	@Override
	public void set(Object object) {
		value = (String) object;
	}

	@Override
	public Object get() {
		return value;
	}
	
}
