package renderer.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;

public class IncludeCommand extends Command {

	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {
		printer.append(Renderer.render(new File(folder.getPath() + File.separator + Renderer.next(code, languages, variables, printer, folder)), languages, variables, folder));
		return null;
	}
	
}
