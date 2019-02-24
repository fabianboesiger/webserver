package server.renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import server.renderer.commands.VariablesCommand;
import server.Response;
import server.renderer.commands.Command;
import server.renderer.commands.EachCommand;
import server.renderer.commands.EqualsCommand;
import server.renderer.commands.ExistsCommand;
import server.renderer.commands.GetCommand;
import server.renderer.commands.IfCommand;
import server.renderer.commands.IncludeCommand;
import server.renderer.commands.NotCommand;
import server.renderer.commands.NullCommand;
import server.renderer.commands.PrintCommand;
import server.renderer.commands.TranslateCommand;

public abstract class Renderer {
	
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
	}
	

	public static String render(File file, LinkedList <String> languages, Map <String, Object> variables) throws IOException, InterpreterException {
		return render(new BufferedReader(new InputStreamReader(new FileInputStream(file), Response.ENCODING)), languages, variables);
	}
	
	public static String render(BufferedReader bufferedReader, LinkedList <String> languages, Map <String, Object> variables) throws IOException, InterpreterException {
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
        			output.append(interpret(code, languages, variables));
        		}
        	}
        }
        bufferedReader.close();
        
        return output.toString();
	}
	
	private static StringBuilder interpret(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables) throws InterpreterException, IOException {
		StringBuilder printer = new StringBuilder();
		while(code.length() > 0) {
			runNext(code, languages, variables, printer);
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
	
	public static Object runNext(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws InterpreterException, IOException {
		return run(nextCommand(code), code, languages, variables, printer);
	}
	
	public static Object run(String commandString, StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws InterpreterException, IOException {		
		if(commandString != null) {
			Command command = getCommand(commandString);
			if(command != null) {
				return command.run(code, languages, variables, printer);
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

	public static Object next(StringBuilder code, LinkedList <String> languages, Map <String, Object> variables, StringBuilder printer) throws InterpreterException, IOException {
		String command = nextCommand(code);
		Object output = parse(command);
		if(output == null) {
			return run(command, code, languages, variables, printer);
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