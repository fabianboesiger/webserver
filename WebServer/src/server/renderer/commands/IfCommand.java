package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class IfCommand extends CommandBlock {
		
	@Override
	public Object run(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws IOException, InterpreterException {
		if(Renderer.nextBoolean(code, languages, variables, printer)) {
			String next;
			while(!(next = Renderer.nextCommand(code)).toLowerCase().equals(END)) {
				Renderer.run(next, code, languages, variables, printer);
			}
		} else {
			skip(code);
		}
		return null;
	}
	
}