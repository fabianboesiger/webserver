package database.templates;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectTemplate extends Template {
	
	private PrimitiveTemplate idTemplate;
	private String id;
	private String name;
	
	public ObjectTemplate(String name) {
		super(name);
	}
	
	public void setId(PrimitiveTemplate idTemplate) {
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
	
	public void setFromStringMap(Map <String, String> input) {
		HashMap <String, Object> output = new HashMap <String, Object> ();
		for(Map.Entry <String, String> entry : input.entrySet()) {
			output.put(entry.getKey(), entry.getValue());
		}
		set(output);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void set(Object object) {
		Map <String, Object> input = (Map <String, Object>) object;
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			String name = field.getName();
			if(input.containsKey(name)) {
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

	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
