package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.BooleanContainer;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class ExistsCommand extends Command {
		
	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer container, StringBuilder printer) throws IOException, InterpreterException {
		String key = Renderer.nextString(code);
		if(((ObjectContainer) Renderer.runNext(code, languages, container, printer)).containsKey(key)) {
			return new BooleanContainer(true);
		}
		return new BooleanContainer(false);
	}
	
}