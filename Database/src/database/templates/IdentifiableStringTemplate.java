package database.templates;

import database.Messages;

public class IdentifiableStringTemplate extends StringTemplate implements Identifiable {
	
	public IdentifiableStringTemplate(String name, Integer minimumLength, Integer maximumLength, SaveAction saveAction) {
		super(name, minimumLength, maximumLength, saveAction);
	}
	
	public IdentifiableStringTemplate(String name, Integer minimumLength, Integer maximumLength) {
		this(name, minimumLength, maximumLength, null);
	}
	
	public IdentifiableStringTemplate(String name, SaveAction saveAction) {
		this(name, null, null, saveAction);
	}

	public IdentifiableStringTemplate(String name) {
		this(name, null);
	}
	
	@Override
	public boolean validate(Messages messages) {
		boolean valid = true;
		if(value == null) {
			messages.add(name, "not-initialized");
			valid = false;
		} else {
			for(int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if(((int) c) >= 256 || Character.isISOControl(c)) {
					messages.add(name, "invalid-characters");
					valid = false;
				}
			}
			if(value.length() < 1) {
				messages.add(name, "minimum-length-exceeded");
				valid = false;
			} else
			if(minimumLength != null) {
				if(value.length() < minimumLength) {
					messages.add(name, "minimum-length-exceeded");
					valid = false;
				}
			}
			if(value.length() >= 128) {
				messages.add(name, "maximum-length-exceeded");
				valid = false;
			} else
			if(maximumLength != null) {
				if(value.length() > maximumLength) {
					messages.add(name, "maximum-length-exceeded");
					valid = false;
				}
			}
		}
		return valid;
	}

	@Override
	public void set(Object object) {
		value = (String) object;
	}

	@Override
	public Object get() {
		return value;
	}

	@Override
	public String getId() {
		return value;
	}
	
}
