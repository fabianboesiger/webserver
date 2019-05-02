package user;

import java.util.HashMap;

import database.Database;
import database.validator.Validator;
import mailer.Mailer;
import responder.RenderResponder;
import server.Request;
import server.Server;

public class UserManager {
	
	public static final String ERRORS_NAME = "errors";
	public static final String USERNAME_NAME = "username";
	public static final String ENCRYPTED_USERNAME_NAME = "encrypted-username";
	public static final String KEY_NAME = "key";
	public static final String ACTIVATED_NAME = "activated";
	public static final String NOTIFICATIONS_NAME = "notifications";
	public static final String EMAIL_NAME = "email";
	public static final String USER_NAME = "user";
	public static final String PASSWORD_NAME = "password";
	public static final String ID_NAME = "id";
	
	public static final String SIGNIN_FILE = "/profile/signin.html";
	public static final String SIGNUP_FILE = "/profile/signup.html";
	public static final String ACTIVATE_FILE = "/profile/activate-error.html";
	public static final String PROFILE_FILE = "/profile/profile.html";
	public static final String RECOVER_FILE = "/profile/recover.html";
	public static final String RECOVER_CONFIRM_FILE = "/profile/recover-confirm.html";
	public static final String UNLOCK_FILE = "/profile/unlock-error.html";
	public static final String EMAIL_FILE = "/profile/change-email.html";
	public static final String PASSWORD_FILE = "/profile/change-password.html";
	public static final String DELETE_FILE = "/profile/delete-confirm.html";
	
	public static final String SIGNIN_PATH = "/signin";
	public static final String SIGNUP_PATH = "/signup";
	public static final String SIGNOUT_PATH = "/signout";
	public static final String ACTIVATE_PATH = "/activate";
	public static final String PROFILE_PATH = "/profile";
	public static final String RECOVER_PATH = "/recover";
	public static final String RECOVER_CONFIRM_PATH = "/recover/confirm";
	public static final String UNLOCK_PATH = "/unlock";
	public static final String EMAIL_PATH = "/profile/email";
	public static final String PASSWORD_PATH = "/profile/password";
	public static final String NOTIFICATION_PATH = "/profile/notifications";
	public static final String DELETE_PATH = "/profile/delete";
	public static final String DELETE_CONFIRM_PATH = "/profile/delete/confirm";
	
	public static final String LOGIN_REDIRECT = "/";
	public static final String LOGOUT_REDIRECT = "/";
	
	public static final String ACTIVATE_MAIL = "/profile/activate.html";
	public static final String RECOVER_MAIL = "/profile/recover.html";
	
	public static final String DOES_NOT_MATCH_KEY = "does-not-match";
	public static final String DOES_NOT_EXIST_KEY = "does-not-exist";
	public static final String IN_USE_KEY = "in-use";
	public static final String NOT_ACTIVATED_KEY = "not-activated";
	public static final String DELETION_ERROR_KEY = "deletion-error";
	public static final String ACTIVATE_ACCOUNT_KEY = "activate-account";
	public static final String RECOVER_ACCOUNT_KEY = "recover-account";
	
	Server server;
	RenderResponder responder;
	Database database;
	Mailer mailer;
	HashMap <String, Object> predefined;
	
	ModificationAction onCreate;
	ModificationAction onDelete;
	
	public UserManager(Server server, RenderResponder responder, Database database, Mailer mailer, HashMap <String, Object> predefined) {
		this(server, responder, database, mailer, predefined, null, null);
	}

	public UserManager(Server server, RenderResponder responder, Database database, Mailer mailer, HashMap <String, Object> predefined, ModificationAction onCreate, ModificationAction onDelete) {
		this.server = server;
		this.responder = responder;
		this.database = database;
		this.mailer = mailer;
		this.predefined = predefined;
		this.onCreate = onCreate;
		this.onDelete = onDelete;
		
		initializeRoutes();
	}

