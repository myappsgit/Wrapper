package myapps.solutions.wrapper.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.UserDetails;

public interface IFacebookLogin {
	String loginUrl();
	boolean createUser(UserDetails userDetails, String accessToken, String redirectUrl) throws AddressException, IOException, MessagingException;
	AuthorizationResult authenticate(String emailId, String accessToken, String product);
	AuthorizationResult fAuthenticate(String emailId, String accessToken, String product);

}
