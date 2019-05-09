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

	public void set(T object) {
		updated();
		value = object;
	}

	public T get() {
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
	public String render() throws Exception {
		if(value != null) {
			return value.render();
		} else {
			return "null";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initialized) throws Exception {
		parsed();
		this.database = database;
		//if(database != null && initialized != null) {
		set((T) checkIfInitialized((ObjectTemplate) get(), database, crop(string), initialized, getSupplier()));
		/*} else {
			value = supplier.get();
			System.out.println("!4 "+this);
			value.parse(database, string, initialized);
		}*/
	}

	@Override
	public void checkIfUpdated(Database database) {
		this.database = database;
		if(value != null) {
			value.checkIfUpdated(database);
			if(value.updated) {
				updated();
			}
		}
	}


	@Override
	public boolean checkVersion(boolean overwrite) {
		if(value != null) {
			return value.checkVersion(overwrite);
		} else {
			return true;
		}
	}

	public Supplier <T> getSupplier() {
		return supplier;
	}

	@Override
	protected void resetSave() {
		if(value != null) {
			value.resetSave();
		}
	}

	@Override
	protected void resetLoad() {
		if(value != null) {
			value.resetLoad();
		}
	}

}
