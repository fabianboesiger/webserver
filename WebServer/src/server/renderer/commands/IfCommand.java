package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.BooleanContainer;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class IfCommand extends Command {
	
	private static final String END = "end";
	
	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer container, StringBuilder printer) throws IOException, InterpreterException {
		BooleanContainer doIt = (BooleanContainer) Renderer.runNext(code, languages, container, printer);		
		System.out.println(doIt);
		if(doIt.get()) {
			String next;
			while(!(next = Renderer.nextString(code)).toLowerCase().equals(END)) {
				Renderer.run(next, code, languages, container, printer);
			}
		} else {
			while(!Renderer.nextString(code).toLowerCase().equals(END));
		}
		return null;
	}
	
}