package database.templates;

public abstract class Template {	
	
	protected transient String name;
	
	public Template(String name) {
		this.name = name;
	}
	
	public abstract void set(Object object);
	public abstract Object get();
	public abstract boolean validate(Errors errors);
	
}
