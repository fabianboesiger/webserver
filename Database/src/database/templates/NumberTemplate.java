package database.templates;

public abstract class NumberTemplate <T> extends PrimitiveTemplate <T> {
	
	protected T minimum;
	protected T maximum;
	protected T value;
	protected boolean notNull;
	
	
	public NumberTemplate(String name, UpdateAction updateAction) {
		super(name, updateAction);
	}
	
	public NumberTemplate(String name) {
		super(name);
	}
	
	public T getMinimum() {
		return minimum;
	}
	
	public T getMaximum() {
		return maximum;
	}
	
}
