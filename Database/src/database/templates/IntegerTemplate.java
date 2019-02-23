package database.templates;

public class IntegerTemplate extends PrimitiveTemplate implements Identifiable {
	
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
	
	public IntegerTemplate(String name, Integer minimum, Integer maximum) {
		this(name, minimum, maximum, true);
	}
	
	public IntegerTemplate(String name) {
		this(name, null, null);
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
	public void set(Object object) {
		value = (Integer) object;
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
	public String render() {
		return value.toString();
	}

	@Override
	public void parse(String string) {
		value = Integer.parseInt(string);
	}
	
}
