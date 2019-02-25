package database.templates;

public abstract class PrimitiveTemplate extends Template {
	
	protected transient ParseAction parseAction;
	
	public PrimitiveTemplate(String name, ParseAction parseAction) {
		super(name);
		this.parseAction = parseAction;
	}
	
	public PrimitiveTemplate(String name) {
		this(name, null);
	}
	
	public abstract String render();
	public abstract void parse(String string);
	
}
