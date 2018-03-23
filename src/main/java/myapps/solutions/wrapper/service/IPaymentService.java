package myapps.solutions.wrapper.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public interface IPaymentService {

	String createPaymentRequest(String sessionId, String product, double amount, String redirectUrl)
			throws ClientProtocolException, IOException;
	String addPaymentDetails(String paymentId, String productName, String sessionId) throws URISyntaxException, IOException;
}
