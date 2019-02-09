package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.BooleanContainer;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class IfCommand extends CommandBlock {
		
	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer) throws IOException, InterpreterException {
		BooleanContainer doIt = (BooleanContainer) Renderer.runNext(code, languages, variables, printer);		
		if(doIt.get()) {
			String next;
			while(!(next = Renderer.nextString(code)).toLowerCase().equals(END)) {
				Renderer.run(next, code, languages, variables, printer);
			}
		} else {
			skip(code);
		}
		return null;
	}
	
}