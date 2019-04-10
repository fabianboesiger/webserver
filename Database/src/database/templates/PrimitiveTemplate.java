package database.templates;

public abstract class PrimitiveTemplate <T> extends Template {
	
	
	public PrimitiveTemplate(String name, UpdateAction updateAction) {
		super(name, updateAction);
	}
	
	public PrimitiveTemplate(String name) {
		super(name);
	}
	
	public abstract void set(T object);
	public abstract T get();
	
}
