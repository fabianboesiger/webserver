package database.templates;

import database.Database;

public abstract class ComplexTemplate extends Template {
	
	protected Database database;
	
	public ComplexTemplate(String name) {
		super(name);
	}
	
	public abstract void checkIfUpdated(Database database);
	public abstract boolean checkVersion(boolean overwrite);
	
}
