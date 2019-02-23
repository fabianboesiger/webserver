package database.templates;

public abstract class PrimitiveTemplate extends Template {
	
	public PrimitiveTemplate(String name) {
		super(name);
	}
	
	public abstract String render();
	public abstract void parse(String string);
	
}
