package server.renderer.container;

import java.util.HashMap;

public class ObjectContainer implements Container {
	
	private HashMap <String, Container> values;
	
	public ObjectContainer() {
		values = new HashMap <String, Container> ();
	}
	
	public void put(String key, Container value) {
		values.put(key, value);
	}
	
	public void putAll(ObjectContainer objectContainer) {
		values.putAll(objectContainer.values);
	}

	public Container get(String key) {
		return values.get(key);
	}
	
	@Override
	public String toString() {
		return values.toString();
	}

	public boolean containsKey(String key) {
		return values.containsKey(key);
	}
	
}
