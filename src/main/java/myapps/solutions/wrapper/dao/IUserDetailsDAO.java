package myapps.solutions.wrapper.dao;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import myapps.solutions.wrapper.model.AccountDevice;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.model.State;
import myapps.solutions.wrapper.model.SyncDetails;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.model.UserSession;

public interface IUserDetailsDAO {
	
	// UserDetails operations
	boolean add(UserDetails userDetails, String redirectUrl) throws IOException, AddressException, MessagingException;
	UserDetails get(String sessionId);
	boolean update(String sessionId, UserDetails userDetails);
	boolean delete(String sessionId);
	boolean deleteUserAccountThroughEmail(String emailId, String token);
	boolean activateAcc(String sessionId, boolean state);

	// AccountDevice operations
	AuthorizationCodes addAccDevice(AccountDevice accountDevice, int userId);
	List<AccountDevice> getAccDevice(String sessionId);

	// Verify if the data already exist
	boolean isUserNameExist(String userName);
	boolean isMobileNoExist(String mobileNo);
	boolean isEmailIdExists(String emailId);
	
	//Authorization operations
	AuthorizationResult canAuthenticate(String userName, byte[] password);
	
	//Session operations
	List<UserSession> getSessions();
	AuthorizationResult createSession(int userId, String emailId, String deviceName, String deviceType, String macAddress, String product);
	boolean isSessionExistWithDifferentUser(int userId, String macAddress);
	boolean logout(String sessionId);
	String getCashupUserType(String sessionId);
	boolean blockUser(String sessionId, int state, String user);
    SyncDetails syncDetails(String sessionId);

	List<State> sateList();
    
    //Account Activation
    int activateAccountThroughEmail(String emailId, String token, String product);
    boolean verifyMobileNumberWithOTP(String OTP, String mobileNo, String productName, String userType, String redirectUrl)throws  MessagingException, IOException;
    
    //OAuth
    public User getByUsername(String username);
    public BaseClientDetails getByClientId(String clientId);
    
    
	boolean logoutHuddil(String sessionId);
	void createHuddilUser(int userId, int userType, String emailId, String mobileNo, String displayName);
	
	//Update Mobile Number
	int updateMobileNumber(String sessionId, String newMobileNumber, String otp);
	int updateHuddilMobileNumber(String newMobilenumber, String sessionId, String displayName);
	
	int mobileNumberVerificationWithOTP(String sessionId, String otp, String mobileNo);
	
	int getUserStatus(String data);
}
