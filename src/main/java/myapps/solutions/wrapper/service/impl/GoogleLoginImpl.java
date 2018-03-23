package myapps.solutions.wrapper.service.impl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import myapps.solutions.wrapper.dao.IPasswordDAO;
import myapps.solutions.wrapper.dao.IUserDetailsDAO;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.service.IGoogleLogin;

@Service
// @Transactional("transactionManager")
public class GoogleLoginImpl implements IGoogleLogin {

	@Autowired
	IUserDetailsDAO userDetailsDAO;

	@Autowired
	IPasswordDAO passwordDAO;

	@Override
	public String url() {
		GoogleConnectionFactory connectionFactory = new GoogleConnectionFactory(
				"132502553552-pkkvs5f6dbhvhu4b2dnn7fbpfleo8595.apps.googleusercontent.com", "zH5aVRosCEbmk6-ZVg6CG8VE");
		OAuth2Operations oauthoperations = connectionFactory.getOAuthOperations();
		OAuth2Parameters params = new OAuth2Parameters();
		params.setRedirectUri("http://www.huddil.com");
		params.setScope(
				"https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
		params.set("approval_prompt", "force");
		// params.set("access_type","offline");
		String authorizeUrl = oauthoperations.buildAuthenticateUrl(GrantType.IMPLICIT_GRANT, params);
		return authorizeUrl;
	}

	@Override
	public String getProfileDetails(String accessToken) throws IOException, MessagingException {

		GoogleTemplate template = new GoogleTemplate(accessToken);

		String emailId = template.plusOperations().getGoogleProfile().getAccountEmail();
		String displayName = template.plusOperations().getGoogleProfile().getDisplayName();
		String givenName = template.plusOperations().getGoogleProfile().getGivenName();
		// String imageUrl =
		// template.plusOperations().getGoogleProfile().getImageUrl();
		String gender = template.plusOperations().getGoogleProfile().getGender();

		UserDetails userDetails = new UserDetails();
		userDetails.setEmailId(emailId);
		userDetails.setUserName(givenName);
		userDetails.setAddressingName(displayName);
		if (gender == null)
			userDetails.setGender("o");
		else {
			if (gender.equalsIgnoreCase("male"))
				userDetails.setGender("m");
			else if (gender.equalsIgnoreCase("female"))
				userDetails.setGender("f");
		}
		userDetails.setFromGoogle(true);
		System.out.println(emailId);
		return null;// userDetailsDAO.add(userDetails, null);
	}

	@Override
	public boolean createUser(UserDetails userDetails, String accessToken, String redirectUrl)
			throws AddressException, IOException, MessagingException {
		GoogleTemplate template = new GoogleTemplate(accessToken);
		System.out.println(template.plusOperations().getGoogleProfile().getAccountEmail());
		if (template.plusOperations().getGoogleProfile().getAccountEmail().equalsIgnoreCase(userDetails.getEmailId())) {
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
		GoogleTemplate template = new GoogleTemplate(accessToken);
		if (template.plusOperations().getGoogleProfile().getAccountEmail().equalsIgnoreCase(emailId))
			return userDetailsDAO.createSession(0, emailId, "", "", "", product);
		return new AuthorizationResult(AuthorizationCodes.Fail);
	}

	@Override
	public AuthorizationResult gAuthenticate(String emailId, String accessToken, String product) {
		GoogleTemplate template = new GoogleTemplate(accessToken);
		if (template.plusOperations().getGoogleProfile().getAccountEmail().equalsIgnoreCase(emailId))
			return userDetailsDAO.createSession(0, emailId, "", "", "", product);
		return new AuthorizationResult(AuthorizationCodes.Fail);
	}
}