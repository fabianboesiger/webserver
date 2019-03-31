package database.templates;

import java.util.Map;
import java.util.function.Supplier;

import database.Database;
import database.validator.Validator;

public class ObjectTemplateReference <T extends ObjectTemplate> extends ComplexTemplate {
	
	private T value;
	private Supplier <T> supplier;
	
	public ObjectTemplateReference(String name, Supplier <T> supplier) {
		super(name);
		this.supplier = supplier;
		value = null;
	}
	
	public ObjectTemplateReference(Supplier <T> supplier) {
		this(null, supplier);
	}

	@SuppressWarnings("unchecked")
	public void set(Object object) {
		updated();
		value = (T) object;
	}

	public Object get() {
		return value;
	}

	@Override
	public boolean validate(Validator validator) {
		if(value != null) {
			return value.validate(validator);
		} else {
			validator.addMessage(templateName, "does-not-exist");
			return false;
		}
	}
	
	@Override
	public boolean validate() {
		return value.validate(null);
	}

	@Override
	public String render(Database database) throws Exception {
		if(value != null) {
			return value.render(database);
		} else {
			return "null";
		}
	}

	@Override
	public void parse(Database database, StringBuilder string, Map<String, ObjectTemplate> initialized) throws Exception {
		parsed();
		value = supplier.get();
		value.parse(database, string, initialized);
	}

	@Override
	public void checkIfUpdated() {
		if(value != null) {
			value.checkIfUpdated();
		}
	}


	@Override
	public boolean check(Database database, boolean overwrite) {
		if(value != null) {
			return value.check(database, overwrite);
		} else {
			return true;
		}
	}

}
