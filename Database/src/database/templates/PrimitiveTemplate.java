package database.templates;

public abstract class PrimitiveTemplate extends Template {
	
	
	public PrimitiveTemplate(String name, UpdateAction updateAction) {
		super(name, updateAction);
	}
	
	public PrimitiveTemplate(String name) {
		super(name);
	}
	
	public abstract void set(Object object);
	public abstract Object get();
	
}
