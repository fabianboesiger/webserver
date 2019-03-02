package database.templates;

public abstract class PrimitiveTemplate extends Template {
	
	
	public PrimitiveTemplate(String name, SaveAction saveAction) {
		super(name, saveAction);
	}
	
	public PrimitiveTemplate(String name) {
		super(name);
	}
	
	public abstract void set(Object object);
	public abstract Object get();
	
}
