package myapps.solutions.wrapper.utils;

import javax.validation.ConstraintValidatorContext;

import org.apache.http.util.TextUtils;

import myapps.solutions.wrapper.model.UserDetails;

public class UserValidator {

	public static boolean validateCashupUser(UserDetails user, ConstraintValidatorContext context) {
		boolean isValid = validateUser(user, context);
		String data = user.getUserName().trim();
		if (TextUtils.isEmpty(data)) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.userName").addPropertyNode("userName")
					.addConstraintViolation();
		} else {
			if (data.length() < RegExs.minUserLen || data.length() > RegExs.maxUserLen) {
				isValid = false;
				context.buildConstraintViolationWithTemplate("Size.userDetails.userName").addPropertyNode("userName")
						.addConstraintViolation();
			} else {
				if (!data.matches(RegExs.userName)) {
					isValid = false;
					context.buildConstraintViolationWithTemplate("Pattern.userDetails.userName")
							.addPropertyNode("userName").addConstraintViolation();
				}
			}
		}

		data = user.getGender().trim();
		if (TextUtils.isEmpty(data)) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.gender").addPropertyNode("gender")
					.addConstraintViolation();

		} else if (!(data.equalsIgnoreCase("m") || data.equalsIgnoreCase("f") || data.equalsIgnoreCase("o"))) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("Value.userDetails.gender").addPropertyNode("gender")
					.addConstraintViolation();
		}

		isValid = validateField(user.getCity(), context, "city");

		if (user.getAge() == 0) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.age").addPropertyNode("age")
					.addConstraintViolation();
		} else if (user.getAge() < RegExs.minAge) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("Value.userDetails.age").addPropertyNode("age")
					.addConstraintViolation();
		}
		return isValid;
	}

	public static boolean validateHuddilUser(UserDetails user, ConstraintValidatorContext context) {
		boolean isValid = validateUser(user, context);
		return isValid && validateField(user.getUserName(), context, "userName");
	}

	private static boolean validateUser(UserDetails user, ConstraintValidatorContext context) {
		boolean isValid = true;
		String data = user.getEmailId().trim();
		if (TextUtils.isEmpty(data)) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.emailId").addPropertyNode("emailId")
					.addConstraintViolation();
		} else if (!data.matches(RegExs.emailId)) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("Pattern.userDetails.emailId").addPropertyNode("emailId")
					.addConstraintViolation();
		}

		/*data = user.getMobileNo().trim();
		if (TextUtils.isEmpty(data)) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.mobileNo").addPropertyNode("mobileNo")
					.addConstraintViolation();
		} else if (!user.getMobileNo().matches(RegExs.indianMobileNo)) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("Pattern.userDetails.mobileNo").addPropertyNode("mobileNo")
					.addConstraintViolation();
		}*/

		if (!user.isFromGoogle()) {
			if (user.getAuthorization() == null) {
				isValid = false;
				context.buildConstraintViolationWithTemplate("NotNull.authorization.userDetails")
						.addPropertyNode("authorization").addConstraintViolation();
			} else if (user.getAuthorization().getPassword() == null
					|| user.getAuthorization().getPassword().length == 0) {
				isValid = false;
				context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.password")
						.addPropertyNode("password").addConstraintViolation();
			} else if (user.getAuthorization().getSalt() == null || user.getAuthorization().getSalt().length == 0) {
				isValid = false;
				context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.password")
						.addPropertyNode("password").addConstraintViolation();
			}
		}
		if (user.getTermsConditionsHistories() == null || user.getTermsConditionsHistories().isEmpty()) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.termsConditionsHistories")
					.addPropertyNode("termsConditionsHistories").addConstraintViolation();
		} else if (user.getTermsConditionsHistories().get(0).getTermsConditions() == null) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.termsConditionsHistories")
					.addPropertyNode("termsConditionsHistories").addConstraintViolation();
		} else if (user.getTermsConditionsHistories().get(0).getTermsConditions().getDescription() != null
				&& user.getTermsConditionsHistories().get(0).getTermsConditions().getDescription().equals("NotValid")) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotValid.userDetails.termsConditionsHistories")
					.addPropertyNode("termsConditionsHistories").addConstraintViolation();
		}

		data = user.getAddressingName().trim();
		if (TextUtils.isEmpty(data)) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails.addressingName")
					.addPropertyNode("addressingName").addConstraintViolation();
		} else if (data.length() < RegExs.minAddressingLen || data.length() > RegExs.maxAddressingLen) {
			isValid = false;
			context.buildConstraintViolationWithTemplate("Size.userDetails.addressingName")
					.addPropertyNode("addressingName").addConstraintViolation();
		}

		if (user.getUserSubscriptions().get(0).getUserType().getType().equalsIgnoreCase("service provider")) {
			isValid = validateField(user.getAddress(), context, "address")
					&& validateField(user.getCity(), context, "city")
					&& validateField(user.getPincode(), context, "pincode")
					&& validateField(user.getCountry(), context, "country")
					&& validateField(user.getWebsite(), context, "website") && isValid;

		}
		return isValid;
	}

	private static boolean validateField(String data, ConstraintValidatorContext context, String field) {
		if (TextUtils.isEmpty(data) || TextUtils.isBlank(data)) {
			context.buildConstraintViolationWithTemplate("NotEmpty.userDetails." + field).addPropertyNode(field)
					.addConstraintViolation();
			return false;
		} else if (field.equals("pincode") && !data.matches(RegExs.pincode)) {
			context.buildConstraintViolationWithTemplate("Pattern.userDetails.pincode").addPropertyNode(field)
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
