package server.renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import server.renderer.commands.VariablesCommand;
import server.renderer.commands.Command;
import server.renderer.commands.EachCommand;
import server.renderer.commands.ExistsCommand;
import server.renderer.commands.GetCommand;
import server.renderer.commands.IfCommand;
import server.renderer.commands.IncludeCommand;
import server.renderer.commands.InsertCommand;
import server.renderer.commands.LayoutCommand;
import server.renderer.commands.PrintCommand;
import server.renderer.commands.TranslateCommand;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;
import server.renderer.container.StringContainer;

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
		commands.put("layout", new LayoutCommand());
		commands.put("insert", new InsertCommand());
	}
	
	public static String render(File file, LinkedList <String> languages, ObjectContainer variables) throws IOException, InterpreterException {
		return render(new BufferedReader(new InputStreamReader(new FileInputStream(file))), languages, variables);
	}
	
	public static String render(File file, LinkedList <String> languages, ObjectContainer variables, BufferedReader insert) throws IOException, InterpreterException {
		return render(new BufferedReader(new InputStreamReader(new FileInputStream(file))), languages, variables, insert);
	}
	
	public static String render(BufferedReader bufferedReader, LinkedList <String> languages, ObjectContainer variables) throws IOException, InterpreterException {
		return render(bufferedReader, languages, variables, bufferedReader);
	}
	
	public static String render(BufferedReader bufferedReader, LinkedList <String> languages, ObjectContainer variables, BufferedReader insert) throws IOException, InterpreterException {
		
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
        			output.append(interpret(code, languages, variables, insert));
        		}
        	}
        }
        bufferedReader.close();
        
        return output.toString();
	}
	
	private static StringBuilder interpret(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, BufferedReader insert) throws InterpreterException, IOException {
		StringBuilder printer = new StringBuilder();
		while(code.length() > 0) {
			runNext(code, languages, variables, printer, insert);
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
	
	public static Container runNext(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer, BufferedReader insert) throws InterpreterException, IOException {
		return run(nextCommand(code), code, languages, variables, printer, insert);
	}
	
	public static Container run(String commandString, StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer, BufferedReader insert) throws InterpreterException, IOException {
		if(commandString != null) {
			Command command = getCommand(commandString);
			if(command != null) {
				return command.run(code, languages, variables, printer, insert);
			} else {
				throw new UnknownCommandException(commandString);
			}
		}
		return null;
	}
	
	public static Command getCommand(String command){
		if(command != null) {
			command = command.toLowerCase();
			if(commands.containsKey(command)) {
				return commands.get(command);
			}
		}
		return null;
	}
	
	public static int nextInt(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer, BufferedReader insert) throws ParserException {
		String command = nextCommand(code);
		try {
			return Integer.parseInt(command);
		} catch (NumberFormatException e1) {
			try {
				return Integer.parseInt(((StringContainer) run(command, code, languages, variables, printer, insert)).get());
			} catch (NumberFormatException | InterpreterException | IOException e2) {
				throw new ParserException("Parse Integer Failed");
			}
		}
	}
	
	public static boolean nextBoolean(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer, BufferedReader insert) throws ParserException {
		String command = nextCommand(code);
		try {
			return Boolean.parseBoolean(command);
		} catch (NumberFormatException e1) {
			try {
				return Boolean.parseBoolean(((StringContainer) run(command, code, languages, variables, printer, insert)).get());
			} catch (NumberFormatException | InterpreterException | IOException e2) {
				throw new ParserException("Parse Boolean Failed");
			}
		}
	}
	
	public static String nextString(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer, BufferedReader insert) throws ParserException {
		String command = nextCommand(code);
		if(command.startsWith("" + STRING) && command.endsWith("" + STRING)) {
			return command.substring(1, command.length() - 1);
		} else {
				try {
					return ((StringContainer) runNext(code, languages, variables, printer, insert)).get();
				} catch (InterpreterException | IOException e) {
					throw new ParserException("Parse String Failed");
				}
		}
	}
	
}