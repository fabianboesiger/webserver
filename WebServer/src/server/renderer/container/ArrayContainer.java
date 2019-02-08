package server.renderer.container;

import java.util.ArrayList;

import server.renderer.ParserException;
import server.renderer.Renderer;

public class ArrayContainer implements Container {
	
	private ArrayList <Container> values;
	
	public ArrayContainer() {
		values = new ArrayList <Container> ();
	}
	
	public void add(int index, Container value) {
		values.add(index, value);
	}
	
	public void add(Container value) {
		values.add(value);
	}
	
	public Container get(int index) {
		return values.get(index);
	}

	@Override
	public Container get(String key) throws ParserException {
		return get(Renderer.parseInt(key));
	}
	
	@Override
	public String toString() {
		return values.toString();
	}
	
}
