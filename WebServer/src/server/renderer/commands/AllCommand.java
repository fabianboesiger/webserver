package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.container.Container;

public class AllCommand extends Command {

	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, Container container, StringBuilder printer) throws IOException, InterpreterException {
		return container;
	}
	
}
