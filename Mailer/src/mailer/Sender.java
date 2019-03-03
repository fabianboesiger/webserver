package mailer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Sender implements Runnable {
	
	String email;
	String subject;
	String body;
	String type;
	
	public Sender(String email, String subject, String body, String type) {
		this.email = email;
		this.subject = subject;
		this.body = body;
		this.type = type;
		
		new Thread(this).start();
	}

	@Override
	public void run() {
		
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(Mailer.MAIL_DATA), Mailer.ENCODING));			
			String username = bufferedReader.readLine();
			String password = bufferedReader.readLine();
			
			Properties props = new Properties();
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.socketFactory.port", "587");
	        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.port", "587");
	
	        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        });
	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(username));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
	            message.setSubject(subject);
	            message.setContent(body, type + "; charset=" + Mailer.ENCODING.name().toLowerCase());
	            Transport.send(message);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
