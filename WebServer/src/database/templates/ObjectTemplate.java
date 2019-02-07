package database.templates;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

public class ObjectTemplate implements Template, Serializable {

	private static final long serialVersionUID = -3949241716730057312L;
	
	private transient ObjectTemplateField idTemplate;
	private transient String name;
	
	public ObjectTemplate(String name) {
		this.name = name;
	}
	
	public void setId(ObjectTemplateField idTemplate) {
		this.idTemplate = idTemplate;
	}
	
	public String getId() throws ObjectTemplateException {
		if(idTemplate != null) {
			if(idTemplate.get() != null) {
				return idTemplate.get().toString();
			}
			return null;
		}
		throw new ObjectTemplateException("No ID defined");
	}
	
	public String getName() {
		return name;
	}
	
	public void parse(HashMap <String, String> input) {
		Field[] fields = getClass().getDeclaredFields();
		for(Field field : fields) {
			String name = field.getName();
			if(input.containsKey(name)) {
				try {
					((ObjectTemplateField) field.get(this)).fromString(input.get(name));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public boolean validate() {
		return false;
	}
	
}
