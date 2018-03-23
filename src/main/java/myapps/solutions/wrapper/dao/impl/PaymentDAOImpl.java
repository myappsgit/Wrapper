package myapps.solutions.wrapper.dao.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import myapps.payment.service.InstaMojoService;
import myapps.payment.service.model.InstaMojoPayment;
import myapps.solutions.wrapper.dao.IPaymentDAO;
import myapps.solutions.wrapper.model.Payment;
import myapps.solutions.wrapper.model.Product;
import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.model.UserPreferences;
import myapps.solutions.wrapper.utils.ResponseCode;

@Repository
public class PaymentDAOImpl implements IPaymentDAO {

	@PersistenceContext(unitName = "wrapper")
	private EntityManager wrapperEM;

	@PersistenceContext(unitName = "cashup")
	private EntityManager cashupEM;

	@Override
	public String createPaymentRequest(String sessionId, String productName, double amount, String redirectUrl)
			throws ClientProtocolException, IOException {
		Product product = getProduct(productName);
		if (product != null) {
			UserPreferences userPref = cashupEM.find(UserPreferences.class, sessionId);
			if (userPref == null)
				return "-1";
			else if (userPref.getUserType() != 1)
				return "-2";
			switch (product.getName().toLowerCase()) {
			case "cashup":
				UserDetails user = wrapperEM.find(UserDetails.class, userPref.getUserId());
				return InstaMojoService.getInstance().createPaymentRequest(user.getAddressingName(), user.getEmailId(),
						user.getMobileNo(), "Paying to LetzCashUp", amount, redirectUrl, productName);
			}
		}
		return null;
	}

	@Override
	@Transactional(value = "wrapperTranscationManager")
	public Payment addPaymentDetails(String paymentId, String productName, String sessionId)
			throws URISyntaxException, IOException {
		Object obj = wrapperEM.find(Payment.class, paymentId);
		if (obj != null)
			return new Payment(ResponseCode.PaymentDetailsExist);
		Product product = getProduct(productName);
		if (product != null) {
			UserPreferences userPref = cashupEM.find(UserPreferences.class, sessionId);
			if (userPref == null)
				return new Payment(ResponseCode.invalidSession);
			else if (userPref.getUserType() != 1)
				return new Payment(ResponseCode.accessRestricted);
			switch (product.getName().toLowerCase()) {
			case "cashup":
				InstaMojoPayment iPayment = InstaMojoService.getInstance().getPaymentResponse(paymentId, productName);
				if (iPayment == null)
					return new Payment(ResponseCode.PaymentIdNotValid);
				Payment payment = new Payment(product, userPref.getUserId(), iPayment);
				if (wrapperEM
						.createNativeQuery(
								"INSERT INTO `wrapper`.`payment`(`id`,`userId`,`productId`,`buyerName`,`method`,`amount`,`status`,`fees`,`paymentDate`) VALUES "
										+ "(:id, :userId, :productId, :buyerName, :method, :amount, :status, :fees, :paymentDate)")
						.setParameter("id", paymentId).setParameter("userId", userPref.getUserId())
						.setParameter("productId", product.getId()).setParameter("buyerName", iPayment.getBuyerName())
						.setParameter("method", iPayment.getMethod()).setParameter("amount", iPayment.getAmount())
						.setParameter("status", iPayment.getStatus()).setParameter("fees", iPayment.getFees())
						.setParameter("paymentDate", iPayment.getPaymentDate()).executeUpdate() == 1)
					// wrapperEM.persist(payment);
					payment.setResponseCode(ResponseCode.PaymentSuccessfull);
				else
					payment.setResponseCode(ResponseCode.PaymentUpdateFailed);
				return payment;
			}
		}
		return new Payment(ResponseCode.invalidProductName);
	}

	private Product getProduct(String name) {
		Object obj = wrapperEM.createNativeQuery("SELECT id, name FROM product WHERE name = :name", "products")
				.setParameter("name", name).getSingleResult();
		if (obj == null)
			return null;
		return (Product) obj;
	}

	@Override
	@Transactional(value = "cashupTranscationManager")
	public boolean updateCashUpPoints(String sessionId, int points) {
		return cashupEM.createQuery("UPDATE UserPreferences SET points = points - :amt WHERE sessionId = :sessionId")
				.setParameter("amt", points).setParameter("sessionId", sessionId).executeUpdate() == 1;
	}
}
