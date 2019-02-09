package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public abstract class Command {
	
	public abstract Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer) throws IOException, InterpreterException;
	
}
