package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class AllCommand extends Command {

	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer container, StringBuilder printer) throws IOException, InterpreterException {
		return container;
	}
	
}
