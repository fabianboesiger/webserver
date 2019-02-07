package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;

import server.renderer.InterpreterException;

public abstract class Command {
	
	public abstract String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException;
	
}
