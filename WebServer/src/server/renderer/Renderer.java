package server.renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import server.renderer.commands.AllCommand;
import server.renderer.commands.BooleanCommand;
import server.renderer.commands.Command;
import server.renderer.commands.EachCommand;
import server.renderer.commands.ExistsCommand;
import server.renderer.commands.GetCommand;
import server.renderer.commands.IfCommand;
import server.renderer.commands.IncludeCommand;
import server.renderer.commands.PrintCommand;
import server.renderer.commands.SetCommand;
import server.renderer.commands.StringCommand;
import server.renderer.commands.TranslateCommand;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;

public class Renderer {
	
	private static final String KEYWORD = "server";
	private static final String BEGIN = "<" + KEYWORD + ">";
	private static final String END = "</" + KEYWORD + ">";
	private static final char STRING = '"';
	private static final char ESCAPE = '\\';

	private static HashMap <String, Command> commands;
		
	static {
		commands = new HashMap <String, Command> ();
		commands.put("print", new PrintCommand());
		commands.put("set", new SetCommand());
		commands.put("get", new GetCommand());
		commands.put("include", new IncludeCommand());
		commands.put("translate", new TranslateCommand());
		commands.put("all", new AllCommand());
		commands.put("string", new StringCommand());
		commands.put("each", new EachCommand());
		commands.put("if", new IfCommand());
		commands.put("exists", new ExistsCommand());
		commands.put("boolean", new BooleanCommand());
	}
	
	public static String render(File file, LinkedList <String> languages, ObjectContainer variables) throws IOException, InterpreterException {
		return render(new BufferedReader(new InputStreamReader(new FileInputStream(file))), languages, variables);
	}
	
	public static String render(BufferedReader bufferedReader, LinkedList <String> languages, ObjectContainer variables) throws IOException, InterpreterException {
		System.out.println(variables);
		
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
	
	private static StringBuilder interpret(StringBuilder code, LinkedList <String> languages, ObjectContainer variables) throws InterpreterException, IOException {
		StringBuilder printer = new StringBuilder();
		while(code.length() > 0) {
			runNext(code, languages, variables, printer);
		}
		return printer;
	}
	
	public static String nextString(StringBuilder code) {
		StringBuilder buffer = new StringBuilder();
		boolean insideString = false;
		boolean escaped = false;
						
		while(code.length() > 0) {
			char character = code.charAt(0);
			code.deleteCharAt(0);
			if(!insideString) {
				if(character == STRING) {
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
				} else {
					if(character == ESCAPE) {
						escaped = true;
					} else
					if(character == STRING) {
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
	
	public static Container runNext(StringBuilder code, LinkedList <String> languages, ObjectContainer container, StringBuilder printer) throws InterpreterException, IOException {
		return run(nextString(code), code, languages, container, printer);
	}
	
	public static Container run(String command, StringBuilder code, LinkedList <String> languages, ObjectContainer container, StringBuilder printer) throws InterpreterException, IOException {
		if(command != null) {
			command = command.toLowerCase();
			if(commands.containsKey(command)) {
				return commands.get(command).run(code, languages, container, printer);
			}
			throw new UnknownCommandException(command);
		}
		return null;
	}
	
	public static int parseInt(String string) throws ParserException {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new ParserException(string);
		}
	}
	
	public static int nextInt(StringBuilder code) throws ParserException {
		return parseInt(nextString(code));
	}
	
	public static double parseDouble(String string) throws ParserException {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			throw new ParserException(string);
		}
	}
	
	public static double nextDouble(StringBuilder code) throws ParserException {
		return parseDouble(nextString(code));
	}
	
	public static boolean parseBoolean(String string) throws ParserException {
		try {
			return Boolean.parseBoolean(string);
		} catch (NumberFormatException e) {
			throw new ParserException(string);
		}
	}
	
	public static boolean nextBoolean(StringBuilder code) throws ParserException {
		return parseBoolean(nextString(code));
	}
	
}