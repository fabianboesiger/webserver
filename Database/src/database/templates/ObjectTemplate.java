package database.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import database.Database;
import database.DatabaseException;

public abstract class ObjectTemplate extends Template {
		
	private transient Identifiable identifier;
	
	public ObjectTemplate() {
		super(null);
	}
	
	public ObjectTemplate(String name) {
		super(name);
		identifier = null;
	}
		
	public void setIdentifier(Identifiable identifier) {
		this.identifier = identifier;
	}
	
	public Identifiable getIdentifier() {
		return identifier;
	}
	/*
	@SuppressWarnings("unchecked")
	@Override
	public void set(Object object) {
		Map <String, Object> input = (Map <String, Object>) object;
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			try {
				field.setAccessible(true);
				if(field.get(this) instanceof PrimitiveTemplate) {
					String name = ((PrimitiveTemplate) field.get(this)).name;
					if(input.containsKey(name)) {
						((Template) field.get(this)).set(input.get(name));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Object get() {
		HashMap <String, Object> output = new HashMap <String, Object> ();
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			try {
				if(field.get(this) instanceof PrimitiveTemplate) {
					if(!Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
						output.put(((PrimitiveTemplate) field.get(this)).name, ((Template) field.get(this)).get());
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	*/
	@Override
	public boolean validate(Errors errors) {
		boolean valid = true;
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			try {
				if(field.get(this) instanceof Template) {
					if(!((Template) field.get(this)).validate(errors)) {
						valid = false;
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return false;
			}
		}
		return valid;
	}
	
	public LinkedList <String> renderToList(Database database) {
		LinkedList <String> lines = new LinkedList <String> ();
		Field[] fields = getClass().getDeclaredFields();
		for(int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				Object object = field.get(this);
				if(!Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
					if(object instanceof Template) {
						lines.add(((Template) object).name + "=" + ((Template) object).render(database));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lines;
	}
	
	public void parseFromMap(Map <String, String> input) {
		parseFromMap(null, input, null);
	}
	
	public void parseFromMap(Database database, Map <String, String> input, Map <String, ObjectTemplate> initialized) {
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			try {
				field.setAccessible(true);
				Object object = field.get(this);
				if(object instanceof Template) {
					String name = ((Template) object).name;
					if(input.containsKey(name)) {
						setField(database, field, input.get(name), initialized);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String render(Database database) throws Exception {
		String id = getIdentifier().getId();
		if(id == null) {
			id = Integer.toHexString(database.getCount(this.getClass().getSimpleName()));
		} else {
			id = Database.encrypt(id);
		}
		File file = database.getFile(getClass().getSimpleName(), id);
		
		if(file.exists()) {
			throw new DatabaseException("File already exists");
		}
				
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Database.ENCODING));
		LinkedList <String> lines = renderToList(database);
		for(int i = 0; i < lines.size(); i++) {
			bufferedWriter.write(lines.get(i));
			if(i != lines.size() - 1) {
				bufferedWriter.newLine();
			}
		}
		bufferedWriter.close();
		return id;
	}

	@Override
	public void parse(Database database, String string, Map <String, ObjectTemplate> initialized) throws Exception {
		File file = database.getFile(getClass().getSimpleName(), string);
		if(file != null) {
			if(file.exists()) {	
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Database.ENCODING));
				HashMap <String, String> map = new HashMap <String, String> ();
				
				String line = null;
				while((line = bufferedReader.readLine()) != null) {
					int indexOfEquals = line.indexOf("=");
					if(indexOfEquals != -1) {
						String key = line.substring(0, indexOfEquals).trim();
						String value = line.substring(indexOfEquals + 1, line.length()).trim();
						map.put(key, value);
					}
				}
				bufferedReader.close();
				
				parseFromMap(database, map, initialized);
			}
		}
	}
	
}
