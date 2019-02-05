package database.structures;

import java.util.HashMap;

public class DataObject extends Data {
	
	private HashMap <String, Data> fields;
	
	public DataObject() {
		fields = new HashMap <String, Data> ();
	}
	
}
