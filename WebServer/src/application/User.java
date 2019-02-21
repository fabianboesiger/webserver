package application;

import database.templates.ObjectTemplate;
import database.templates.StringTemplate;

public class User extends ObjectTemplate {

	private static final long serialVersionUID = 606839185936007810L;
	
	private StringTemplate username;
	private StringTemplate password;
	
	public User() {
		super("user");
		username = new StringTemplate("username", 4, 16);
		password = new StringTemplate("password", 4, 64);
		setId(username);
	}
	
	public boolean authenticate(String password) {
		if(password.equals(this.password.get())) {
			return true;
		}
		return false;
	}

}
