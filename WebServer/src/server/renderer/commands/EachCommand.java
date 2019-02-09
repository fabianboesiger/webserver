package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.ArrayContainer;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class EachCommand extends Command {
	
	private static final String END = "end";
	
	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer container, StringBuilder printer) throws IOException, InterpreterException {
		String key = Renderer.nextString(code);
		ArrayContainer arrayContainer = (ArrayContainer) Renderer.runNext(code, languages, container, printer);		
		
		if(arrayContainer.size() == 0) {
			while(!Renderer.nextString(code).toLowerCase().equals(END));
		} else {
			for(int i = 0; i < arrayContainer.size(); i++) {
				StringBuilder codeCopy;
				if(i == arrayContainer.size() - 1) {
					codeCopy = code;
				} else {
					codeCopy = new StringBuilder(code);
				}
				
				Container value = arrayContainer.get(i);
				container.put(key, value);
				String next;
				while(!(next = Renderer.nextString(codeCopy)).toLowerCase().equals(END)) {
					Renderer.run(next, codeCopy, languages, container, printer);
				}
			}
		}
		
		return null;
	}
	
}