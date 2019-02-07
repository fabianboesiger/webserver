package server.renderer;

import java.io.IOException;
import java.util.LinkedList;

public abstract class Command {
	
	protected abstract String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException;
	
}
