package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.container.Container;

public class GetCommand extends Command {

	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, Container container, StringBuilder printer) throws IOException, InterpreterException {
		String key = Renderer.nextString(code);
		return Renderer.run(Renderer.nextString(code), code, languages, container, printer).get(key);
	}
	
}
