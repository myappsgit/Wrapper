package myapps.solutions.wrapper.service.impl;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myapps.solutions.wrapper.dao.IPasswordDAO;
import myapps.solutions.wrapper.dao.IUserDetailsDAO;
import myapps.solutions.wrapper.model.AccountDevice;
import myapps.solutions.wrapper.model.Authorization;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.model.State;
import myapps.solutions.wrapper.model.SyncDetails;
import myapps.solutions.wrapper.model.Tokens;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.model.UserSession;
import myapps.solutions.wrapper.service.IUserDetailsService;
import myapps.solutions.wrapper.utils.Auth;
import myapps.solutions.wrapper.utils.Misc;
import myapps.solutions.wrapper.utils.ResponseCode;

@Service
public class UserDetailsServiceImpl implements IUserDetailsService {

	@Autowired
	IUserDetailsDAO userDetailsDAO;

	@Autowired
	IPasswordDAO pass;

	/***
	 * UserDetails Operations
	 * 
	 * @throws IOException
	 * @throws MessagingException
	 */
	@Override
	public boolean add(UserDetails userDetails, String redirectUrl) throws IOException, MessagingException {
		Authorization authorization = userDetails.getAuthorization();
		if (authorization != null && authorization.getPassword() != null && authorization.getPassword().length > 0) {
			byte[] salt = Auth.generateSalt();
			authorization.setPassword(Auth.generateHash(Auth.decrypt(authorization.getPassword()), salt));
			authorization.setSalt(salt);
		}
		userDetails.setIsActive(-1);
		// userDetails.setAge(Misc.calculateAge(userDetails.getDob()));
		userDetails.setFromGoogle(false);
		String token = Misc.randomAlphaNumeric(Auth.generateSalt().toString());
		userDetails.setTokens(new Tokens(userDetails, token, ""));
		if (userDetailsDAO.add(userDetails, redirectUrl)) {
			userDetailsDAO.createHuddilUser(userDetails.getId(),
					userDetails.getUserSubscriptions().get(0).getUserType().getId(), userDetails.getEmailId(),
					userDetails.getMobileNo(), userDetails.getAddressingName());
			pass.resendActivationLink(userDetails, redirectUrl);
			return true;
		}
		return false;
	}

	@Override
	public UserDetails get(String sessionId) {
		return userDetailsDAO.get(sessionId);
	}

	@Override
	public boolean update(String sessionId, UserDetails userDetails) {
		if (userDetailsDAO.update(sessionId, userDetails)) {
			return userDetailsDAO.updateHuddilMobileNumber("", sessionId, userDetails.getAddressingName()) == 1;
		} else
			return false;
	}

	@Override
	public boolean delete(String sessionId) {
		return userDetailsDAO.delete(sessionId);
	}

	@Override
	public boolean deleteUserAccountThroughEmail(String emailId, String token) {
		return userDetailsDAO.deleteUserAccountThroughEmail(emailId, token);

	}

	@Override
	public boolean activateAcc(String sessionId, boolean state) {
		return userDetailsDAO.activateAcc(sessionId, state);
	}

	/***
	 * AccountDevice operations
	 */
	@Override
	public AuthorizationCodes addAccDevice(AccountDevice accountDevice, int userId) {
		return userDetailsDAO.addAccDevice(accountDevice, userId);
	}

	@Override
	public List<AccountDevice> getAccDevices(String sessionId) {
		return userDetailsDAO.getAccDevice(sessionId);
	}

	/***
	 * Verify whether the userName, mobileNo, emailId already exist
	 */
	@Override
	public boolean isUserNameExist(String userName) {
		return userDetailsDAO.isUserNameExist(userName);
	}

	@Override
	public boolean isMobileNoExist(String mobileNo) {
		return userDetailsDAO.isMobileNoExist(mobileNo);
	}

	@Override
	public boolean isEmailIdExists(String emailId) {
		return userDetailsDAO.isEmailIdExists(emailId);
	}

