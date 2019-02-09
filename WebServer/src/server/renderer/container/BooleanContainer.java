package server.renderer.container;

public class BooleanContainer implements Container {
	
	private boolean value;
	
	public BooleanContainer(boolean value) {
		this.value = value;
	}
	
	public void set(boolean value) {
		this.value = value;
	}
	
	public boolean get() {
		return value;
	}

	@Override
	public Container get(String key) {
		return null;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(value);
	}
	
}
