package database.templates;

import database.Database;

public abstract class ComplexTemplate extends Template {
	
	protected boolean checkedIfUpdated = false;
	protected boolean checkedVersion = false;
	
	public ComplexTemplate(String name) {
		super(name);
	}
	
	public abstract void checkIfUpdated();
	public abstract boolean checkVersion(Database database, boolean overwrite);
	
}
