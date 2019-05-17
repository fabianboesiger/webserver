package database.templates;

import database.Database;

public abstract class ComplexTemplate extends Template {
	
	protected Database database;
	
	public ComplexTemplate(String name) {
		super(name);
	}
	
	public abstract void checkIfUpdated(Database database);
	public abstract boolean checkVersion(boolean overwrite);
	public abstract void reload();
	protected abstract void resetSave();
	protected abstract void resetLoad();
	
	public void setDatabase(Database database) {
		this.database = database;
	}
	
}
