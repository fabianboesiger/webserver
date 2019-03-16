package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class AndCommand extends Command {
		
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		return ((boolean) Renderer.next(code, languages, variables, printer, folder)) && ((boolean) Renderer.next(code, languages, variables, printer, folder));
		
	}
	
}
