package myapps.solutions.wrapper.service.impl;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myapps.solutions.wrapper.dao.IPasswordDAO;
import myapps.solutions.wrapper.dao.IUserDetailsDAO;
import myapps.solutions.wrapper.model.PasswordChange;
import myapps.solutions.wrapper.model.UserType;
import myapps.solutions.wrapper.service.IPasswordService;

@Service
//@Transactional("transactionManager")
public class PasswordServiceImpl implements IPasswordService {

	@Autowired
	IUserDetailsDAO userDetailsDAO;

	@Autowired
	IPasswordDAO passwordDAO;

	@Override
	public boolean sendPasswordRestLink(String emailId, String url, String productName) throws IOException, MessagingException {
		if (!userDetailsDAO.isEmailIdExists(emailId))
			return false;
		else
			return passwordDAO.sendPasswordResetLink(emailId, url, productName);
	}

	@Override
	public int sendPasswordRestOTP(String sessionId, String mobileNo, String product) throws IOException {
		/*if (userDetailsDAO.isMobileNoExist(mobileNo))
			return 2;
		else*/
			return passwordDAO.sendPasswordResetOTP(sessionId, mobileNo, product);
	}

	@Override
	public int changePassword(PasswordChange passwordChange, String sessionId) {
		return passwordDAO.changePassword(passwordChange, sessionId);
	}

	@Override
    public boolean resendActivationLink(String emailId, UserType userType, String productName, String url) throws IOException, MessagingException {
        if (!userDetailsDAO.isEmailIdExists(emailId))
            return false;
        else
            return passwordDAO.resendActivationLink(emailId, userType, productName, url);
    }

	@Override
	public boolean resendActivationOTP(String mobileNo, String product) throws IOException {
		if (!userDetailsDAO.isMobileNoExist(mobileNo))
			return false;
		else
			return passwordDAO.resendActivationOTP(mobileNo, product);
	}

	@Override
	public int changePasswordWithResetOTP(PasswordChange passwordChange, String OTP) {
		return passwordDAO.changePassword(OTP, passwordChange);
	}

	@Override
	public int resetPassword(String emailId, String token, PasswordChange passwordChange) {
		return passwordDAO.resetPassword(emailId, token, passwordChange);
	}

}
