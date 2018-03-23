package myapps.solutions.wrapper;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import myapps.notification.service.EmailService;
import myapps.solutions.wrapper.model.PasswordChange;
import myapps.solutions.wrapper.model.UserType;
import myapps.solutions.wrapper.service.IPasswordService;
import myapps.solutions.wrapper.utils.ResponseCode;

@Controller
public class PasswordController {

	@Autowired
	IPasswordService passwordService;

	EmailService service;

	/* SEND EMAIL LINK FOR CHANGING PASSWORD WITHOUT LOGIN */
	@ApiOperation(value = "Request reset password with Email", notes = "To request reset password without login using email ID")
	@ApiResponses(value = { @ApiResponse(code = 1911, message = "Email sent successful"),
			@ApiResponse(code = 1912, message = "Email sent failure"),
			@ApiResponse(code = 9999, message = "Invalid/ Empty email ID") })
	@RequestMapping(value = "/sendPasswordResetEmailWithoutLogin/", method = RequestMethod.GET)
	public ResponseEntity<Void> sendPasswordResetLink(@RequestParam String emailId, @RequestParam String redirectUrl, @RequestParam String productName)
			throws IOException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		if (emailId == null || emailId.isEmpty() || !emailId
				.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (passwordService.sendPasswordRestLink(emailId, redirectUrl, productName))
				headers.set("ResponseCode", ResponseCode.EmailSendSuccessfully);
			else
				headers.set("ResponseCode", ResponseCode.EmailIdDoesNotExist);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	/* RESET PASSWORD WITH EMAIL LINK WITHOUT LOGIN */
	// @RequestMapping(value = "/resetPasswordWithEmailLink/", method =
	// RequestMethod.GET)
	public String changePasswordWithResetLink(@RequestParam String emailId, @RequestParam String token) {
		return "passwordreset";
	}

	/* SEND OTP FOR CHANGING PASSWORD WITHOUT LOGIN */
	@ApiOperation(value = "Request reset password with mobile OTP", notes = "To reuest reset password wihtout login using mobile number OTP")
	@ApiResponses(value = { @ApiResponse(code = 1901, message = "OTP sent successful"),
			@ApiResponse(code = 1032, message = "Mobile number not found"),
			@ApiResponse(code = 9999, message = "Invalid mobile number") })
	@RequestMapping(value = "/sendPasswordResetOTPWithoutLogin", method = RequestMethod.GET)
	public ResponseEntity<Void> sendPasswordRestOTP(@RequestParam String sessionId, @RequestParam String mobileNo, @RequestParam String product) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		if (mobileNo == null || mobileNo.isEmpty() || !mobileNo.matches("\\+91[0-9]{10}"))
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			int response = passwordService.sendPasswordRestOTP(sessionId, mobileNo, product);
			if(response == 1)
				headers.set("ResponseCode", ResponseCode.OTPSendSuccessfully);
			else if(response == 2)
				headers.set("ResponseCode", ResponseCode.MobileNoRegisteredWithAnotherUser);
			else
				headers.set("ResponseCode", ResponseCode.OTPSendFailure);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	/* RESET PASSWORD WITH OTP WITHOUT LOGIN */
	@ApiOperation(value = "Reset password with OTP", notes = "To reset password with OTP sent to registered mobile number")
	@ApiResponses(value = { @ApiResponse(code = 1921, message = "Password change successful"),
			@ApiResponse(code = 1922, message = "Password change failure"),
			@ApiResponse(code = 1923, message = "Password & Confirm password does not match"),
			@ApiResponse(code = 1924, message = "Password cannot be empty") })
	@RequestMapping(value = "/resetPasswordWithOTP/{OTP}", method = RequestMethod.PUT)
	public ResponseEntity<Void> changePasswordWithResetOTP(@RequestBody PasswordChange passwordChange,
			@PathVariable("OTP") String OTP) {
		HttpHeaders headers = new HttpHeaders();
		int result = passwordService.changePasswordWithResetOTP(passwordChange, OTP);
		if (result == 0)
			headers.set("ResponseCode", ResponseCode.PasswordChangeSuccessful);
		else if (result == 1)
			headers.set("ResponseCode", ResponseCode.PasswordCannotBeNull);
		else if (result == 2)
			headers.set("ResponseCode", ResponseCode.PasswordsDoNotMatch);
		else
			headers.set("ResponseCode", ResponseCode.PasswordChangeFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	/* CHANGE PASSWORD AFTER LOGIN */
	@ApiOperation(value = "Change password", notes = "To change password after login")
	@ApiResponses(value = { @ApiResponse(code = 1921, message = "Password change successful"),
			@ApiResponse(code = 1922, message = "Password change failure"),
			@ApiResponse(code = 1923, message = "Password & Confirm password does not match"),
			@ApiResponse(code = 1924, message = "Password cannot be empty"),
			@ApiResponse(code = 1925, message = "Old password does not match") })
	@RequestMapping(value = "/changePassword/{sessionId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> changePasswordwithEmail(@PathVariable("sessionId") String sessionId,
			@RequestBody PasswordChange passwordChange) {
		HttpHeaders headers = new HttpHeaders();
		int result = passwordService.changePassword(passwordChange, sessionId);
		if (result == 0)
			headers.set("ResponseCode", ResponseCode.PasswordChangeSuccessful);
		else if (result == 1)
			headers.set("ResponseCode", ResponseCode.PasswordCannotBeNull);
		else if (result == 2)
			headers.set("ResponseCode", ResponseCode.PasswordsDoNotMatch);
		else if (result == 3)
			headers.set("ResponseCode", ResponseCode.OldPasswordMatchFailed);
		else
			headers.set("ResponseCode", ResponseCode.PasswordChangeFailure);

		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	/* RESEND EMAIL ACTIVATION LINK */
	@ApiOperation(value = "Resend activation mail", notes = "To resend email ID verification link to mail")
	@ApiResponses(value = { @ApiResponse(code = 1931, message = "Email sent successful"),
			@ApiResponse(code = 1022, message = "Email ID does not exist"),
			@ApiResponse(code = 9999, message = "Invalid email ID") })
	@RequestMapping(value = "/resendActivationEmail/", method = RequestMethod.GET)
	public ResponseEntity<Void> resendActivationLink(@RequestParam String emailId, @RequestParam String userType,
			@RequestParam String productName, @RequestParam String redirectUrl) throws IOException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		UserType u = new UserType(userType);
		if (emailId == null || emailId.isEmpty() || !emailId
				.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (passwordService.resendActivationLink(emailId, u, productName, redirectUrl))
				headers.set("ResponseCode", ResponseCode.ActivationLinkSendSuccessful);
			else
				headers.set("ResponseCode", ResponseCode.EmailIdDoesNotExist);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	/* RESEND ACTIVATION OTP */
	@ApiOperation(value = "Resend activation OTP", notes = "To resend mobile number activation OTP to mobile number")
	@ApiResponses(value = { @ApiResponse(code = 1951, message = "OTP sent successful"),
			@ApiResponse(code = 1032, message = "Mobile number does not exist"),
			@ApiResponse(code = 9999, message = "Invalid mobile number") })
	@RequestMapping(value = "/resendActivationOTP/{mobileNo}", method = RequestMethod.GET)
	public ResponseEntity<Void> sendActivationOTP(@PathVariable("mobileNo") String mobileNo, @RequestParam String product) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		if (mobileNo == null || mobileNo.isEmpty() || !mobileNo.matches("\\+91[0-9]{10}"))
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (passwordService.resendActivationOTP(mobileNo, product))
				headers.set("ResponseCode", ResponseCode.ActivationOTPSendSuccessful);
			else
				headers.set("ResponseCode", ResponseCode.MobileNoDoesNotExist);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Reset password", notes = "To reset password with email ID and token")
	@ApiResponses(value = { @ApiResponse(code = 1921, message = "Password change successful"),
			@ApiResponse(code = 1922, message = "Password change failure"),
			@ApiResponse(code = 1923, message = "Password and confirm password does not match"),
			@ApiResponse(code = 1924, message = "Password cannot be emtpy") })
	@RequestMapping(value = "/resetPassword/{emailId}/{token}/", method = RequestMethod.POST)
	public ResponseEntity<Void> resetPassword(@RequestBody PasswordChange passwordChange, @PathVariable String emailId,
			@PathVariable String token) {
		HttpHeaders headers = new HttpHeaders();
		int result = passwordService.resetPassword(emailId, token, passwordChange);
		if (result == 0)
			headers.set("ResponseCode", ResponseCode.PasswordChangeSuccessful);
		else if (result == 1)
			headers.set("ResponseCode", ResponseCode.PasswordCannotBeNull);
		else if (result == 2)
			headers.set("ResponseCode", ResponseCode.PasswordsDoNotMatch);
		else
			headers.set("ResponseCode", ResponseCode.PasswordChangeFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);

	}

}
