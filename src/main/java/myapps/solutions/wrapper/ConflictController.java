package myapps.solutions.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import myapps.solutions.wrapper.service.IUserDetailsService;
import myapps.solutions.wrapper.utils.ResponseCode;

@RestController
public class ConflictController {

	@Autowired
	private IUserDetailsService usrDtsService;

	@ApiOperation(value = "Check user name exists", notes = "To check if user name exists")
	@ApiResponses(value = { @ApiResponse(code = 1011, message = "User name exist"),
			@ApiResponse(code = 1012, message = "User name does not exist"),
			@ApiResponse(code = 9999, message = "Empty user name") })
	@RequestMapping(value = "/usrExist/", method = RequestMethod.GET)
	public ResponseEntity<Void> isUserNameExist(@RequestParam String userName) {
		HttpHeaders headers = new HttpHeaders();
		if (userName == null || userName.isEmpty())
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (usrDtsService.isUserNameExist(userName))
				headers.set("ResponseCode", ResponseCode.UserNameExist);
			else
				headers.set("ResponseCode", ResponseCode.UserNameDoesNotExist);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Check mobile number exists", notes = "To check if mobile number exists")
	@ApiResponses(value = { @ApiResponse(code = 1031, message = "Mobile number exist"),
			@ApiResponse(code = 1032, message = "Mobile number does not exist"),
			@ApiResponse(code = 9999, message = "Empty/ Invalid mobile number") })
	@RequestMapping(value = "/mobNoExist/", method = RequestMethod.GET)
	public ResponseEntity<Void> isMobileNoExist(@RequestParam String mobileNo) {
		HttpHeaders headers = new HttpHeaders();
		mobileNo = mobileNo.trim();
		if (mobileNo == null || mobileNo.isEmpty() || !mobileNo.matches("\\+91[0-9]{10}"))
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (usrDtsService.isMobileNoExist(mobileNo))
				headers.set("ResponseCode", ResponseCode.MobileNoExist);
			else
				headers.set("ResponseCode", ResponseCode.MobileNoDoesNotExist);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Check email ID exists", notes = "To check if email ID exists")
	@ApiResponses(value = { @ApiResponse(code = 1021, message = "Email ID exist"),
			@ApiResponse(code = 1022, message = "Email ID does not exist"),
			@ApiResponse(code = 9999, message = "Empty/ Invalid Email ID") })
	@RequestMapping(value = "/emailExist/", method = RequestMethod.GET)
	public ResponseEntity<Void> isEmailIdExist(@RequestParam String emailId) {
		HttpHeaders headers = new HttpHeaders();
		if (emailId == null || emailId.isEmpty() || !emailId
				.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (usrDtsService.isEmailIdExists(emailId))
				headers.set("ResponseCode", ResponseCode.EmailIDExist);
			else
				headers.set("ResponseCode", ResponseCode.EmailIdDoesNotExist);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}
}
