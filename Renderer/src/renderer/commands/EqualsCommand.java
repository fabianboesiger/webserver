package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class EqualsCommand extends Command {
		
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		if(Renderer.runNext(code, languages, variables, printer, folder) == Renderer.runNext(code, languages, variables, printer, folder)) {
			return new Boolean(true);
		}
		return new Boolean(false);
	}
	
}