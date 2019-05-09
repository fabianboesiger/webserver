package obfuscator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public abstract class Obfuscator {
	
	private static Random random = new Random();
	
	private static char randomChar() {
		int r = random.nextInt(62);
		if(r < 10) {
			r += 48;
		} else
		if(r < 36) {
			r += 65-10;
		} else {
			r += 97-36;
		}
		return (char) r;
	}
	
	private static String randomVariableName(HashMap <String, String> names) {
		StringBuilder name = new StringBuilder();
		do {
			for(int i = 0; i < 4; i++) {
				char c = randomChar();
				if(i == 0) {
					while(Character.isDigit(c)) {
						c = randomChar();
					}
				}
				name.append(c);
			}
		} while(names.containsValue(name.toString()));
		return name.toString();
	}
	
	private static void generateGarbage(StringBuilder out, HashMap <String, String> names, ArrayList <String> variables, ArrayList <String> functions) {
		while(random.nextInt(10) < 4) {
			switch(random.nextInt(3)) {
			case 0:
				String variable = randomVariableName(names);
				variables.add(variable);
				switch(random.nextInt(6)) {
				case 0:
					out.append("var " + variable + "=" + random.nextInt() + ";\n");
					break;
				case 1:
					out.append("var " + variable + "=" + random.nextFloat() + ";\n");
					break;
				case 2:
					out.append("var " + variable + "=" + random.nextBoolean() + ";\n");
					break;
				case 3:
					if(variables.size() > 0) {
						out.append("var " + variable + "=" + variables.get(random.nextInt(variables.size())) + ";\n");
					}
				case 4:
					if(variables.size() > 0) {
						out.append("var " + variable + "=" + variables.get(random.nextInt(variables.size())) + "+" + variables.get(random.nextInt(variables.size())) + ";\n");
					}
				case 5:
					if(variables.size() > 0) {
						out.append(variables.get(random.nextInt(variables.size())) + "=" + variables.get(random.nextInt(variables.size())) + "+" + variables.get(random.nextInt(variables.size())) + ";\n");
					}
				}
			case 1:
				String function = randomVariableName(names);
				functions.add(function);
				out.append("function " + function + "(");
				boolean inputs = false;
				while(random.nextInt(10) < 5) {
					String arg = randomVariableName(names);
					variables.add(arg);
					out.append(arg + ",");
					inputs = true;
				}
				if(inputs) {
					out.setLength(out.length() - 1);
				}
				out.append("){\n");
				generateGarbage(out, names, new ArrayList <String> (variables), new ArrayList <String> (functions));
				out.append("}");
				break;
			case 2:
				if(variables.size() > 0) {
					String[] operators = {"==", "===", ">", "<", ">=", "<=", "!=", "!=="};
					out.append("if( " + variables.get(random.nextInt(variables.size())) + operators[random.nextInt(operators.length)] + variables.get(random.nextInt(variables.size())) + "){\n");
					generateGarbage(out, names, new ArrayList <String> (variables), new ArrayList <String> (functions));
					out.append("}");
				}
				break;
			}
		}
	}

	public static void obfuscate() throws IOException {
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("input.js"), "UTF-8"));			
		
		StringBuilder in = new StringBuilder();
		StringBuilder out = new StringBuilder();
		
		int next;
        while((next = bufferedReader.read()) != -1) {
        	char character = (char) next;
        	in.append(character);
        }
        
        bufferedReader.close();
		                
        LinkedList <String> keywords = new LinkedList <String> ();
        
		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("keywords.txt"), "UTF-8"));			
		
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			keywords.add(line);
		}
		
		bufferedReader.close();
		
		LinkedList <String> definers = new LinkedList <String> ();
        
		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("definers.txt"), "UTF-8"));			
		
		line = null;
		while((line = bufferedReader.readLine()) != null) {
			definers.add(line);
		}
		
		bufferedReader.close();
		
        HashMap <String, String> names = new HashMap <String, String> ();
        
        StringBuilder word = new StringBuilder();
        ArrayList <String> wordBuffer = new ArrayList <String> ();
        boolean direct = false;
        boolean escaped = false;
        boolean string = false;
        char stringChar = 0;
        for(int i = 0; i < in.length(); i++) {
        	char c = in.charAt(i);
        	if(!string) {
	        	if(Character.isLetterOrDigit(c) || c == '_') {
	        		word.append(c);
	        	} else {
	        		if(word.length() != 0) {
	        			if(!keywords.contains(word.toString())) {
	        				if(!Character.isDigit(word.charAt(0))) {
	        					
	        					int j = i;
	        					while(Character.isWhitespace(in.charAt(j))) {
	        						j++;
	        					}
	        					
	        					if((definers.contains(wordBuffer.get(wordBuffer.size() - 1)) && direct) 
	        						|| (in.length() > j+1 && in.charAt(j) == '=' && in.charAt(j + 1) != '=')
	        					) {
	        						String key = word.toString();
	        						if(!names.containsKey(key)) {
	        							String name = randomVariableName(names);
		        						names.put(word.toString(), name);
		        						System.out.println(word.toString() + " -> " + name);
	        						}
	        						
	        					}
	        				}
	        			}
	        			
	        			wordBuffer.add(word.toString());
		        		direct = true;
		        		word.setLength(0);
	        		}
	        		if(c == '"' || c == '\'') {
	        			string = true;
	        			stringChar = c;
	        		}
	        		if(!Character.isWhitespace(c)) {
	        			direct = false;
	        		}
	        	}
        	} else {
        		if((c == '"' || c == '\'') && c == stringChar && !escaped) {
        			string = false;
        		}
        		escaped = false;
        		if(c == '\\') {
        			escaped = true;
        		}
        	}
        }
                
        generateGarbage(out, names, new ArrayList <String> (), new ArrayList <String> ());
        
        word = new StringBuilder();
        escaped = false;
        string = false;
        stringChar = 0;
        int semis = 0;

        for(int i = 0; i < in.length(); i++) {
        	char c = in.charAt(i);
        	if(!string) {
	        	if(Character.isLetterOrDigit(c) || c == '_') {
	        		word.append(c);
	        	} else {
	        		if(word.length() != 0) {
	        			
	        			if(!keywords.contains(word.toString())) {
	        				if(names.containsKey(word.toString())) {
	        					out.append(names.get(word.toString()));
	        				} else {
	        					out.append(word.toString());
	        				}
	        			} else {
    						out.append(word.toString());
    						if(word.toString().equals("for")) {
    							semis = 2;
    						}
    					}
		        		word.setLength(0);
	        		}
	        		
	        		out.append(c);
	        		
	        		if(c == '"' || c == '\'') {
	        			string = true;
	        			stringChar = c;
	        		} else
	        		if(c == ';') {
	        			if(semis == 0) {
	        				generateGarbage(out, names, new ArrayList <String> (), new ArrayList <String> ());
	        			} else {
	        				semis--;
	        			}
	        		}
	        			        		
	        	}
        	} else {
        		if((c == '"' || c == '\'') && c == stringChar && !escaped) {
        			string = false;
        		}
        		escaped = false;
        		if(c == '\\') {
        			escaped = true;
        		}
        		out.append(c);
        	}
        }
        
        StringBuilder commentless = new StringBuilder();
        
        boolean big = false;
        boolean small = false;
        for(int i = 0; i < out.length() - 1; i++) {
        	String symbol = out.substring(i, i+2);
        	if(big) {
        		if(symbol.equals("*/")) {
        			big = false;
        			i++;
        		}
        	} else {
        		if(small) {
        			if(out.charAt(i) == '\n') {
        				small = false;
        				if(out.charAt(i+1) == '\r'){
        					i++;
        				}
        			}
        		} else {
	        		if(symbol.equals("/*")) {
	        			big = true;
	        		} else {
	        			if(symbol.equals("//")) {
	        				small = true;
	        			} else {
	    	        		commentless.append(out.charAt(i));
	        			}
	        		}
        		}
        	}
        }
        commentless.append(out.charAt(out.length() - 1));
        
        StringBuilder minified = new StringBuilder();
        
        boolean needed = false;
        boolean whitespace = false;
        string = false;
        stringChar = 0;
        for(int i = 0; i < commentless.length(); i++) {
        	char c = commentless.charAt(i);
        	if(!string) {
        		if(Character.isWhitespace(c)) {
            		whitespace = true;
            		if(c == '\n' && minified.length() > 0 && minified.charAt(minified.length() - 1) != '\n') {
            			minified.append('\n');
            		}
            	} else {
            		if(Character.isLetterOrDigit(c) || c == '_') {
            			if(whitespace && needed) {
                			minified.append(' ');
                		}
            			needed = true;
            		} else {
            			needed = false;
            		}
            		whitespace = false;
            		minified.append(c);
            		if(c == ';') {
            			minified.append('\n');
            		}
            	}
        		if(c == '"' || c == '\'') {
        			string = true;
        			stringChar = c;
        		}
        	} else {
        		if((c == '"' || c == '\'') && c == stringChar && !escaped) {
        			string = false;
        		}
        		escaped = false;
        		if(c == '\\') {
        			escaped = true;
        		}
        		minified.append(c);
        	}
        	
        }
                
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.js"), "UTF-8"));
		for(int i = 0; i < minified.length(); i++) {
			bufferedWriter.write(minified.charAt(i));
		}
		bufferedWriter.close();
        
	}

}