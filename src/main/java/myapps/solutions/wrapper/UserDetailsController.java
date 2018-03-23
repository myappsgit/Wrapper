package myapps.solutions.wrapper;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import myapps.solutions.wrapper.model.AccountDevice;
import myapps.solutions.wrapper.model.Authorization;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.model.State;
import myapps.solutions.wrapper.model.SyncDetails;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.model.UserSession;
import myapps.solutions.wrapper.service.IUserDetailsService;
import myapps.solutions.wrapper.utils.RegExs;
import myapps.solutions.wrapper.utils.ResponseCode;

@RestController
public class UserDetailsController {

	@Autowired
	IUserDetailsService userDetailsService;

	@ApiOperation(value = "Create a new user", notes = "To create one new user in the wrapper")
	@ApiResponses(value = { @ApiResponse(code = 1111, message = "Create user successful"),
			@ApiResponse(code = 1112, message = "Create user failure"),
			@ApiResponse(code = 1011, message = "User name exist"),
			@ApiResponse(code = 1021, message = "Email ID exists"),
			@ApiResponse(code = 1031, message = "Mobile No exists"),
			@ApiResponse(code = 9999, message = "Invalid/ empty data passed") })
	@RequestMapping(value = "/usrDts/", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody UserDetails userDetails, UriComponentsBuilder ucBuilder,
			@RequestParam String redirectUrl) throws IOException, MessagingException {
		String responseCode = userDetailsService.checkForExistingUserDetails(userDetails);
		HttpHeaders headers = new HttpHeaders();
		if (!responseCode.equals("0")) {
			headers.set("ResponseCode", responseCode);
			headers.set("status", Integer.toString(userDetailsService.getUserStatus(userDetails.getEmailId())));
		} else {
			userDetailsService.add(userDetails, redirectUrl);
			headers.set("ResponseCode", ResponseCode.CreateUserSuccessful);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Fetch user details", notes = "To get user details from the wrapper")
	@ApiResponses(value = { @ApiResponse(code = 1211, message = "Read user successful"),
			@ApiResponse(code = 1212, message = "Read user failure") })
	@RequestMapping(value = "/usrDts/{sessionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDetails> getUser(@PathVariable("sessionId") String sessionId) {
		UserDetails user = userDetailsService.get(sessionId);
		HttpHeaders headers = new HttpHeaders();
		if (user == null)
			headers.set("ResponseCode", ResponseCode.ReadUserFailure);
		else
			headers.set("ResponseCode", ResponseCode.ReadUserSuccessful);
		return new ResponseEntity<UserDetails>(user, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Authorize a existing user", notes = "To authorize and create session for a user in the wrapper")
	@ApiResponses(value = {
			@ApiResponse(code = 1511, message = "Autherization successful and a session is established"),
			@ApiResponse(code = 1512, message = "Device limit has reached"),
			@ApiResponse(code = 1513, message = "Autherization failure"),
			@ApiResponse(code = 1514, message = "Session cannot be created"),
			@ApiResponse(code = 1515, message = "Account is not yet activated"),
			@ApiResponse(code = 1943, message = "Email Id not ")
			})
	@RequestMapping(value = "/auth/", method = RequestMethod.POST)
	public ResponseEntity<SyncDetails> authenticateUser(
			@RequestBody Authorization auth /*
											 * @RequestBody Authorization auth,
											 * 
											 * @RequestHeader String
											 * macAddress, @RequestHeader String
											 * deviceType, @RequestHeader String
											 * deviceName
											 */) {
		AuthorizationResult result = userDetailsService.canAuthenticate(new String(auth.getSalt()), auth.getPassword(),
				"", "", "", auth.getProduct());
		/*
		 * AuthorizationResult result =
		 * userDetailsService.canAuthenticate(userName, auth.getPassword(),
		 * deviceName, deviceType, macAddress);
		 */
		HttpHeaders headers = new HttpHeaders();
		AuthorizationCodes status = result.getResult();
		if (status == AuthorizationCodes.Success) {
			headers.set("sessionId", result.getSessionId());
			headers.set("userType", result.getUserType());
			headers.set("ResponseCode", ResponseCode.AuthUserSuccessful);
		} else if (status == AuthorizationCodes.Fail)
			headers.set("ResponseCode", ResponseCode.AuthUserFailure);
		else if (status == AuthorizationCodes.DeviceLimitReached)
			headers.set("ResponseCode", ResponseCode.AuthDeviceLimitReached);
		else if (status == AuthorizationCodes.SessionExist)
			headers.set("ResponseCode", ResponseCode.AuthSessionExist);
		/*else if (status == AuthorizationCodes.MobileNumberNotVerified)
			headers.set("ResponseCode", ResponseCode.EmailNotVerified);*/
		else if (status == AuthorizationCodes.EmailIdNotVerified)
			headers.set("ResponseCode", ResponseCode.EmailIdNotVerified);
		else if (status == AuthorizationCodes.AccountDeActivated)
			headers.set("ResponseCode", ResponseCode.AccountDeActivated);
		else if (status == AuthorizationCodes.NotFound)
			headers.set("ResponseCode", ResponseCode.AuthUserFailure);
		return new ResponseEntity<SyncDetails>(result.getSyncDetails(), headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Update a existing user", notes = "To update a user details in the wrapper")
	@ApiResponses(value = { @ApiResponse(code = 1311, message = "Update user successful"),
			@ApiResponse(code = 1312, message = "Update user failure") })
	@RequestMapping(value = "/usrDts/{sessionId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateUser(@PathVariable String sessionId, @RequestBody UserDetails userDetails) {
		HttpHeaders headers = new HttpHeaders();
		if (userDetailsService.update(sessionId, userDetails))
			headers.set("ResponseCode", ResponseCode.UpdateUserSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.UpdateUserFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Logout of the wrapper", notes = "To logout of the application")
	@ApiResponses(value = { @ApiResponse(code = 1611, message = "User logout successful"),
			@ApiResponse(code = 1612, message = "User logout failure") })
	@RequestMapping(value = "/logout/", method = RequestMethod.PUT)
	public ResponseEntity<Void> logout(@RequestParam String sessionId, @RequestParam String product) {
		HttpHeaders headers = new HttpHeaders();
		if (userDetailsService.logout(sessionId, product))
			headers.set("ResponseCode", ResponseCode.LogoutUserSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.LogoutUserFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Read devices related to an account", notes = "To read all the devices linked to an user account")
	@ApiResponses(value = { @ApiResponse(code = 1231, message = "Read devices successful"),
			@ApiResponse(code = 1232, message = "Read devices failure") })
	@RequestMapping(value = "/getAccDev/{sessionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AccountDevice>> getAccDevices(@PathVariable("sessionId") String sessionId) {
		List<AccountDevice> accountDevices = userDetailsService.getAccDevices(sessionId);
		HttpHeaders headers = new HttpHeaders();
		if (accountDevices.isEmpty())
			headers.set("ResponseCode", ResponseCode.ReadDeviceFailure);
		else
			headers.set("ResponseCode", ResponseCode.ReadDeviceSuccesful);
		return new ResponseEntity<List<AccountDevice>>(accountDevices, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Activate/ Deactivate an account", notes = "To activate/ deactivate a user account")
	@ApiResponses(value = { @ApiResponse(code = 1311, message = "Activation/ Deactivation successful"),
			@ApiResponse(code = 1312, message = "Activation/ Deactivation failure") })
	@RequestMapping(value = "/activateAcc/", method = RequestMethod.PUT)
	public ResponseEntity<Void> deactivateAcc(@RequestParam String sessionId, @RequestParam boolean state) {
		HttpHeaders headers = new HttpHeaders();
		if (userDetailsService.activateAcc(sessionId, state))
			headers.set("ResponseCode", ResponseCode.UpdateUserSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.UpdateUserFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	// @RequestMapping(value = "/delAcc/{sessionId}", method =
	// RequestMethod.DELETE)
	public ResponseEntity<Void> deleteAcc(@PathVariable("sessionId") String sessionId) {
		HttpHeaders headers = new HttpHeaders();
		if (userDetailsService.delete(sessionId))
			headers.set("ResponseCode", ResponseCode.DeleteUserSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.DeleteUserFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete Account", notes = "To delete an account")
	@ApiResponses(value = { @ApiResponse(code = 1411, message = "Delete Account Successful"),
			@ApiResponse(code = 1412, message = "Delete Account Failure") })
	@RequestMapping(value = "/deleteUserAccountThroughMail/{emailId}/{token}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUserAccount(@PathVariable("emailId") String emailId,
			@PathVariable("token") String token) {
		HttpHeaders headers = new HttpHeaders();
		if (userDetailsService.deleteUserAccountThroughEmail(emailId, token))
			headers.set("ResponseCode", ResponseCode.DeleteAccountSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.DeleteAccountFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	// @RequestMapping(value = "/getSes/", method = RequestMethod.GET, produces
	// = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserSession>> getSession() {
		return new ResponseEntity<List<UserSession>>(userDetailsService.getUserSessions(), HttpStatus.OK);
	}

	@ApiOperation(value = "Block a user", notes = "To block a particular user from a wrapper")
	@ApiResponses(value = { @ApiResponse(code = 1811, message = "Block user successful"),
			@ApiResponse(code = 1822, message = "Block user failure"),
			@ApiResponse(code = 9999, message = "Invalid state passed") })
	@RequestMapping(value = "/blockUser/{sessionId}/{state}/", method = RequestMethod.PUT)
	public ResponseEntity<Void> blockUser(@PathVariable("sessionId") String sessionId, @PathVariable("state") int state,
			@RequestParam String user) {
		HttpHeaders headers = new HttpHeaders();
		if (state != 0 && state != 1)
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (userDetailsService.blockUser(sessionId, state, user))
				headers.set("ResponseCode", ResponseCode.BlockUserSuccessful);
			else
				headers.set("ResponseCode", ResponseCode.BlockUserFailure);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);

	}

	@ApiOperation(value = "Read state list", notes = "To get a state list")
	@RequestMapping(value = "/stateList/", method = RequestMethod.GET)
	public ResponseEntity<List<State>> stateList() {
		return new ResponseEntity<List<State>>(userDetailsService.stateList(), HttpStatus.OK);
	}

	@ApiOperation(value = "Verify email", notes = "To activate/ verify an account using email")
	@ApiResponses(value = { @ApiResponse(code = 1941, message = "Account activation/ verification successful"),
			@ApiResponse(code = 1942, message = "Account activation/ verification failure"),
			@ApiResponse(code = 9999, message = "Invalid email Id/ token") })
	@RequestMapping(value = "/accountActivationThroughEmail/", method = RequestMethod.PUT)
	public ResponseEntity<Void> activateAccountThroughEmail(@RequestParam String emailId, @RequestParam String token,
			@RequestParam String product) {
		HttpHeaders headers = new HttpHeaders();
		if (TextUtils.isEmpty(emailId) || !emailId.matches(RegExs.emailId) || TextUtils.isEmpty(token)
				|| TextUtils.isEmpty(product))
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			int value = userDetailsService.activateAccountThroughEmail(emailId, token, product);
			if (value == 1)
				headers.set("ResponseCode", ResponseCode.AccountActivationSuccessful);
			else if (value == 2)
				headers.set("ResponseCode", ResponseCode.AccountAlreadyActivated);
			else if (value == 3)
				headers.set("ResponseCode", ResponseCode.AccountDeActivated);
			else
				headers.set("ResponseCode", ResponseCode.AccountActivationFailure);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Verify mobile number", notes = "To verify a mobile number")
	@ApiResponses(value = { @ApiResponse(code = 1941, message = "Mobile verification successful"),
			@ApiResponse(code = 1942, message = "Mobile verification failure"),
			@ApiResponse(code = 9999, message = "Empty OTP") })
	@RequestMapping(value = "/verifyMobileNumberWithOTP/", method = RequestMethod.GET)
	public ResponseEntity<Void> verifyMobileNumberWithOTP(@RequestParam String OTP, @RequestParam String mobileNo,
			@RequestParam String productName, @RequestParam String userType, @RequestParam String redirectUrl)
			throws MessagingException, IOException {
		HttpHeaders headers = new HttpHeaders();
		if (OTP == null || OTP.isEmpty())
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (userDetailsService.verifyMobileNumberWithOTP(OTP, mobileNo, productName, userType, redirectUrl))
				headers.set("ResponseCode", ResponseCode.AccountActivationSuccessful);
			else
				headers.set("ResponseCode", ResponseCode.AccountActivationFailure);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Sync user details", notes = "To sync user details/ check for any update")
	@ApiResponses(value = { @ApiResponse(code = 1961, message = "Sync read successful"),
			@ApiResponse(code = 1962, message = "Sync read failure") })
	@RequestMapping(value = "/syncDetails/{sessionId}", method = RequestMethod.GET)
	public ResponseEntity<SyncDetails> syncDetails(@PathVariable("sessionId") String sessionId) {
		HttpHeaders headers = new HttpHeaders();
		SyncDetails details = userDetailsService.syncDetails(sessionId);
		if (details != null)
			headers.set("ResponseCode", ResponseCode.SyncDetailsReadSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.SyncDetailsReadFailure);
		return new ResponseEntity<SyncDetails>(details, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Update Mobile Number", notes = "To update mobile number of user")
	@ApiResponses(value = { @ApiResponse(code = 9998, message = "Invalid Session"),
			@ApiResponse(code = 9997, message = "Access Restricted"),
			@ApiResponse(code = 1033, message = "Mobile Number Updated Successfully"),
			@ApiResponse(code = 1034, message = "Mobile Number Update Failure"), })
	@RequestMapping(value = "/updateMobileNumber", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateMobileNumber(@RequestParam String sessionId, @RequestParam String newMobileNumber,
			@RequestParam String otp) {
		HttpHeaders headers = new HttpHeaders();
		if (newMobileNumber == null || newMobileNumber.isEmpty() || !newMobileNumber.matches("\\+91[0-9]{10}"))
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			int user = userDetailsService.updateMobileNumber(sessionId, newMobileNumber, otp);
			if (user == -1)
				headers.set("ResponseCode", ResponseCode.invalidSession);
			else if (user == -2)
				headers.set("ResponseCode", ResponseCode.MobileNoExist);
			else if (user == -4)
				headers.set("ResponseCode", ResponseCode.WrongOTP);
			else if (user == 1)
				headers.set("ResponseCode", ResponseCode.UpdateMobileNumberSuccessful);
			else
				headers.set("ResponseCode", ResponseCode.UpdateMobileNumberFailure);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/mobileNumberVerification", method = RequestMethod.PUT)
	public ResponseEntity<Void> mobileNumberVerificationWithOTP(@RequestParam String sessionId,
			@RequestParam String otp, @RequestParam String mobileNo) {
		HttpHeaders headers = new HttpHeaders();
		int response = userDetailsService.mobileNumberVerificationWithOTP(sessionId, otp, mobileNo);
		if (response == -1)
			headers.set("ResponseCode", ResponseCode.invalidSession);
		else if (response == -2)
			headers.set("ResponseCode", ResponseCode.EmailIdNotVerified);
		else if (response == 1)
			headers.set("ResponseCode", ResponseCode.WrongOTP);
		else if (response == 2)
			headers.set("ResponseCode", ResponseCode.MobileNumberVerificationSuccessful);
		else if (response == 3)
			headers.set("ResponseCode", ResponseCode.MobileNumberAlreadyVerified);
		else
			headers.set("ResponseCode", ResponseCode.MobileNumberVerificationFailed);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);

	}

}
