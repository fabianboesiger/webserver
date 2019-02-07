package application;

import database.templates.ObjectTemplate;
import database.templates.StringTemplate;

public class User extends ObjectTemplate {

	private static final long serialVersionUID = 606839185936007810L;
	
	public StringTemplate username;
	public StringTemplate password;
	
	public User() {
		super("user");
		username = new StringTemplate(4, 16);
		password = new StringTemplate(4, 64);
		setId(username);
	}

}
