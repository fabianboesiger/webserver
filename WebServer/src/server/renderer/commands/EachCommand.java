package server.renderer.commands;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import server.renderer.InterpreterException;
import server.renderer.Renderer;

public class EachCommand extends CommandBlock {
	
	private static final String END = "end";
	
	@SuppressWarnings("unchecked")
	@Override
	public Object run(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws IOException, InterpreterException {
		String key = Renderer.nextString(code, languages, variables, printer);
		List <Object> list = (List <Object>) Renderer.runNext(code, languages, variables, printer);		
		
		if(list.size() == 0) {
			skip(code);
		} else {
			for(int i = 0; i < list.size(); i++) {
				StringBuilder codeCopy;
				if(i == list.size() - 1) {
					codeCopy = code;
				} else {
					codeCopy = new StringBuilder(code);
				}
				
				Object value = list.get(i);
				variables.put(key, value);
				String next;
				while(!(next = Renderer.nextCommand(codeCopy)).toLowerCase().equals(END)) {
					Renderer.run(next, codeCopy, languages, variables, printer);
				}
			}
		}
		
		return null;
	}
	
}