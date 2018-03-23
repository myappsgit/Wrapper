package myapps.solutions.wrapper.utils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Misc {

    private static HashMap<String, String> sessionIds = new HashMap<String, String>();
    
	public static int calculateAge(Date dateOfBirth) {
		if (dateOfBirth == null)
			return 0;
		return Period.between(dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now())
				.getYears();
	}

	private static final String ALPHA_NUMERIC_STRING = "cHIJdeAh10HIJ10HI23bDEfgFrstGKwxyLMv1HIJ4rstuz@Cv1HIBv1HIv1HIJv1HIYv1HIZa5VW6X1012VW3mnop4789QVWRSTU67HIJ89ijklmnopq";

	public static String randomAlphaNumeric(String token) {

		token = token + ALPHA_NUMERIC_STRING;
		int count = 30;
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * token.length());
			builder.append(token.charAt(character));
		}

		return builder.toString();
	}

	public static String generateOTP() {

		String numbers = "0123456789";
		String OTP = "";

		Random rand = new SecureRandom();

		for (int i = 0; i < 6; i++) {
			OTP = OTP + numbers.charAt(rand.nextInt(numbers.length()));
		}
		return OTP;
	}

	public static String getSessionId(String userName) {
		String sessionId = sessionIds.get(userName);
		sessionIds.remove(userName);
		return sessionId;
	}

	public static void setSessionId(String userName, String sessionId) {
		sessionIds.put(userName, sessionId);
	}
}
