package server.renderer.container;

public class StringContainer implements Container {
	
	private String value;
	
	public StringContainer(String value) {
		this.value = value;
	}
	
	public void set(String value) {
		this.value = value;
	}
	
	public String get() {
		return value;
	}

	@Override
	public Container get(String key) {
		return null;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}
