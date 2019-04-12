package database.templates;

import database.validator.Validator;

public class IdentifiableStringTemplate extends StringTemplate implements Identifiable {
	
	public IdentifiableStringTemplate(String name, Integer minimumLength, Integer maximumLength, UpdateAction updateAction) {
		super(name, minimumLength, maximumLength, updateAction);
	}
	
	public IdentifiableStringTemplate(String name, Integer minimumLength, Integer maximumLength) {
		this(name, minimumLength, maximumLength, null);
	}
	
	public IdentifiableStringTemplate(String name, UpdateAction updateAction) {
		this(name, null, null, updateAction);
	}

	public IdentifiableStringTemplate(String name) {
		this(name, null);
	}
	
	public IdentifiableStringTemplate() {
		this(null);
	}
	
	@Override
	public boolean validate(Validator validator) {
		
		boolean valid = true;
		if(updated) {
			if(value == null) {
				validator.addMessage(templateName, "not-initialized");
				valid = false;
			} else {
				for(int i = 0; i < value.length(); i++) {
					char c = value.charAt(i);
					if(((int) c) >= 256 || Character.isISOControl(c)) {
						validator.addMessage(templateName, "invalid-characters");
						valid = false;
					}
				}
				if(value.length() < 1) {
					validator.addMessage(templateName, "minimum-length-exceeded");
					valid = false;
				} else
				if(minimumLength != null) {
					if(value.length() < minimumLength) {
						validator.addMessage(templateName, "minimum-length-exceeded");
						valid = false;
					}
				}
				if(value.length() >= 128) {
					validator.addMessage(templateName, "maximum-length-exceeded");
					valid = false;
				} else
				if(maximumLength != null) {
					if(value.length() > maximumLength) {
						validator.addMessage(templateName, "maximum-length-exceeded");
						valid = false;
					}
				}
			}
		}
		return valid;
	}
	
	@Override
	public String getId() {
		return value;
	}
	
}
