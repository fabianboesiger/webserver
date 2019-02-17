package server.renderer.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

import server.renderer.InterpreterException;
import server.renderer.Renderer;
import server.renderer.TranslatorException;
import server.renderer.container.Container;
import server.renderer.container.ObjectContainer;
import server.renderer.container.StringContainer;

public class TranslateCommand extends Command {
	
	private static final File LANGUAGES_FOLDER = new File("languages");
	private static final File LANGUAGES_INDEX_FILE = new File("languages/index.txt");
	
	@Override
	public Container run(StringBuilder code, LinkedList <String> languages, ObjectContainer variables, StringBuilder printer) throws IOException, InterpreterException {

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
		    			throw new TranslatorException("Language file not found");
		    		}
		    		
		    		BufferedReader bufferedReader = new BufferedReader(new FileReader(LANGUAGES_INDEX_FILE));  
		 			String line = null;
		 			ArrayList <String> keys = new ArrayList <String> ();
		 			while((line = bufferedReader.readLine()) != null) {
		 				keys.add(line);
		 			}
		 			bufferedReader.close();
		 			
		 			String key = ((StringContainer) Renderer.runNext(code, languages, variables, printer)).get();
		 			
		 			int keyIndex = -1;
		 			for(int i = 0; i < keys.size(); i++) {
		 				if(keys.get(i).equals(key)) {
		 					keyIndex = i;
		 				}
		 			}
		 			
		    		if(keyIndex == -1) {
		    			throw new TranslatorException("Language key not found");
		    		}

		 					 			
		 			if(keyIndex >= 0) {
			 			bufferedReader = new BufferedReader(new FileReader(languageFiles.get(index)));  
			 			for(int i = 0; i < keyIndex; i++) {
			 				bufferedReader.readLine();
			 			}
			 			line = bufferedReader.readLine();
			 			bufferedReader.close();
			 			
			 			printer.append(Renderer.render(new BufferedReader(new StringReader(line)), languages, variables));
			 			return null;
		 			}
		        }
	        }
		}
		
		throw new TranslatorException("No Language Files found");
	}
	
}
