package database.templates;

import java.util.ArrayList;
import java.util.Map;

import database.Database;

public class ArrayTemplate extends PrimitiveTemplate {
	
	ArrayList <Template> values;
	private transient boolean notNull;
	private transient Integer maximumSize;
	private transient Integer minimumSize;
	
	public ArrayTemplate(String name, Integer minimumSize, Integer maximumSize, boolean notNull) {
		super(name);
		this.notNull = notNull;
	}
	
	public ArrayTemplate(String name, Integer minimumSize, Integer maximumSize) {
		this(name, minimumSize, maximumSize, true);
	}
	
	public ArrayTemplate(String name) {
		this(name, null, null);
	}

	@Override
	public String render(Database database) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for(int i = 0; i < values.size(); i++) {
			if(i != 0) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(values.get(i).render(database));
		}
		stringBuilder.append("]");
		return null;
	}

	@Override
	public void parse(Database database, String string, Map <String, ObjectTemplate> initialized) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(Errors errors) {
		boolean valid = true;
		if(values == null) {
			if(notNull) {
				valid = false;
				errors.add(name, "not-initialized");
			}
		} else {
			if(minimumSize != null) {
				if(values.size() < minimumSize) {
					valid = false;
					errors.add(name, "minimum-elements-exceeded");
				}
			}
			if(maximumSize != null) {
				if(values.size() > maximumSize) {
					valid = false;
					errors.add(name, "maximum-elements-exceeded");
				}
			}
		}
		return valid;
	}

}
