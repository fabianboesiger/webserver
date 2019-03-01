package database.templates;

public abstract class Template {	
	
	protected transient String name;
	
	public Template(String name) {
		this.name = name;
	}
	
	public abstract boolean validate(Errors errors);
	
}
