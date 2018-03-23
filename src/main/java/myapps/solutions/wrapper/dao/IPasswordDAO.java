package myapps.solutions.wrapper.dao;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import myapps.solutions.wrapper.model.PasswordChange;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.model.UserType;

public interface IPasswordDAO {

	int changePassword(PasswordChange passwordChange, String sessionId);
	int changePassword(String OTP, PasswordChange passwordChange);
	
	boolean sendPasswordResetLink(String emailId, String url, String productName)throws IOException, MessagingException;
	int sendPasswordResetOTP(String sessionId, String mobileNo, String product) throws IOException;
	
	boolean resendActivationLink(UserDetails userDetails, String url) throws AddressException, IOException, MessagingException;
	boolean resendActivationLink(String emailId, UserType userType, String productName, String url) throws IOException, MessagingException;
	boolean resendActivationOTP(String mobileNo, String product) throws IOException;
	
	int resetPassword(String emailId, String token, PasswordChange passwordChange);
}
