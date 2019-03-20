package renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import renderer.commands.AndCommand;
import renderer.commands.Command;
import renderer.commands.ConsoleCommand;
import renderer.commands.EachCommand;
import renderer.commands.EqualsCommand;
import renderer.commands.ExistsCommand;
import renderer.commands.GetCommand;
import renderer.commands.HTMLEncodeCommand;
import renderer.commands.IfCommand;
import renderer.commands.IncludeCommand;
import renderer.commands.MarkdownCommand;
import renderer.commands.NotCommand;
import renderer.commands.NullCommand;
import renderer.commands.OrCommand;
import renderer.commands.PrintCommand;
import renderer.commands.SetCommand;
import renderer.commands.SizeCommand;
import renderer.commands.TranslateCommand;
import renderer.commands.VariablesCommand;

public abstract class Renderer {
	
	public static final Charset ENCODING = StandardCharsets.UTF_8;
	private static final String BEGIN = "{{";
	private static final String END = "}}";
	private static final char STRING = '"';
	private static final char ESCAPE = '\\';

	private static HashMap <String, Command> commands;
		
	static {
		commands = new HashMap <String, Command> ();
		commands.put("print", new PrintCommand());
		commands.put("get", new GetCommand());
		commands.put("include", new IncludeCommand());
		commands.put("translate", new TranslateCommand());
		commands.put("variables", new VariablesCommand());
		commands.put("each", new EachCommand());
		commands.put("if", new IfCommand());
		commands.put("exists", new ExistsCommand());
		commands.put("equals", new EqualsCommand());
		commands.put("null", new NullCommand());
		commands.put("not", new NotCommand());
		commands.put("htmlencode", new HTMLEncodeCommand());
		commands.put("or", new OrCommand());
		commands.put("and", new AndCommand());
		commands.put("markdown", new MarkdownCommand());
		commands.put("size", new SizeCommand());
		commands.put("console", new ConsoleCommand());
		commands.put("set", new SetCommand());
	}
	

	public static String render(File file, List <String> languages, Map <String, Object> variables, File folder) throws IOException, InterpreterException {
		return render(new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING)), languages, variables, folder);
	}
	
	public static String render(BufferedReader bufferedReader, List <String> languages, Map <String, Object> variables, File folder) throws IOException, InterpreterException {
		StringBuilder buffer = new StringBuilder();
		StringBuilder code = new StringBuilder();
    	StringBuilder output = new StringBuilder();
		boolean insideTag = false;

		int next;
        while((next = bufferedReader.read()) != -1) {
        	char character = (char) next;
    		buffer.append(character);
        	if(!insideTag) {
        		if(character != BEGIN.charAt(buffer.length()-1)) {
        			output.append(buffer);
        			buffer.setLength(0);
        		} else
        		if(buffer.length() >= BEGIN.length()) {
        			insideTag = true;
        			buffer.setLength(0);
        		}
        	} else {
        		if(character != END.charAt(buffer.length()-1)) {
        			code.append(buffer);
        			buffer.setLength(0);
        		} else
        		if(buffer.length() >= END.length()) {
        			insideTag = false;
        			buffer.setLength(0);
        			output.append(interpret(code, languages, variables, folder));
        		}
        	}
        }
        bufferedReader.close();
        
        return output.toString();
	}
	
	private static StringBuilder interpret(StringBuilder code, List <String> languages, Map <String, Object> variables, File folder) throws InterpreterException, IOException {
		StringBuilder printer = new StringBuilder();
		while(code.length() > 0) {
			runNext(code, languages, variables, printer, folder);
		}
		return printer;
	}
	
	public static String nextCommand(StringBuilder code) {
		StringBuilder buffer = new StringBuilder();
		boolean insideString = false;
		boolean escaped = false;

		while(code.length() > 0) {
			char character = code.charAt(0);
			code.deleteCharAt(0);
			if(!insideString) {
				if(character == STRING) {
					buffer.append(STRING);
					insideString = true;
				} else
				if(Character.isWhitespace(character)) {
					if(buffer.length() > 0) {
						return buffer.toString();
					}
				} else {
					buffer.append(character);
				}
			} else {
				if(escaped) {
					if(character == STRING) {
						buffer.append(STRING);
					} else
					if(character == ESCAPE) {
						buffer.append(ESCAPE);
					}
					escaped = false;
				} else {
					if(character == ESCAPE) {
						escaped = true;
					} else
					if(character == STRING) {
						buffer.append(STRING);
						if(buffer.length() > 0) {
							return buffer.toString();
						}
					} else {
						buffer.append(character);
					}
				}
			}
		}
		if(buffer.length() > 0) {
			return buffer.toString();
		}
		
		return null;
	}
	
	public static Object runNext(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws InterpreterException, IOException {
		return run(nextCommand(code), code, languages, variables, printer, folder);
	}
	
	public static Object run(String commandString, StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws InterpreterException, IOException {		
		if(commandString != null) {
			Command command = getCommand(commandString);
			if(command != null) {
				return command.run(code, languages, variables, printer, folder);
			} else {
				throw new UnknownCommandException(commandString);
			}
		}

		return null;
	}
	
	public static Command getCommand(String command){
		if(command != null) {
			if(commands.containsKey(command)) {
				return commands.get(command);
			}
		}
		return null;
	}

	public static Object next(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws InterpreterException, IOException {
		String command = nextCommand(code);
		Object output = parse(command);

		if(output == null) {
			return run(command, code, languages, variables, printer, folder);
		} else {
			return output;
		}
	}
	
	public static Object parse(String input) {
		if(input.matches("[0-9]+")) {
			return Integer.parseInt(input);
		} else
		if(input.matches("[0-9]*\\.[0-9]+")) {
			return Double.parseDouble(input);
		} else
		if(input.matches("null")) {
			return null;
		} else
		if(input.matches("true|false")) {
			return Boolean.parseBoolean(input);
		} else
		if(input.matches("\\[.*(,.*)*\\]")) {
			ArrayList <Object> list = new ArrayList <Object> ();
			String trimmed = input.trim();
			String string = trimmed.substring(1, trimmed.length() - 1);
			String[] splitted = string.split(",");
			for(String element : splitted) {
				list.add(parse(element));
			}
			return list;
		} else
		if(input.matches("\".*\"")) {
			String trimmed = input.trim();
			String string = trimmed.substring(1, trimmed.length() - 1);
			return string;
		}
		return null;
	}
	
}