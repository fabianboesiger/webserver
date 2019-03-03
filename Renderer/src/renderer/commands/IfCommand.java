package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class IfCommand extends CommandBlock {
		
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		if((boolean) Renderer.next(code, languages, variables, printer, folder)) {
			String next;
			while(!(next = Renderer.nextCommand(code)).toLowerCase().equals(END)) {
				Renderer.run(next, code, languages, variables, printer, folder);
			}
		} else {
			skip(code);
		}
		return null;
	}
	
}