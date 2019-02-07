package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import database.structures.ObjectTemplate;
import database.structures.ObjectTemplateException;

public class Database {
	
	private transient static final File DATA_FOLDER = new File("data");
	
	public Database() {
		
	}
	
	public ObjectTemplate load(String name, String id) throws ObjectTemplateException, IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(getFile(name, id));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ObjectTemplate output = (ObjectTemplate) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return output;
	}
	
	public boolean save(ObjectTemplate objectTemplate) {
		try {
			String id = objectTemplate.getId();
			if(id != null) {
				File file = getFile(objectTemplate.getName(), id);
				if(file.exists()) {
					return false;
				}
				file.getParentFile().mkdirs();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(objectTemplate);
				objectOutputStream.close();
				fileOutputStream.close();
				return true;
			}
			return false;
		} catch (IOException | ObjectTemplateException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public File getFile(String name, String id) throws ObjectTemplateException {
		return new File(DATA_FOLDER.getName() + "/" + name.toLowerCase() + "s/" + encrypt(id) + ".ser");
	}
	
	private static String encrypt(String input) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < input.length(); i++) {
			int number = input.charAt(i);
			StringBuilder hex = new StringBuilder(Integer.toHexString(number));
			while(hex.length() < 2) {
				hex.insert(0, "0");
			}
			output.append(hex);
		}
		return output.toString();
	}
	
	/*
	private static String decrypt(String input) {
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < input.length(); i += 2) {
			output.append((char) Integer.parseInt("" + input.charAt(i) + input.charAt(i + 1), 16));
		}
		return output.toString();
	}
	*/
	
}
