package database.templates;

public class StringTemplate extends PrimitiveTemplate {
		
	private String value;
	private transient Integer minimumLength;
	private transient Integer maximumLength;
	private transient boolean notNull;
	
	public StringTemplate(String name, Integer minimumLength, Integer maximumLength, boolean notNull) {
		super(name);
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
	public void fromCSV(String string) {
		String trimmed = string.trim();
		if(trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
			value = trimmed.substring(1, trimmed.length() - 1).replaceAll("\"\"", "\"");
		} else {
			value = string.replaceAll("\"\"", "\"");
		}
	}
	
	@Override
	public String toCSV() {
		return "\"" + value.toString().replaceAll("\"", "\"\"") + "\"";
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
