package database.templates;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjectTemplate implements Template {
	
	private ObjectTemplateField idTemplate;
	private String id;
	private String name;
	
	public ObjectTemplate(String name) {
		this.name = name;
	}
	
	public void setId(ObjectTemplateField idTemplate) {
		this.idTemplate = idTemplate;
	}
	
	public String getId() throws ObjectTemplateException {
		if(id == null) {
			if(idTemplate != null) {
				if(idTemplate.get() != null) {
					id = idTemplate.get().toString();
					return id;
				}
			}
			return null;
		} else {
			return id;
		}
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unused")
	public void setFromStringMap(Map <String, String> input) {
		System.out.println("in "+input.toString());
		HashMap <String, Object> output = new HashMap <String, Object> ();
		for(Map.Entry <String, String> entry : input.entrySet()) {
			output.put(entry.getKey(), entry.getValue());
		}
		System.out.println("out "+output.toString());
		set(output);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void set(Object object) {
		Map <String, Object> input = (Map <String, Object>) object;
		System.out.println("!2.1");
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			String name = field.getName();
			System.out.println("2.1.1" + name + " " + input.toString());
			if(input.containsKey(name)) {
				System.out.println("!2.2" + name);
				field.setAccessible(true);
				try {
					if(field.get(this) instanceof Template) {
						((Template) field.get(this)).set(input.get(name));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public boolean validate(List <Map <String, String>> errors) {
		System.out.println("3.1");
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
		System.out.println("3.2" + valid);

		return valid;
	}

	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
