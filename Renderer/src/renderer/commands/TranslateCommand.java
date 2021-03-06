package renderer.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import renderer.InterpreterException;
import renderer.Renderer;
import renderer.TranslatorException;

public class TranslateCommand extends Command {
	
	private static final File LANGUAGES_FOLDER = new File("languages");
	private static final File LANGUAGES_INDEX_FILE = new File("languages/index.txt");
	
	private static final LinkedList <String> DEFAULT_LANGUAGES;

	static {
		DEFAULT_LANGUAGES = new LinkedList <String> ();
		DEFAULT_LANGUAGES.add("de");
		DEFAULT_LANGUAGES.add("en");
	}
	
	@Override
	public Object run(StringBuilder code, List <String> languages, Map <String, Object> variables, StringBuilder printer, File folder) throws IOException, InterpreterException {

		if(languages != null) {
	        File[] files = LANGUAGES_FOLDER.listFiles();
	        if(files.length > 0) {
	        	
		        ArrayList <File> languageFiles = new ArrayList <File> ();
		        for(File languageFile : files) {
		        	String name = languageFile.getName();
		            if(name.substring(name.lastIndexOf(".") + 1, name.length()).equals("txt") && !name.equals("index.txt")) {
		            	languageFiles.add(languageFile);
		            }
		        }

		        if(languageFiles.size() > 0) {
		        	
		        	languages.addAll(DEFAULT_LANGUAGES);
		        	
		        	int index = -1;
		    		int confidence = 0;
		    		for(String language : languages) {
			    		for(int j = 0; j < languageFiles.size(); j++) {
			    			int currentConfidence = 0;
			    			String[] languageSplitted = language.split("-");
			    			String name = languageFiles.get(j).getName();
			    			String[] keySplitted = name.substring(0, name.lastIndexOf(".")).split("-");
			    			for(int k = 0; k < languageSplitted.length; k++) {
			    				if(k < keySplitted.length) {
			    					if(keySplitted[k].equals(languageSplitted[k])) {
			    						currentConfidence++;
			    					} else {
			    						break;
			    					}
			    				}
			    			}
			    			if(currentConfidence > confidence) {
			    				confidence = currentConfidence;
			    				index = j;
			    			}
			    		}
		    		}
		    		
		    		if(index == -1) {
		    			throw new TranslatorException("Language file not found for " + languages.toString());
		    		}
		    		
		    		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(LANGUAGES_INDEX_FILE), Renderer.ENCODING));  
		 			String line = null;
		 			ArrayList <String> keys = new ArrayList <String> ();
		 			while((line = bufferedReader.readLine()) != null) {
		 				keys.add(line);
		 			}
		 			bufferedReader.close();
		 			
		 			String key = (String) Renderer.next(code, languages, variables, printer, folder);
		 			int keyIndex = -1;
		 			for(int i = 0; i < keys.size(); i++) {
		 				if(keys.get(i).equals(key)) {
		 					keyIndex = i;
		 				}
		 			}
		 			
		    		if(keyIndex == -1) {
		    			throw new TranslatorException("Language key not found for " + key);
		    		}

		 					 			
		 			if(keyIndex >= 0) {
			 			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(languageFiles.get(index)), Renderer.ENCODING));  
			 			for(int i = 0; i < keyIndex; i++) {
			 				bufferedReader.readLine();
			 			}
			 			line = bufferedReader.readLine();
			 			bufferedReader.close();
			 			
			 			return Renderer.render(new BufferedReader(new StringReader(line)), languages, variables, folder);
		 			}
		        } 
	        }
		}
		
		throw new TranslatorException("No Language Files found");
	}
	
}
