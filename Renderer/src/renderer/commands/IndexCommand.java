package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class IndexCommand extends Command {

	@SuppressWarnings("unchecked")
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		int key = (Integer) Renderer.next(code, languages, variables, printer, folder);
		return ((List <Object>) Renderer.next(code, languages, variables, printer, folder)).get(key);
	}
	
}
