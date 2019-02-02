package server.renderer.structure;

import java.util.HashMap;
import java.util.LinkedList;

public class Tag extends Element {
	
	private String name;
	private HashMap <String, String> attributes;
	private LinkedList <Tag> content;
	
	public Tag() {
		
	}

	@Override
	public String parse() {
		return null;
	}
	
	@Override
	public String render() {
		return null;
	}
	
}
