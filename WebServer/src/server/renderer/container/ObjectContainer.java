package server.renderer.container;

import java.util.HashMap;

public class ObjectContainer {
	
	private HashMap <String, Container> values;
	
	public ObjectContainer() {
		
	}
	
	public void put(String key, Container value) {
		values.put(key, value);
	}
	
	public void putAll(ObjectContainer objectContainer) {
		values.putAll(objectContainer.values);
	}
	
}
