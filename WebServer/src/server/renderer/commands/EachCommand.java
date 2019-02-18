package server.renderer.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.ArrayContainer;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class EachCommand extends CommandBlock {
	
	private static final String END = "end";
	
	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer, BufferedReader insert) throws IOException, InterpreterException {
		String key = Renderer.nextString(code, languages, variables, printer, insert);
		ArrayContainer arrayContainer = (ArrayContainer) Renderer.runNext(code, languages, variables, printer, insert);		
		
		if(arrayContainer.size() == 0) {
			skip(code);
		} else {
			for(int i = 0; i < arrayContainer.size(); i++) {
				StringBuilder codeCopy;
				if(i == arrayContainer.size() - 1) {
					codeCopy = code;
				} else {
					codeCopy = new StringBuilder(code);
				}
				
				Container value = arrayContainer.get(i);
				variables.put(key, value);
				String next;
				while(!(next = Renderer.nextCommand(codeCopy)).toLowerCase().equals(END)) {
					Renderer.run(next, codeCopy, languages, variables, printer, insert);
				}
			}
		}
		
		return null;
	}
	
}