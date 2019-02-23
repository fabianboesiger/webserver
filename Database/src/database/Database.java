package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import database.templates.ObjectTemplate;
import server.Response;

public class Database {
	
	private static final File DATA_FOLDER = new File("data");
	private static final String ENDING = ".csv";
	
	public Database() throws DatabaseException {
		System.out.println("Starting database with directory " + DATA_FOLDER.getAbsolutePath());
		
		// Create folder if it does not exist
		if(!DATA_FOLDER.exists()) {
			if(!DATA_FOLDER.mkdir()) {
				throw new DatabaseException("Database folder could not be created");
			}
		}
	}
	
	public synchronized ObjectTemplate load(String name, String id) {
		try {
			File file = new File(DATA_FOLDER.getName() + "/" + name + ENDING);
			if(!file.exists()) {
				
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(), Response.ENCODING));
			String line = null;
			ArrayList <String> keys = new ArrayList <String> ();
			while((line = bufferedReader.readLine()) != null) {
				keys.add(line);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return null;
	}
	
	public synchronized boolean save(ObjectTemplate objectTemplate) {
		return false;
	}
	
}
