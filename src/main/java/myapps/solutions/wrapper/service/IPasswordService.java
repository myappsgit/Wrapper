package myapps.solutions.wrapper.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import myapps.solutions.wrapper.model.PasswordChange;
import myapps.solutions.wrapper.model.UserType;

public interface IPasswordService {
	
	int changePassword(PasswordChange passwordChange, String sessionId);
	int changePasswordWithResetOTP(PasswordChange passwordChanage, String OTP);

	boolean sendPasswordRestLink(String emailId, String url, String productName) throws IOException, MessagingException;
	int sendPasswordRestOTP(String sessionId, String mobileNo, String product) throws IOException;		
	
	boolean resendActivationLink(String emailId, UserType userType, String productName, String url) throws AddressException, IOException, MessagingException;
	boolean resendActivationOTP(String mobileNo, String product) throws IOException;
	
	int resetPassword(String emailId, String token, PasswordChange passwordChange);
}
