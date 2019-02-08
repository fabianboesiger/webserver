package server.renderer.container;

import server.renderer.ParserException;

public interface Container {
	
	public String toString();
	public Container get(String key) throws ParserException;
	
}
