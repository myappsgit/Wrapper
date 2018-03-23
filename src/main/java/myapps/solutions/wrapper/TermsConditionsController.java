package myapps.solutions.wrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import myapps.solutions.wrapper.model.TermsConditions;
import myapps.solutions.wrapper.service.ITermsConditionsService;
import myapps.solutions.wrapper.utils.ResponseCode;

@RestController
public class TermsConditionsController {

	@Autowired
	ITermsConditionsService termsConditionsService;

	// insert the data in termsAndConditions
	@ApiOperation(value = "Create TCPS", notes = "To create a TCPS for a user type")
	@ApiResponses(value = { @ApiResponse(code = 1121, message = "TCPS creation successful"),
			@ApiResponse(code = 1122, message = "TCPS creation failure"),
			@ApiResponse(code = 9999, message = "Invalid state") })
	@RequestMapping(value = "/termsAndConditions/", method = RequestMethod.POST)
	public ResponseEntity<Void> insertTermsAndConditionsDetails(@RequestParam String sessionId,
			@RequestParam MultipartFile fileName, @RequestParam int userType, @RequestParam int productId, @RequestParam Date startDate, @RequestParam String title) throws FileNotFoundException, IOException {
		HttpHeaders headers = new HttpHeaders();
		if (termsConditionsService.insertTermsAndConditionsDetails(sessionId, fileName, userType, productId, startDate, title))
			headers.set("ResponseCode", ResponseCode.CreateTCPSSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.CreateTCPSFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	// Update the status in termsAndConditions based on sessionId,UserType
	@ApiOperation(value = "Enable/ Disable a TCPS", notes = "To enable/ disable a particual TCPS")
	@ApiResponses(value = { @ApiResponse(code = 1321, message = "Enabled/ Disabled a TCPS successful"),
			@ApiResponse(code = 1322, message = "Enabled/ Disabled a TCPS failure"),
			@ApiResponse(code = 9999, message = "Invalid state") })
	@RequestMapping(value = "/termsAndConditionsStatus/{sessionId}/{status}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateTermsAndConditions(@PathVariable("sessionId") String sessionId,
			@PathVariable("status") int status) {
		HttpHeaders headers = new HttpHeaders();
		if (status != 1 && status != 0)
			headers.set("ResponseCode", ResponseCode.invalidData);
		else {
			if (termsConditionsService.updateTermsAndConditions(status, sessionId))
				headers.set("ResponseCode", ResponseCode.UpdateTCPSSuccessful);
			else
				headers.set("ResponseCode", ResponseCode.UpdateTCPSFailure);
		}
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	// update the terms_conditions_id in termsAndConditionsHistory based on
	// userId
	@ApiOperation(value = "Update TCPS for a user", notes = "To update TCPS for a particular user")
	@ApiResponses(value = { @ApiResponse(code = 1321, message = "TCPS updation successful"),
			@ApiResponse(code = 1322, message = "TCPS updation failure") })
	@RequestMapping(value = "/termsAndConditionsHistory/{sessionId}/{terms_conditions_id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateTermsAndConditionsHistory(@PathVariable("sessionId") String sessionId,
			@PathVariable("terms_conditions_id") int terms_conditions_id) {
		HttpHeaders headers = new HttpHeaders();
		if (termsConditionsService.updateTermsAndConditionsHistory(terms_conditions_id, sessionId))
			headers.set("ResponseCode", ResponseCode.UpdateTCPSSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.UpdateTCPSFailure);
		return new ResponseEntity<Void>(headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Get TCPS", notes = "To get the latest TCPS for a particular user type")
	@ApiResponses(value = { @ApiResponse(code = 1221, message = "TCPS read successful"),
			@ApiResponse(code = 1222, message = "TCPS read failure"),
			@ApiResponse(code = 9999, message = "Invalid state") })
	@RequestMapping(value = "/termsAndConditionsHistory/", method = RequestMethod.GET)
	public ResponseEntity<TermsConditions> getTermsAndConditions(@RequestParam String userType,
			@RequestParam String product) {
		TermsConditions tcps = termsConditionsService.getTermsAndConditions(userType, product);
		HttpHeaders headers = new HttpHeaders();
		if (tcps != null)
			headers.set("ResponseCode", ResponseCode.ReadTCPSSuccessful);
		else
			headers.set("ResponseCode", ResponseCode.ReadTCPSFailure);
		return new ResponseEntity<TermsConditions>(tcps, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "Get TCPS", notes = "To get the list of TCPS for a particular product")
	@ApiResponses(value = { @ApiResponse(code = 1221, message = "TCPS read successful"),
			@ApiResponse(code = 1222, message = "TCPS read failure"),
			@ApiResponse(code = 9997, message = "User is not allowed to perform this action"),
			@ApiResponse(code = 9999, message = "Invalid/ session does not exist") })
	@RequestMapping(value = "/termsAndConditions/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TermsConditions>> getTCPS(@RequestParam String sessionId, @RequestParam String product) {
		HttpHeaders headers = new HttpHeaders();
		List<TermsConditions> conditions = termsConditionsService.getTCPS(sessionId, product);
		if (conditions.isEmpty())
			headers.add("ResponseCode", ResponseCode.ReadTCPSSuccessful);
		else if (conditions.get(0).getId() == -1)
			headers.add("ResponseCode", ResponseCode.invalidSession);
		else if (conditions.get(0).getId() == -2)
			headers.add("ResponseCode", ResponseCode.accessRestricted);
		else
			headers.add("ResponseCode", ResponseCode.ReadTCPSSuccessful);
		return new ResponseEntity<List<TermsConditions>>(conditions, headers, HttpStatus.OK);
	}
}
