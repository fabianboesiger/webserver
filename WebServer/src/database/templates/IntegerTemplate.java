package database.templates;

public class IntegerTemplate extends PrimitiveTemplate {
	
	private Integer value;
	private transient Integer minimum;
	private transient Integer maximum;
	private transient boolean notNull;
	
	public IntegerTemplate(String name, Integer minimum, Integer maximum, boolean notNull) {
		super(name);
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
	public boolean validate(Errors errors) {
		boolean valid = true;
		if(value == null) {
			if(notNull) {
				valid = false;
				errors.add(name, "not-initialized");
			}
		} else {
			if(minimum != null) {
				if(value < minimum) {
					valid = false;
					errors.add(name, "minimum-exceeded");
				}
			}
			if(maximum != null) {
				if(value > maximum) {
					valid = false;
					errors.add(name, "maximum-exceeded");
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
