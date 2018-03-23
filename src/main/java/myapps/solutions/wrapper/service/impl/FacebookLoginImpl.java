package myapps.solutions.wrapper.service.impl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import myapps.solutions.wrapper.dao.IPasswordDAO;
import myapps.solutions.wrapper.dao.IUserDetailsDAO;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.service.IFacebookLogin;

@Service
public class FacebookLoginImpl implements IFacebookLogin {

	@Autowired
	IUserDetailsDAO userDetailsDAO;

	@Autowired
	IPasswordDAO passwordDAO;

	public String loginUrl() {

		FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory("334788613700258",
				"b11919c739670f0edf833a8f860a2381");
		OAuth2Operations oauthoperations = connectionFactory.getOAuthOperations();
		OAuth2Parameters params = new OAuth2Parameters();

		params.setRedirectUri("http://localhost:8080");
		params.setScope("user_status");
		String authorizeUrl = oauthoperations.buildAuthenticateUrl(GrantType.IMPLICIT_GRANT, params);
		System.out.println(authorizeUrl);
		return authorizeUrl;
	}

	@Override
	public boolean createUser(UserDetails userDetails, String accessToken, String redirectUrl)
			throws AddressException, IOException, MessagingException {
		FacebookTemplate template = new FacebookTemplate(accessToken);
		String[] email = { "email" };
		User profile = template.fetchObject("me", User.class, email);
		System.out.println(profile.getEmail());
		if (profile.getEmail().equals(userDetails.getEmailId())) {
			userDetails.setFromGoogle(true);
			userDetails.setAuthorization(null);
			userDetails.setIsActive(0);
			if (userDetailsDAO.add(userDetails, redirectUrl)) {
				userDetailsDAO.createHuddilUser(userDetails.getId(),
						userDetails.getUserSubscriptions().get(0).getUserType().getId(), userDetails.getEmailId(),
						userDetails.getMobileNo(), userDetails.getAddressingName());
				return true;
			}
		}
		return false;
	}

	@Override
	public AuthorizationResult authenticate(String emailId, String accessToken, String product) {
		return new AuthorizationResult(AuthorizationCodes.Fail);
	}

	@Override
	public AuthorizationResult fAuthenticate(String emailId, String accessToken, String product) {
		FacebookTemplate template = new FacebookTemplate(accessToken);
		String[] email = { "email" };
		User profile = template.fetchObject("me", User.class, email);
		if (profile.getEmail().equals(emailId))
			return userDetailsDAO.createSession(0, emailId, "", "", "", product);
		return new AuthorizationResult(AuthorizationCodes.Fail);
	}
}
