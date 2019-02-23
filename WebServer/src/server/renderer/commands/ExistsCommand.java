package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class ExistsCommand extends Command {
		
	@SuppressWarnings("unchecked")
	@Override
	public Object run(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws IOException, InterpreterException {
		String key = (String) Renderer.next(code, languages, variables, printer);
		if(((Map <String, Object>) Renderer.runNext(code, languages, variables, printer)).containsKey(key)) {
			return new Boolean(true);
		}
		return new Boolean(false);
	}
	
}