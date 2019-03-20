package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class SetCommand extends Command {

	@SuppressWarnings("unchecked")
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		String key = (String) Renderer.next(code, languages, variables, printer, folder);
		Object value = Renderer.next(code, languages, variables, printer, folder);
		return ((Map <String, Object>) Renderer.next(code, languages, variables, printer, folder)).put(key, value);
	}
	
}
