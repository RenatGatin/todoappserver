package ca.gatin.api.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ca.gatin.model.security.User;
import ca.gatin.model.signup.PseudoUser;

@Service
public class EmailService {
	
	private JavaMailSender javaMailSender;
	
	@Autowired
	private Environment env;
	
	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void test() {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo("renat.gatin@gmail.com");
		mail.setFrom("emc2.software.lab@gmail.com");
		mail.setSubject("Test subject");
		mail.setText("Test mail body text. Timestamp: " + new Date());
		
		javaMailSender.send(mail);
	}
	
	public void sendActivationKey(PseudoUser user) {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom("emc2.software.lab@gmail.com");
		mail.setSubject("[GitHub] Please activate your account");
		mail.setText("Hey, " + user.getFirstname() + "!" +
					"\nWelcome on board!" + 
					 "\n\nYou can use this Key:" + 
				     "\n" + user.getActivationKey() + 
					 "\nto activate your account." + 
				     "\n\nOr click this link: " + "http://todoapp.rg:8080/customer/#/sign-up?username=" + user.getEmail() + "&key=" + user.getActivationKey() +
				     "\nand follow the instuctions." + 
				     "\n\nTimestamp: " + new Date());
		
		javaMailSender.send(mail);
	}
	
	public void sendResetPasswordKey(User user, String resetPasswordKey) {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom("emc2.software.lab@gmail.com");
		mail.setSubject("[ToDoApp] Please reset your password");
		mail.setText("We heard that you lost your ToDoApp password. Sorry about that!" +
					 "\n\nBut don’t worry! You can use the following link to reset your password:" + 
					 "\n\n " + "http://todoapp.rg:8080/customer/#/password-do-reset/" + resetPasswordKey  +
				     "\n\nIf you don’t use this link within 3 hours, it will expire. To get a new password reset link, visit http://todoapp.rg:8080/customer/#/password-reset" + 
					 "\n\nThanks," + 
				     "\nYour friends at ToDoApp" + 
				     "\n\nTimestamp: " + new Date());
		
		javaMailSender.send(mail);
	}

}
