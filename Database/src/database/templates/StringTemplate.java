package database.templates;

import java.util.Map;

import database.Database;
import database.validator.Validator;

public class StringTemplate extends PrimitiveTemplate {
		
	protected String value;
	protected Integer minimumLength;
	protected Integer maximumLength;
	protected boolean notNull;
	private static final char STRING_CHARACTER = '"';
	private static final char ESCAPE_CHARACTER = '\\';
	private static final char[] RAW = {'\t', '\b', '\n', '\r', '\f', '"', '\\'};
	private static final char[] ESCAPED = {'t', 'b', 'n', 'r', 'f', '"', '\\'};
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength, boolean notNull, UpdateAction updateAction) {
		super(name, updateAction);
		this.minimumLength = minimumLength;
		this.maximumLength = maximumLength;
		this.notNull = notNull;
	}
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength, UpdateAction updateAction) {
		this(name, minimumLength, maximumLength, true, updateAction);
	}
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength, boolean notNull) {
		this(name, minimumLength, maximumLength, notNull, null);
	}
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength) {
		this(name, minimumLength, maximumLength, true, null);
	}
	
	public StringTemplate(String name, boolean notNull, UpdateAction updateAction) {
		this(name, null, null, notNull, updateAction);
	}
	
	public StringTemplate(String name, UpdateAction updateAction) {
		this(name, null, null, updateAction);
	}
	
	public StringTemplate(String name, boolean notNull) {
		this(name, null, null, notNull);
	}
	
	public StringTemplate(String name) {
		this(name, null, null);
	}
	
	public StringTemplate() {
		this(null);
	}
	
	@Override
	public boolean validate(Validator validator) {
		boolean valid = true;
		if(updated) {
			if(value == null) {
				if(notNull) {
					if(validator != null) {
						validator.addMessage(templateName, "not-initialized");
					}
					valid = false;
				}
			} else {
				if(minimumLength != null) {
					if(value.length() < minimumLength) {
						if(validator != null) {
							validator.addMessage(templateName, "minimum-length-exceeded");
						}
						valid = false;
					}
				}
				if(maximumLength != null) {
					if(value.length() > maximumLength) {
						if(validator != null) {
							validator.addMessage(templateName, "maximum-length-exceeded");
						}
						valid = false;
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
		value = (String) object;
	}

	@Override
	public Object get() {
		return value;
	}

	@Override
	public String render(Database database) throws Exception {
		StringBuilder replaced = new StringBuilder();
		for(int i = 0; i < value.length(); i++) {
			boolean found = false;
			for(int j = 0; j < RAW.length; j++) {
				if(value.charAt(i) == RAW[j]) {
					replaced.append(ESCAPE_CHARACTER);
					replaced.append(ESCAPED[j]);
					found = true;
					break;
				}
			}
			if(!found) {
				replaced.append(value.charAt(i));
			}
		}
		
		String output = replaced.toString();
		if(updated && updateAction != null) {
			output = (String) updateAction.act(output);
		}
		
		return "\"" + output + "\"";
	}

	@Override
	public void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initialized) throws Exception {
		parsed();
		
		StringBuilder builder = new StringBuilder();
	
		boolean escaped = false;
		boolean started = false;

		String trimmed = string.toString().trim();
		if(!trimmed.startsWith("\"") && !trimmed.endsWith("\"")) {
			started = true;
		}
		
		while(string.length() > 0) {
			if(started) {
				if(!escaped) {
					if(string.charAt(0) == STRING_CHARACTER) {
						string.deleteCharAt(0);
						
						while(string.length() > 0) {
							if(string.charAt(0) == SEPARATION_CHARACTER){
								string.deleteCharAt(0);
								break;
							} else {
								string.deleteCharAt(0);
							}
						}
						
						break;
					}
					if(string.charAt(0) == ESCAPE_CHARACTER) {
						escaped = true;
					} else {
						builder.append(string.charAt(0));
					}
				} else {
					for(int j = 0; j < ESCAPED.length; j++) {
						if(string.charAt(0) == ESCAPED[j]) {
							builder.append(RAW[j]);
							break;
						}
					}
					escaped = false;
				}
			} else {
				if(string.charAt(0) == STRING_CHARACTER) {
					started = true;
				}
			}
			string.deleteCharAt(0);
		}
		value = builder.toString();
	}
	
}