	@Override
	public String checkForExistingUserDetails(UserDetails userDetails) {
		String value = userDetails.getProduct();
		if (TextUtils.isEmpty(value) || !value.equalsIgnoreCase("huddil"))
			return ResponseCode.invalidData;
		value = userDetails.getUserType();
		if (TextUtils.isEmpty(value) || (!value.equalsIgnoreCase("consumer")
				&& !value.equalsIgnoreCase("service provider") && !value.equalsIgnoreCase("advisor")))
			return ResponseCode.invalidData;

		/*value = userDetails.getMobileNo();
		if (value == null || value.isEmpty())
			return ResponseCode.invalidData;
		else if (userDetailsDAO.isMobileNoExist(userDetails.getMobileNo()))
			return ResponseCode.MobileNoExist;*/

		value = userDetails.getEmailId();
		if (value == null || value.isEmpty())
			return ResponseCode.invalidData;
		else if (userDetailsDAO.isEmailIdExists(value))
			return ResponseCode.EmailIDExist;
		value = userDetails.getUserName();
		if (TextUtils.isEmpty(value))
			userDetails.setUserName(userDetails.getEmailId());
		else if (userDetailsDAO.isUserNameExist(value))
			return ResponseCode.UserNameExist;
		return "0";
	}

	@Override
	public AuthorizationResult canAuthenticate(String userName, byte[] password, String deviceName, String deviceType,
			String macAddress, String product) {
		AuthorizationResult result = userDetailsDAO.canAuthenticate(userName, password);
		if (result.getResult() == AuthorizationCodes.Success)
			return userDetailsDAO.createSession(result.getUserId(), userName, deviceName, deviceType, macAddress,
					product);
		else
			return result;
	}

	/***
	 * Authorization & Session operations
	 */
	@Override
	public AuthorizationResult createSession(int userId, String macAddress, String product) {
		return userDetailsDAO.createSession(userId, "", "", "", macAddress, product);
	}

	@Override
	public boolean logout(String sessionId, String product) {
		if (userDetailsDAO.logout(sessionId)) {
			if (product.equalsIgnoreCase("huddil"))
				return userDetailsDAO.logoutHuddil(sessionId);
		}
		return false;
	}

	@Override
	public List<UserSession> getUserSessions() {
		return userDetailsDAO.getSessions();
	}

	@Override
	public boolean blockUser(String sessionId, int state, String user) {
		return userDetailsDAO.blockUser(sessionId, state, user);
	}

	@Override
	public List<State> stateList() {
		return userDetailsDAO.sateList();
	}

	@Override
	public int activateAccountThroughEmail(String emailId, String token, String product) {
		return userDetailsDAO.activateAccountThroughEmail(emailId, token, product);
	}

	@Override
	public boolean verifyMobileNumberWithOTP(String OTP, String mobileNo, String productName, String userType,
			String redirectUrl) throws MessagingException, IOException {
		return userDetailsDAO.verifyMobileNumberWithOTP(OTP, mobileNo, productName, userType, redirectUrl);
	}

	@Override
	public SyncDetails syncDetails(String sessionId) {
		return userDetailsDAO.syncDetails(sessionId);
	}

	@Override
	public int updateMobileNumber(String sessionId, String newMobileNumber, String otp) {
		/*if (userDetailsDAO.isMobileNoExist(newMobileNumber))
			return -2;*/
		int result = userDetailsDAO.updateMobileNumber(sessionId, newMobileNumber, otp);
		if (result == 1)
			result = userDetailsDAO.updateHuddilMobileNumber(newMobileNumber, sessionId, "");
		return result;
	}

	@Override
	public int getUserStatus(String data) {
		return userDetailsDAO.getUserStatus(data);
	}

	@Override
	public int mobileNumberVerificationWithOTP(String sessionId, String otp, String mobileNo) {
		if (userDetailsDAO.mobileNumberVerificationWithOTP(sessionId, otp, mobileNo) == 2) {
			userDetailsDAO.updateHuddilMobileNumber(mobileNo, sessionId, "");
			return 2;
		} else
			return 2;
	}
}
