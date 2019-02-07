package database.structures;

import java.io.Serializable;

public class IntegerTemplate implements Template, ObjectTemplateField, Serializable {

	private static final long serialVersionUID = -1341999161149872249L;
	
	private Integer value;
	private transient Integer minimum;
	private transient Integer maximum;
	private transient boolean notNull;
	
	public IntegerTemplate(int minimum, int maximum, boolean notNull) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.notNull = notNull;
	}
	
	public IntegerTemplate(Integer minimum, Integer maximum) {
		this(minimum, maximum, true);
	}
	
	public IntegerTemplate() {
		this(null, null);
	}

	@Override
	public boolean validate() {
		if(notNull) {
			
		}
		if(minimum != null) {
			
		}
		if(maximum != null) {
			
		}
		return true;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public void fromString(String string) {
		value = Integer.parseInt(string);
	}

	@Override
	public void set(Object object) {
		value = (Integer) object;
	}

	@Override
	public Object get() {
		return value;
	}
	
}
