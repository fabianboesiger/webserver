package database.templates;

import database.Database;

public abstract class ComplexTemplate extends Template {
	
	protected boolean checkedIfUpdated;
	protected boolean checkedVersion;
	
	public ComplexTemplate(String name) {
		super(name);
		checkedIfUpdated = false;
		checkedVersion = false;
	}
	
	public abstract void checkIfUpdated();
	public abstract boolean checkVersion(Database database, boolean overwrite);
	
}
