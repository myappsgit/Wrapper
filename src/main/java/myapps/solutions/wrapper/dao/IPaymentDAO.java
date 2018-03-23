package myapps.solutions.wrapper.dao;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import myapps.solutions.wrapper.model.Payment;

public interface IPaymentDAO {

	String createPaymentRequest(String sessionId, String product, double amount, String redirectUrl)
			throws ClientProtocolException, IOException;
	Payment addPaymentDetails(String paymentId, String productName, String sessionId) throws URISyntaxException, IOException;
	boolean updateCashUpPoints(String sessionId, int points);
}
