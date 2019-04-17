package user;

import java.util.HashMap;

import database.Database;
import database.validator.Validator;
import mailer.Mailer;
import responder.RenderResponder;
import server.Request;
import server.Server;

public class UserManager {
	
	Server server;
	RenderResponder responder;
	Database database;
	Mailer mailer;

	public UserManager(Server server, RenderResponder responder, Database database, Mailer mailer) {
		this.server = server;
		this.responder = responder;
		this.database = database;
		this.mailer = mailer;
	}
	

	public void addSignInRoutes(String path, String file, String successRedirect, String userDoesNotExistError, String passwordDoesNotMatchError) {
		
		server.on("GET", path, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, "errors", variables);
			return responder.render(file, request.languages, variables);
		});
		
		server.on("POST", path, (Request request) -> {
			Validator validator = new Validator("errors");
			User user = null;
			if((user = (User) database.load(User.class, request.parameters.get("username"))) != null) {
				if(user.authenticate(request.parameters.get("password"))) {
					user.setLanguages(request.languages);
					database.update(user);					
					request.session.save(user);
					return responder.redirect(successRedirect);
				} else {
					validator.addMessage("password", passwordDoesNotMatchError);
				}
			} else {
				validator.addMessage("user", userDoesNotExistError);
			}
			request.session.addFlash(validator);
			return responder.redirect(path);
		});
		
	}
	
	public void addSignUpRoutes(String path, String file, String successRedirect, String usernameInUseError) {
		
		server.on("GET", path, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, "errors", variables);
			return responder.render(file, request.languages, variables);
		});
		
		server.on("POST", path, (Request request) -> {

			User user = new User();
			user.parseFromParameters(request.parameters);
			user.setLanguages(request.languages);

			Validator validator = new Validator("errors");
			
			if(user.validate(validator)) {
				if(database.save(user)) {
					request.session.save(user);
					
					sendActivationMail(user);
					
					return responder.redirect(successRedirect); 
				} else {
					validator.addMessage("username", usernameInUseError);
				}
			}

			request.session.addFlash(validator);
			return responder.redirect(path);
		});
		
	}
	
	
	public void addSignOutRoutes(String path, String successRedirect) {
		
		server.on("GET", "/signout", (Request request) -> {
			request.session.delete();
			return responder.redirect(successRedirect);
		});
		
	}

	public void addActivationRoutes(String path, String file, String successRedirect) {
	
		server.on("GET", path, (Request request) -> {	
			User user = null;
			if((user = (User) database.loadId(User.class, request.parameters.get("id"))) != null) {
				if(user.keyEquals(request.parameters.get("key"))) {
					request.session.save(user);
					user.setActivated(true);
					database.update(user);
					return responder.redirect(successRedirect);
				}
			}
			return responder.render(file, request.languages);
		});
		
	}
		
	public void addRecoveryRoutes(String path, String file, String successRedirect, String userDoesNotExistError, String notActivatedError) {
	
		server.on("GET", path, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, "errors", variables);
			return responder.render(file, request.languages, variables);
		});
		
		server.on("POST", path, (Request request) -> {
			Validator validator = new Validator("errors");
			User user = null;
			if((user = (User) database.load(User.class, request.parameters.get("username"))) != null) {
				if(user.isActivated()) {
					sendRecoverMail(user);
					return responder.redirect(successRedirect);
				} else {
					validator.addMessage("user", notActivatedError);
				}
			} else {
				validator.addMessage("user", userDoesNotExistError);
			}
			
			request.session.addFlash(validator);
			return responder.redirect("/recover");
		});
		
	}
	
	public void addRecoveryConfirmationRoutes(String path, String file) {
		
		server.on("GET", path, (Request request) -> {
			return responder.render(file, request.languages);
		});
		
	}
	
	public void addUnlockingRoutes(String path, String file, String successRedirect) {

		server.on("GET", path, (Request request) -> {	
			User user = null;
			if((user = (User) database.loadId(User.class, request.parameters.get("id"))) != null) {
				if(user.keyEquals(request.parameters.get("key"))) {
					request.session.save(user);
					return responder.redirect(successRedirect);
				}
			}
			return responder.render(file, request.languages);
		});
		
	}

		
	public void addProfileRoutes(String path, String file, String notSignedInRedirect) {
	
		server.on("GET", path, (Request request) -> {
			User user = (User) request.session.load();
			if(user != null) {
				HashMap <String, Object> variables = new HashMap <String, Object> ();
				variables.put("activated", user.isActivated());
				variables.put("notifications", user.notificationsEnabled());
				return responder.render(file, request.languages, variables);
			}
			return responder.redirect(notSignedInRedirect);
		});
		
	}
	
	public void addChangeMailRoutes(String path, String file, String notSignedInRedirect, String successRedirect) {
		
		server.on("GET", path, (Request request) -> {
			User user = (User) request.session.load();
			if(user != null) {
				HashMap <String, Object> variables = new HashMap <String, Object> ();
				addMessagesFlashToVariables(request, "errors", variables);
				variables.put("email", user.getMail());
				return responder.render(file, request.languages, variables);
			}
			return responder.redirect(notSignedInRedirect);
		});
		
		server.on("POST", path, (Request request) -> {
			Validator validator = new Validator("errors");
			User user = (User) request.session.load();
			if(user != null) {
				user.setMail(request.parameters.get("email"));
				user.setActivated(false);
				if(user.validate(validator)) {
					if(database.update(user)) {
						sendActivationMail(user);
						return responder.redirect(successRedirect);
					}
				}
			} else {
				responder.redirect(notSignedInRedirect);
			}
			
			request.session.addFlash(validator);
			return responder.redirect(path);
		});
		
	}
	
	public void addChangePasswordRoutes(String path, String file, String notSignedInRedirect, String successRedirect) {
		
		server.on("GET", path, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, "errors", variables);
			return responder.render(file, request.languages, variables);
		});
		
		server.on("POST", path, (Request request) -> {
			Validator validator = new Validator("errors");
			User user = (User) request.session.load();
			if(user != null) {
				user.setPassword(request.parameters.get("password"));
				if(user.validate(validator)) {
					if(database.update(user)) {
						return responder.redirect(successRedirect);
					}
				}
			} else {
				responder.redirect(notSignedInRedirect);
			}
			
			request.session.addFlash(validator);
			return responder.redirect(path);
		});
		
	}
	
	public void addChangeNotificationsRoutes(String path, String notSignedInRedirect, String successRedirect) {

		server.on("GET", path, (Request request) -> {
			User user = (User) request.session.load();
			if(user != null) {
				user.toggleNotifications();
				if(user.validate()) {
					database.update(user);
				}
			} else {
				return responder.redirect(notSignedInRedirect);
			}
			
			return responder.redirect(successRedirect);
		});
		
	}
	
	public void addDeletionRoutes(String path, String file, String confirmationPath, String successRedirect, String deletionError) {
		
		server.on("GET", path, (Request request) -> {
			HashMap <String, Object> variables = new HashMap <String, Object> ();
			addMessagesFlashToVariables(request, "errors", variables);
			return responder.render(file, request.languages, variables);
		});
		
		server.on("GET", confirmationPath, (Request request) -> {
			if(request.session.load() != null) {
				if(database.delete(User.class, ((String) request.session.load()))) {
					request.session.delete();
					return responder.redirect(successRedirect);
				}
			}
			Validator validator = new Validator("errors");
			validator.addMessage("user", deletionError);
			request.session.addFlash(validator);
			return responder.redirect(path);
		});
		
	}
	
	private void sendActivationMail(User user) {
		HashMap <String, Object> variables = new HashMap <String, Object> ();
		variables.put("username", user.getUsername());
		variables.put("encrypted-username", Database.encrypt(user.getUsername()));
		variables.put("key", user.getKey());
		mailer.send(user.getMail(), "{{print translate \"activate-account\"}}", "activate.html", user.getLanguages(), variables);
	}
	
	private void sendRecoverMail(User user) {
		HashMap <String, Object> variables = new HashMap <String, Object> ();
		variables.put("username", user.getUsername());
		variables.put("encrypted-username", Database.encrypt(user.getUsername()));
		variables.put("key", user.getKey());
		mailer.send(user.getMail(), "{{print translate \"recover-account\"}}", "recover.html", user.getLanguages(), variables);
	}

	private void addMessagesFlashToVariables(Request request, String name, HashMap <String, Object> variables) {
		Validator validator = (Validator) request.session.getFlash(name);
		if(validator != null) {
			validator.addToVariables(variables);
		}
	}
	
}
