package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import server.renderer.InterpreterException;

public abstract class Command {
	
	public abstract Object run(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws IOException, InterpreterException;
	
}