	public void initializeRoutes() {
		
		server.on("GET", SIGNIN_PATH, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, ERRORS_NAME, variables);
			return responder.render(SIGNIN_FILE, request.languages, variables);
		});
		
		server.on("POST", SIGNIN_PATH, (Request request) -> {
			Validator validator = new Validator(ERRORS_NAME);
			User user = null;
			if((user = (User) database.load(User.class, request.parameters.get(USERNAME_NAME))) != null) {
				if(user.authenticate(request.parameters.get(PASSWORD_NAME))) {
					user.setLanguages(request.languages);
					database.update(user);					
					request.session.save(user);
					return responder.redirect(LOGIN_REDIRECT);
				} else {
					validator.addMessage(PASSWORD_NAME, DOES_NOT_MATCH_KEY);
				}
			} else {
				validator.addMessage(USER_NAME, DOES_NOT_EXIST_KEY);
			}
			request.session.addFlash(validator);
			return responder.redirect(SIGNIN_PATH);
		});
		
		server.on("GET", SIGNUP_PATH, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, ERRORS_NAME, variables);
			return responder.render(SIGNUP_FILE, request.languages, variables);
		});
		
		server.on("POST", SIGNUP_PATH, (Request request) -> {

			User user = new User();
			user.parseFromParameters(request.parameters);
			user.setLanguages(request.languages);

			Validator validator = new Validator(ERRORS_NAME);
			
			if(user.validate(validator)) {
				if(database.save(user)) {
					request.session.save(user);
					if(onCreate != null) {
						onCreate.run(user);
					}
					sendActivationMail(user);
					return responder.redirect(LOGIN_REDIRECT); 
				} else {
					validator.addMessage(USERNAME_NAME, IN_USE_KEY);
				}
			}

			request.session.addFlash(validator);
			return responder.redirect(SIGNUP_PATH);
		});
		
		server.on("GET", SIGNOUT_PATH, (Request request) -> {
			request.session.delete();
			return responder.redirect(LOGOUT_REDIRECT);
		});
	
		server.on("GET", ACTIVATE_PATH, (Request request) -> {	
			User user = null;
			if((user = (User) database.loadId(User.class, request.parameters.get(ID_NAME))) != null) {
				if(user.keyEquals(request.parameters.get(KEY_NAME))) {
					request.session.save(user);
					user.setActivated(true);
					database.update(user);
					return responder.redirect(PROFILE_PATH);
				}
			}
			return responder.render(ACTIVATE_FILE, request.languages);
		});
	
		server.on("GET", RECOVER_PATH, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, ERRORS_NAME, variables);
			return responder.render(RECOVER_FILE, request.languages, variables);
		});
		
		server.on("POST", RECOVER_PATH, (Request request) -> {
			Validator validator = new Validator(ERRORS_NAME);
			User user = null;
			if((user = (User) database.load(User.class, request.parameters.get(USERNAME_NAME))) != null) {
				if(user.isActivated()) {
					sendRecoverMail(user);
					return responder.redirect(RECOVER_CONFIRM_PATH);
				} else {
					validator.addMessage(USER_NAME, NOT_ACTIVATED_KEY);
				}
			} else {
				validator.addMessage(USER_NAME, DOES_NOT_EXIST_KEY);
			}
			
			request.session.addFlash(validator);
			return responder.redirect(RECOVER_PATH);
		});
		
		
		server.on("GET", RECOVER_CONFIRM_PATH, (Request request) -> {
			return responder.render(RECOVER_CONFIRM_FILE, request.languages);
		});
		
		server.on("GET", UNLOCK_PATH, (Request request) -> {	
			User user = null;
			if((user = (User) database.loadId(User.class, request.parameters.get(ID_NAME))) != null) {
				if(user.keyEquals(request.parameters.get(KEY_NAME))) {
					request.session.save(user);
					return responder.redirect(PASSWORD_PATH);
				}
			}
			return responder.render(UNLOCK_FILE, request.languages);
		});
	
		server.on("GET", PROFILE_PATH, (Request request) -> {
			User user = (User) request.session.load();
			if(user != null) {
				HashMap <String, Object> variables = new HashMap <String, Object> ();
				variables.put(ACTIVATED_NAME, user.isActivated());
				variables.put(NOTIFICATIONS_NAME, user.notificationsEnabled());
				return responder.render(PROFILE_FILE, request.languages, variables);
			}
			return responder.redirect(SIGNIN_PATH);
		});
		
		server.on("GET", EMAIL_PATH, (Request request) -> {
			User user = (User) request.session.load();
			if(user != null) {
				HashMap <String, Object> variables = new HashMap <String, Object> ();
				addMessagesFlashToVariables(request, ERRORS_NAME, variables);
				variables.put(EMAIL_NAME, user.getMail());
				return responder.render(EMAIL_FILE, request.languages, variables);
			}
			return responder.redirect(SIGNIN_PATH);
		});
		
		server.on("POST", EMAIL_PATH, (Request request) -> {
			Validator validator = new Validator(ERRORS_NAME);
			User user = (User) request.session.load();
			if(user != null) {
				user.setMail(request.parameters.get(EMAIL_NAME));
				user.setActivated(false);
				if(user.validate(validator)) {
					if(database.update(user)) {
						sendActivationMail(user);
						return responder.redirect(PROFILE_PATH);
					}
				}
			} else {
				responder.redirect(SIGNIN_PATH);
			}
			
			request.session.addFlash(validator);
			return responder.redirect(EMAIL_PATH);
		});
		
		server.on("GET", PASSWORD_PATH, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, ERRORS_NAME, variables);
			return responder.render(PASSWORD_FILE, request.languages, variables);
		});
		
		server.on("POST", PASSWORD_PATH, (Request request) -> {
			Validator validator = new Validator(ERRORS_NAME);
			User user = (User) request.session.load();
			if(user != null) {
				user.setPassword(request.parameters.get(PASSWORD_NAME));
				if(user.validate(validator)) {
					if(database.update(user)) {
						return responder.redirect(PROFILE_PATH);
					}
				}
			} else {
				responder.redirect(SIGNIN_PATH);
			}
			
			request.session.addFlash(validator);
			return responder.redirect(PASSWORD_PATH);
		});
		
		server.on("GET", NOTIFICATION_PATH, (Request request) -> {
			User user = (User) request.session.load();
			if(user != null) {
				user.toggleNotifications();
				if(user.validate()) {
					database.update(user);
				}
			} else {
				return responder.redirect(SIGNIN_PATH);
			}
			
			return responder.redirect(PROFILE_PATH);
		});
			
		server.on("GET", DELETE_PATH, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, ERRORS_NAME, variables);
			return responder.render(DELETE_FILE, request.languages, variables);
		});
		
		server.on("GET", DELETE_CONFIRM_PATH, (Request request) -> {
			if(request.session.load() != null) {
				User user = (User) request.session.load();
				if(user != null) {
					if(database.delete(User.class, user.getUsername())) {
						if(onDelete != null) {
							onDelete.run(user);
						}
						request.session.delete();
						return responder.redirect(LOGOUT_REDIRECT);
					}
				}
			}
			Validator validator = new Validator(ERRORS_NAME);
			validator.addMessage(USER_NAME, DELETION_ERROR_KEY);
			request.session.addFlash(validator);
			return responder.redirect(DELETE_PATH);
		});
		
		server.on("ALL", ".*", (Request request) -> {
			User user = (User) request.session.load();
			if(user == null) {
				predefined.put("username", null);
			} else {
				predefined.put("username", user.getUsername());
			}
			return responder.next();
		});
		
	}
	
	private void sendActivationMail(User user) {
		HashMap <String, Object> variables = new HashMap <String, Object> ();
		variables.put(USERNAME_NAME, user.getUsername());
		variables.put(ENCRYPTED_USERNAME_NAME, Database.encrypt(user.getUsername()));
		variables.put(KEY_NAME, user.getKey());
		mailer.send(user.getMail(), "{{print translate \"" + ACTIVATE_ACCOUNT_KEY + "\"}}", ACTIVATE_MAIL, user.getLanguages(), variables);
	}
	
	private void sendRecoverMail(User user) {
		HashMap <String, Object> variables = new HashMap <String, Object> ();
		variables.put(USERNAME_NAME, user.getUsername());
		variables.put(ENCRYPTED_USERNAME_NAME, Database.encrypt(user.getUsername()));
		variables.put(KEY_NAME, user.getKey());
		mailer.send(user.getMail(), "{{print translate \"" + RECOVER_ACCOUNT_KEY + "\"}}", RECOVER_MAIL, user.getLanguages(), variables);
	}

	private void addMessagesFlashToVariables(Request request, String name, HashMap <String, Object> variables) {
		Validator validator = (Validator) request.session.getFlash(name);
		if(validator != null) {
			validator.addToVariables(variables);
		}
	}
	
}
