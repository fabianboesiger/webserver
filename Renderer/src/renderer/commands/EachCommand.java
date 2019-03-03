package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class EachCommand extends CommandBlock {
	
	private static final String END = "end";
	
	@SuppressWarnings("unchecked")
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		String key = (String) Renderer.next(code, languages, variables, printer, folder);
		List <Object> list = (List <Object>) Renderer.runNext(code, languages, variables, printer, folder);		
		
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
					Renderer.run(next, codeCopy, languages, variables, printer, folder);
				}
			}
		}
		
		return null;
	}
	
}