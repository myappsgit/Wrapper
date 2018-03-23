package myapps.solutions.wrapper.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class Auth {

	public static int generateOTP() {
		Random generator = new Random();
		int randNum = generator.nextInt(999999);
		int OTP = generator.nextInt(randNum);
		return OTP;
	}

	public static byte[] generateSalt() {
		Random rand = new SecureRandom();
		byte[] salt = new byte[32];
		rand.nextBytes(salt);
		return salt;
	}

	public static byte[] generateHash(byte[] password, byte[] salt) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(salt);
			byte[] hash = digest.digest(password);
			return hash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isPasswordMatch(byte[] plain, byte[] expected, byte[] salt) {
		byte[] actual = generateHash(plain, salt);
		return Arrays.equals(actual, expected);
	}

	public static byte[] decrypt(byte[] encrypted) {
		return encrypted;
	}

	public static String getSessionId(int userId) {
		String id = UUID.randomUUID().toString() + userId;
		return encode(id);
	}

	private static String encode(String data) {
		return Base64.getEncoder().encodeToString(data.getBytes());
	}
}
