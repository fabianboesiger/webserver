package server.renderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import server.Response;

public abstract class Renderer {
	
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
	}
	
	public static String render(File file) throws IOException, InterpreterException {
		InputStream inputStream = new FileInputStream(file);
		
		StringBuilder buffer = new StringBuilder();
		StringBuilder code = new StringBuilder();
    	StringBuilder output = new StringBuilder();
		boolean insideTag = false;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Response.ENCODING));

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
        			output.append(interpret(code));
        		}
        	}
        }
        bufferedReader.close();
        
        return output.toString();
	}
	
	private static StringBuilder interpret(StringBuilder code) throws InterpreterException, IOException {
		StringBuilder output = new StringBuilder();
		while(code.length() > 0) {
			String command = next(code);
			if(command != null) {
				String result = run(command.toLowerCase(), code);
				if(result != null) {
					output.append(result);
				}
			}
		}
		return output;
	}
	
	protected static String next(StringBuilder code) {
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
	
	private static String run(String command, StringBuilder code) throws InterpreterException, IOException {
		if(commands.containsKey(command)) {
			return commands.get(command).run(code);
		}
		throw new UnknownCommandException(command);
	}
	
}