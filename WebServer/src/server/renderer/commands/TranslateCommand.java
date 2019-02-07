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

public class TranslateCommand extends Command {
	
	private static final File LANGUAGES_FOLDER = new File("languages");
	private static final File LANGUAGES_INDEX_FILE = new File("languages/index.txt");
	
	private Renderer renderer;
	
	public TranslateCommand(Renderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public String run(StringBuilder code, LinkedList <String> languages) throws IOException, InterpreterException {

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
		    		
		    		BufferedReader bufferedReader = new BufferedReader(new FileReader(LANGUAGES_INDEX_FILE));  
		 			String line = null;
		 			ArrayList <String> keys = new ArrayList <String> ();
		 			while((line = bufferedReader.readLine()) != null) {
		 				keys.add(line);
		 			}
		 			bufferedReader.close();
		 			
		 			String key = Renderer.next(code).toString();
		 			
		 			int keyIndex = -1;
		 			for(int i = 0; i < keys.size(); i++) {
		 				if(keys.get(i).equals(key)) {
		 					keyIndex = i;
		 				}
		 			}
		 					 			
		 			if(keyIndex >= 0) {
			 			bufferedReader = new BufferedReader(new FileReader(languageFiles.get(index)));  
			 			for(int i = 0; i < keyIndex; i++) {
			 				bufferedReader.readLine();
			 			}
			 			line = bufferedReader.readLine();
			 			bufferedReader.close();
		    		
			 			return renderer.render(new BufferedReader(new StringReader(line)), languages);
		 			}
		        }
	        }
		}
		
		throw new InterpreterException("No Language Files found");
	}
	
}
