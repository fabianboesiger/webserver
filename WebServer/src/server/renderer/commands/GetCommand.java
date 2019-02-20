package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class GetCommand extends Command {

	@SuppressWarnings("unchecked")
	@Override
	public Object run(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws IOException, InterpreterException {
		String key = Renderer.nextString(code, languages, variables, printer);
		return ((Map <String, Object>) Renderer.runNext(code, languages, variables, printer)).get(key);
	}
	
}
