package myapps.solutions.wrapper.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import myapps.solutions.wrapper.model.AccountDevice;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.model.State;
import myapps.solutions.wrapper.model.SyncDetails;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.model.UserSession;

public interface IUserDetailsService {

	// UserDetails operations
	boolean add(UserDetails userDetails, String redirectUrl) throws IOException, AddressException, MessagingException;
	UserDetails get(String sessionId);
	boolean update(String sessionId, UserDetails userDetails);
	boolean delete(String sessionId);
	boolean deleteUserAccountThroughEmail(String emailId, String token);
	boolean activateAcc(String sessionId, boolean state);
	
	// AccountDevice operations
	AuthorizationCodes addAccDevice(AccountDevice accountDevice, int userId);
	List<AccountDevice>getAccDevices(String sessionId);

	// Verify if the data already exists
	boolean isUserNameExist(String userName);
	boolean isMobileNoExist(String mobileNo);
	boolean isEmailIdExists(String emailId);
	String checkForExistingUserDetails(UserDetails userDetails);
	
	//Authorization 
	AuthorizationResult canAuthenticate(String userName, byte[] password, String macAddress, String deviceName, String deviceType, String product);
	AuthorizationResult createSession(int userId, String macAddress, String product);
	boolean logout(String sessionId, String product);
	
	//Session
	List<UserSession> getUserSessions();
	boolean blockUser(String sessionId, int state, String user);
	List<State> stateList();
    
    //Activate Account
    int activateAccountThroughEmail(String emailId, String token, String product);
    boolean verifyMobileNumberWithOTP(String OTP, String mobileNo, String productName, String userType, String redirectUrl) throws MessagingException, IOException;
    
    //Sync Operation
    SyncDetails syncDetails(String sessionId);
    
    //Update Mobile Number
    int updateMobileNumber(String sessionId, String newMobileNumber, String otp);
    int mobileNumberVerificationWithOTP(String sessionId, String otp, String mobileNo);

    
	int getUserStatus(String data);
}
