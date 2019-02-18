package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class VariablesCommand extends Command {

	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer) throws IOException, InterpreterException {
		return variables;
	}
	
}
