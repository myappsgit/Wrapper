package myapps.solutions.wrapper;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import myapps.solutions.wrapper.model.AuthorizationResult;
import myapps.solutions.wrapper.model.SyncDetails;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.model.AuthorizationResult.AuthorizationCodes;
import myapps.solutions.wrapper.service.IGoogleLogin;
import myapps.solutions.wrapper.service.IUserDetailsService;
import myapps.solutions.wrapper.utils.ResponseCode;

@RestController
public class GoogleLoginController {

	@Autowired
	private IGoogleLogin googleLogin;

	@Autowired
	private IUserDetailsService userDetailsService;

	@ApiOperation(value = "Get Google auth URL", notes = "To get Google auth URL")
	@RequestMapping(value = "/google/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getGoolge(UriComponentsBuilder ucBuilder) {
		String url = googleLogin.url();
		return new ResponseEntity<String>(url, HttpStatus.OK);
	}

	@ApiOperation(value = "Create a new user", notes = "To create a new user with Google access token")
	@ApiResponses(value = { @ApiResponse(code = 1111, message = "successful") })
	@RequestMapping(value = "/profileDetails/", method = RequestMethod.GET)
	public ResponseEntity<Void> getProfileDetails(@RequestHeader String accessToken)
			throws IOException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		String sessionId = googleLogin.getProfileDetails(accessToken);
		headers.set("ResponseCode", ResponseCode.CreateUserSuccessful);
		headers.set("sessionId", sessionId);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/oauth2callback", method = RequestMethod.GET)
	public ResponseEntity<Void> googleAuth(@RequestParam String code) {
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@ApiOperation(value = "Create a new user after Google authentication successful", notes = "To create one new user in the wrapper")
	@ApiResponses(value = { @ApiResponse(code = 1112, message = "Create user failure"),
			@ApiResponse(code = 1011, message = "User name exist"),
			@ApiResponse(code = 1021, message = "Email ID exists"),
			@ApiResponse(code = 1031, message = "Mobile No exists"),
			@ApiResponse(code = 1511, message = "Autherization successful and a session is established"),
			@ApiResponse(code = 1512, message = "Device limit has reached"),
			@ApiResponse(code = 1513, message = "Autherization failure"),
			@ApiResponse(code = 1514, message = "Session cannot be created"),
			@ApiResponse(code = 1515, message = "Account is not yet activated"),
			@ApiResponse(code = 9999, message = "Invalid/ empty data passed") })
	@RequestMapping(value = "/googleUser/", method = RequestMethod.POST)
	public ResponseEntity<SyncDetails> createUser(@RequestBody UserDetails userDetails,
			@RequestParam String accessToken, @RequestParam String redirectUrl)
			throws AddressException, IOException, MessagingException {
		HttpHeaders headers = new HttpHeaders();
		boolean createUser = false;
		String responseCode = userDetailsService.checkForExistingUserDetails(userDetails);
		if (!responseCode.equals(ResponseCode.EmailIDExist)) {
			if (!responseCode.equals("0")) {
				headers.set("ResponseCode", responseCode);
				headers.set("status", Integer.toString(userDetailsService.getUserStatus(userDetails.getEmailId())));
			} else {
				createUser = googleLogin.createUser(userDetails, accessToken, redirectUrl);
				if (createUser) {
					headers.set("ResponseCode", ResponseCode.CreateUserSuccessful);
				} else
					headers.set("ResponseCode", ResponseCode.CreateUserFailure);
			}
		} else
			createUser = true;
		if (createUser) {
			AuthorizationResult result = googleLogin.gAuthenticate(userDetails.getEmailId(), accessToken,
					userDetails.getProduct());
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
			else if (status == AuthorizationCodes.MobileNumberNotVerified)
				headers.set("ResponseCode", ResponseCode.EmailNotVerified);
			else if (status == AuthorizationCodes.EmailIdNotVerified)
				headers.set("ResponseCode", ResponseCode.EmailIdNotVerified);
			else if (status == AuthorizationCodes.AccountDeActivated)
				headers.set("ResponseCode", ResponseCode.AccountDeActivated);
			else if (status == AuthorizationCodes.NotFound)
				headers.set("ResponseCode", ResponseCode.AuthUserFailure);
			return new ResponseEntity<SyncDetails>(result.getSyncDetails(), headers, HttpStatus.OK);
		} else
			return new ResponseEntity<SyncDetails>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Authorize a existing user after Google signin", notes = "To authorize and create session for a user in the wrapper")
	@ApiResponses(value = {
			@ApiResponse(code = 1511, message = "Autherization successful and a session is established"),
			@ApiResponse(code = 1512, message = "Device limit has reached"),
			@ApiResponse(code = 1513, message = "Autherization failure"),
			@ApiResponse(code = 1514, message = "Session cannot be created"),
			@ApiResponse(code = 1515, message = "Account is not yet activated") })
	@RequestMapping(value = "/gAuth/", method = RequestMethod.POST)
	public ResponseEntity<SyncDetails> authenticateGUser(@RequestParam String emailId, @RequestParam String accessToken,
			@RequestParam String product) {
		HttpHeaders headers = new HttpHeaders();
		AuthorizationResult result = googleLogin.gAuthenticate(emailId, accessToken, product);
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
		else if (status == AuthorizationCodes.MobileNumberNotVerified)
			headers.set("ResponseCode", ResponseCode.EmailNotVerified);
		else if (status == AuthorizationCodes.EmailIdNotVerified)
			headers.set("ResponseCode", ResponseCode.EmailIdNotVerified);
		else if (status == AuthorizationCodes.AccountDeActivated)
			headers.set("ResponseCode", ResponseCode.AccountDeActivated);
		else if (status == AuthorizationCodes.NotFound)
			headers.set("ResponseCode", ResponseCode.AuthUserFailure);
		return new ResponseEntity<SyncDetails>(result.getSyncDetails(), headers, HttpStatus.OK);
	}
}
