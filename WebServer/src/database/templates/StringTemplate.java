package database.templates;

import java.io.Serializable;

public class StringTemplate implements Template, ObjectTemplateField, Serializable {
	
	private static final long serialVersionUID = 8501305997513292687L;
	
	private String value;
	private transient Integer minimumLength;
	private transient Integer maximumLength;
	private transient boolean notNull;
	
	public StringTemplate(int minimumLength, int maximumLength, boolean notNull) {
		this.minimumLength = minimumLength;
		this.maximumLength = maximumLength;
		this.notNull = notNull;
	}
	
	public StringTemplate(Integer minimumLength, Integer maximumLength) {
		this(minimumLength, maximumLength, true);
	}
	
	public StringTemplate() {
		this(null, null);
	}

	@Override
	public boolean validate() {
		if(notNull) {
			
		}
		if(minimumLength != null) {
			
		}
		if(maximumLength != null) {
			
		}
		return true;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public void fromString(String string) {
		value = string;
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
