package myapps.solutions.wrapper;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import myapps.solutions.wrapper.service.IPaymentService;
import myapps.solutions.wrapper.utils.ResponseCode;

@RestController
public class PaymentController {

	@Autowired
	private IPaymentService paymentService;

	@ApiOperation(value = "Create a payment request URL", notes = "To create payment request URL")
	@ApiResponses(value = { @ApiResponse(code = 2011, message = "Payment URL created successfully"),
			@ApiResponse(code = 9996, message = "Invalid product name"),
			@ApiResponse(code = 9997, message = "Your are not allowded to do this action"),
			@ApiResponse(code = 9998, message = "Invalid session") })
	//@RequestMapping(value = "/payment/", method = RequestMethod.GET)
	public ResponseEntity<String> payment(@RequestParam String sessionId, @RequestParam String product,
			@RequestParam double amount, @RequestParam String redirectUrl) throws ClientProtocolException, IOException {
		String url = paymentService.createPaymentRequest(sessionId, product, amount, redirectUrl);
		HttpHeaders headers = new HttpHeaders();
		if (url == null)
			headers.set("ResponseCode", ResponseCode.invalidProductName);
		else if (url.equals("-2"))
			headers.set("ResponseCode", ResponseCode.accessRestricted);
		else if (url.equals("-1"))
			headers.set("ResponseCode", ResponseCode.invalidSession);
		else
			headers.set("ResponseCode", ResponseCode.CreatePaymnetRequestSuccessfull);
		return new ResponseEntity<String>(url, headers, HttpStatus.OK);
	}

	@ApiOperation(value = "To update the payment details with myApps", notes = "To udpate the pyamnet details")
	@ApiResponses(value = { @ApiResponse(code = 2021, message = "Payment successful"),
			@ApiResponse(code = 2022, message = "Payment failed"),
			@ApiResponse(code = 2023, message = "Payment details already exist"),
			@ApiResponse(code = 2024, message = "Paymet update failed"),
			@ApiResponse(code = 9996, message = "Invalid product name"),
			@ApiResponse(code = 9997, message = "Your are not allowded to do this action"),
			@ApiResponse(code = 9998, message = "Invalid session") })
	//@RequestMapping(value = "/payment/", method = RequestMethod.POST)
	public ResponseEntity<Void> payment(@RequestParam String paymentId, @RequestParam String productName,
			@RequestParam String sessionId) throws URISyntaxException, IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("ResponseCode", paymentService.addPaymentDetails(paymentId, productName, sessionId));
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
}
