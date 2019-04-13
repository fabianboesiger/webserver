package database.templates;

import database.Database;

public abstract class ComplexTemplate extends Template {
	
	public ComplexTemplate(String name) {
		super(name);
	}
	
	public abstract void checkIfUpdated();
	public abstract boolean checkVersion(Database database, boolean overwrite);
	
}
