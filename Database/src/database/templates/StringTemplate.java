package database.templates;

public class StringTemplate extends PrimitiveTemplate {
		
	private String value;
	private transient Integer minimumLength;
	private transient Integer maximumLength;
	private transient boolean notNull;
	private static final char ESCAPE_CHARACTER = '\\';
	private static final char[] RAW = {'\t', '\b', '\n', '\r', '\f', '"', '\\'};
	private static final char[] ESCAPED = {'t', 'b', 'n', 'r', 'f', '"', '\\'};
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength, boolean notNull) {
		super(name);
		this.minimumLength = minimumLength;
		this.maximumLength = maximumLength;
		this.notNull = notNull;
	}
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength) {
		this(name, minimumLength, maximumLength, true);
	}
	
	public StringTemplate(String name) {
		this(name, null, null);
	}
	
	@Override
	public boolean validate(Errors errors) {
		boolean valid = true;
		if(value == null) {
			if(notNull) {
				errors.add(name, "not-initialized");
				valid = false;
			}
		} else {
			if(minimumLength != null) {
				if(value.length() < minimumLength) {
					errors.add(name, "minimum-length-exceeded");
					valid = false;
				}
			}
			if(maximumLength != null) {
				if(value.length() > maximumLength) {
					errors.add(name, "maximum-length-exceeded");
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
	public String render() {
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
		return "\"" + replaced.toString() + "\"";
	}

	@Override
	public void parse(String string) {
		String temporary = null;
		String trimmed = string.trim();
		if(trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
			temporary = trimmed.substring(1, trimmed.length() - 1);
		} else {
			temporary = string;
		}
		StringBuilder replaced = new StringBuilder();
		boolean escaped = false;
		for(int i = 0; i < temporary.length(); i++) {
			if(!escaped) {
				if(temporary.charAt(i) == ESCAPE_CHARACTER) {
					escaped = true;
				} else {
					replaced.append(temporary.charAt(i));
				}
			} else {
				for(int j = 0; j < ESCAPED.length; j++) {
					if(temporary.charAt(i) == ESCAPED[j]) {
						replaced.append(RAW[j]);
						break;
					}
				}
				escaped = false;
			}
		}
		value = replaced.toString();
	}
	
}
