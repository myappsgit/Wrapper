package myapps.solutions.wrapper.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myapps.solutions.wrapper.dao.IPaymentDAO;
import myapps.solutions.wrapper.model.Payment;
import myapps.solutions.wrapper.service.IPaymentService;
import myapps.solutions.wrapper.utils.ResponseCode;

@Service
public class PaymentServiceImpl implements IPaymentService {

	@Autowired
	private IPaymentDAO paymentDao; 
	
	@Override
	public String createPaymentRequest(String sessionId, String product, double amount, String redirectUrl)
			throws ClientProtocolException, IOException {
		return paymentDao.createPaymentRequest(sessionId, product, amount, redirectUrl);
	}

	@Override
	public String addPaymentDetails(String paymentId, String productName, String sessionId)
			throws URISyntaxException, IOException {
		Payment payment = paymentDao.addPaymentDetails(paymentId, productName, sessionId);
		if(payment.getResponseCode().equals(ResponseCode.PaymentSuccessfull) && payment.getStatus().equalsIgnoreCase("credit")){
			if(paymentDao.updateCashUpPoints(sessionId, (int) payment.getAmount()))
				return ResponseCode.PaymentSuccessfull;
			else
				return ResponseCode.PaymentUpdateFailed;
		}
		return payment.getResponseCode();
	}

}
