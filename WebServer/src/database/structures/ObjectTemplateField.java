package database.structures;

public interface ObjectTemplateField {
	
	public String toString();
	public void fromString(String string);
	public void set(Object object);
	public Object get();
	
}
