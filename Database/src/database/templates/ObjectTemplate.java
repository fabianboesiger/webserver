package database.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import database.Database;
import database.validator.Validator;

public abstract class ObjectTemplate extends ComplexTemplate {
		
	private Identifiable identifier;
	protected LongTemplate timestamp;
	protected String id;
	
	private boolean validated;
	private boolean rendered;
	private boolean parsed;
	private boolean checkedIfUpdated;
	private boolean checkedVersion;
	
	private boolean resettedSave;
	private boolean resettedLoad;
	
	public ObjectTemplate() {
		super(null);
		identifier = null;
		timestamp = new LongTemplate("timestamp");
		timestamp.set(Long.valueOf(0));
		id = null;
		
		resetLoad();
		resetSave();
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
		Field[] fields = getFields();
		for(Field field : fields) {
			try {
				field.setAccessible(true);
				if(field.get(this) instanceof PrimitiveTemplate) {
					String templateName = ((PrimitiveTemplate) field.get(this)).name;
					if(input.containsKey(templateName)) {
						((Template) field.get(this)).set(input.get(templateName));
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
		Field[] fields = getFields();
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
	public boolean validate(Validator validator) {
		if(validated) {
			return true;
		}
		validated = true;
		
		boolean valid = true;
		Field[] fields = getFields();
		for(Field field : fields) {
			field.setAccessible(true);
			try {
				if(field.get(this) instanceof Template && !field.getName().equals("identifier")) {
					if(!((Template) field.get(this)).validate(validator)) {
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
	
	@Override
	public boolean validate() {
		return validate(null);
	}
	
	public HashMap <String, Object> renderToMap() {
		HashMap <String, Object> map = new HashMap <String, Object> ();
		Field[] fields = getFields();
		for(int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				Object object = field.get(this);
				if(object instanceof Template) {
					String name = ((Template) object).templateName;
					map.put(name, object);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public HashMap <String, Object> renderPrimitivesToMap(String[] names) {
		HashMap <String, Object> map = new HashMap <String, Object> ();
		Field[] fields = getFields();
		for(int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				Object object = field.get(this);
				if(object instanceof PrimitiveTemplate) {
					PrimitiveTemplate <?> template = ((PrimitiveTemplate <?>) object);
					if(names == null || Arrays.asList(names).contains(template.templateName)) {
						map.put(template.templateName, template.get());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public void parseFromParameters(Map <String, String> input) {
		updated();
		parseFromMap(null, input, null, true);
	}
	
	public void parseFromMap(Database database, Map <String, String> input, Map <String, ObjectTemplate> initialized, boolean wasUpdated) {

		Field[] fields = getFields();
		for(Field field : fields) {
			try {
				field.setAccessible(true);
				Object object = field.get(this);
				if(object instanceof Template) {
					String name = ((Template) object).templateName;
					//System.out.println(name + "=" + input.get(name));

					if(input.containsKey(name)) {
						String value = input.get(name);
						if(object instanceof Template) {
							/*
							if(object instanceof ObjectTemplateReference && database != null && initialized != null) {

								ObjectTemplateReference <?> reference = ((ObjectTemplateReference <?>) object);
								ObjectTemplate objectTemplate = (ObjectTemplate) reference.get();
								reference.set(checkIfInitialized(objectTemplate, database, value, initialized, wasUpdated, reference.getSupplier()));
								
							} else { */
							if(object instanceof PrimitiveTemplate || initialized != null) {
								((Template) object).parse(database, value, initialized);
								((Template) object).updated = wasUpdated;
							}
							//}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String render() throws Exception {
		resettedSave = false;

		String id = getId();
		this.id = id;
				
		if(updated && !rendered) {
			rendered = true;
						
			timestamp.set(System.currentTimeMillis());
			
			File file = database.getFile(getClass(), id);
			/*
			if(file.exists()) {
				throw new DatabaseException("File already exists");
			}
			*/
	
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Database.ENCODING));
			HashMap <String, Object> map = renderToMap();
			int counter = 0;
			for(Map.Entry <String, Object> entry : map.entrySet()) {
				bufferedWriter.write(entry.getKey() + "=" + ((Template) entry.getValue()).render());
				if(counter != map.size() - 1) {
					bufferedWriter.newLine();
				}
				counter++;
			}
			bufferedWriter.close();

		}

		return id;
	}
	
	public void checkIfUpdated(Database database) {
		this.database = database;

		if(checkedIfUpdated) {
			return;
		}
		checkedIfUpdated = true;

		Field[] fields = getFields();
		for(Field field : fields) {
			try {
				field.setAccessible(true);
				Object object = field.get(this);
				if(object instanceof Template) {
					
					if(object instanceof ComplexTemplate) {
						((ComplexTemplate) object).checkIfUpdated(database);
					}

					if(((Template) object).updated) {
						updated();
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void parse(Database database, StringBuilder string, Map <String, ObjectTemplate> initialized) throws Exception {
		resettedLoad = false;
		
		if(parsed) {
			return;
		}
		parsed = true;

		parsed();
		String id = crop(string).trim();
		this.id = id;
		this.database = database;
				
		File file = database.getFile(getClass(), id);
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
				parseFromMap(database, map, initialized, false);
			}
		}
	}
	
	@Override
	public boolean checkVersion(boolean overwrite) {
		if(checkedVersion) {
			return true;
		}
		checkedVersion = true;
		String id = getId();

		File file = database.getFile(getClass(), id);
		if(file.exists()) {
			if(!overwrite && updated) {
				return false;
			} else {
				ObjectTemplate clone;
				try {
					clone = getClass().getConstructor().newInstance();
					clone.parse(database, id, null);
					if((Long) clone.timestamp.get() > (Long) timestamp.get()) {
						return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

			}
		}

		try {
			Field[] fields = getFields();
			for(int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				Object object = field.get(this);
				if(object instanceof ComplexTemplate) {
					if(!((ComplexTemplate) object).checkVersion(true)) {
						return false;
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	public String getId() {
		if(id == null && database != null) {
			if(identifier == null) {
				StringBuilder idBuilder = new StringBuilder(Integer.toHexString(database.getNext(this.getClass())));
				while(idBuilder.length() < Database.COUNTER_LENGTH) {
					idBuilder.insert(0, "0");
				}
				return idBuilder.toString();
			} else {
				return Database.encrypt(identifier.getId());
			}
		} else {
			return id;
		}
	}

	private Field[] getFields() {
		Class <?> c = getClass();
		LinkedList <Field> output = getFields(c);
		Field[] array = new Field[output.size()];
		output.toArray(array);
		return output.toArray(array);
	}
	
	private LinkedList <Field> getFields(Class <?> c) {
		LinkedList <Field> output = new LinkedList <Field> ();
		output.addAll(Arrays.asList(c.getDeclaredFields()));
		if(c.getSuperclass() != Object.class) {
			output.addAll(getFields(c.getSuperclass()));
		}
		return output;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof ObjectTemplate) {
			if(id != null && ((ObjectTemplate) object).id != null && id.equals(((ObjectTemplate) object).id) && getClass() == object.getClass()) {
				return true;
			}
		}
		return false;
	}

	public void resetLoad() {
		if(!resettedLoad) {
			resettedSave = true;
			
			parsed = false;
			try {
				Field[] fields = getFields();
				for(int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					Object object = field.get(this);
					if(object instanceof ComplexTemplate) {
						((ComplexTemplate) object).resetLoad();
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void resetSave() {
		if(!resettedSave) {
			resettedSave = true;
			
			validated = false;
			rendered = false;
			checkedIfUpdated = false;
			checkedVersion = false;
			try {
				Field[] fields = getFields();
				for(int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					Object object = field.get(this);
					if(object instanceof ComplexTemplate) {
						((ComplexTemplate) object).resetSave();
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
