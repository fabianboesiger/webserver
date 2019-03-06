package database.templates;

import java.util.Map;

import database.Database;
import database.Messages;

public class IdentifiableStringTemplate extends PrimitiveTemplate implements Identifiable {
		
	private String value;
	private transient Integer minimumLength;
	private transient Integer maximumLength;
	
	public IdentifiableStringTemplate(String name, Integer minimumLength, Integer maximumLength, SaveAction saveAction) {
		super(name, saveAction);
		this.minimumLength = minimumLength;
		this.maximumLength = maximumLength;
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

	@Override
	public String render(Database database) throws Exception {
		String output = value;
		if(saveAction != null) {
			output = (String) saveAction.act(output);
		}
		
		return "\"" + output + "\"";
	}

	@Override
	public void parse(Database database, String string, Map <String, ObjectTemplate> initialized) throws Exception {
		String trimmed = string.trim();
		String output;
		if(trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
			output = trimmed.substring(1, trimmed.length() - 1);
		} else {
			output = string;
		}
		value = output;
		if(saveAction != null) {
			value = (String) saveAction.act(value);
		}
	}
	
}
