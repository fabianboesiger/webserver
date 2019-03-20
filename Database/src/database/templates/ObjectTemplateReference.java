package database.templates;

import java.util.Map;
import java.util.function.Supplier;

import database.Database;
import database.Messages;

public class ObjectTemplateReference <T extends ObjectTemplate> extends PrimitiveTemplate {
	
	private T value;
	
	public ObjectTemplateReference(String name, Supplier <T> supplier) {
		super(name);
		value = supplier.get();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void set(Object object) {
		update();
		value = (T) object;
	}

	@Override
	public Object get() {
		return value;
	}

	@Override
	public boolean validate(Messages messages) {
		return value.validate(messages);
	}
	
	@Override
	public boolean validate() {
		return validate(null);
	}

	@Override
	public String render(Database database) throws Exception {
		return value.render(database);
	}

	@Override
	public void parse(Database database, StringBuilder string, Map<String, ObjectTemplate> initialized) throws Exception {
		value.parse(database, string, initialized);
	}

	@Override
	public void update() {
		updated = true;
	}

}
