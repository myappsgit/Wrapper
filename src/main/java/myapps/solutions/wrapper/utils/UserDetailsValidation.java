package myapps.solutions.wrapper.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.http.util.TextUtils;

import myapps.solutions.wrapper.model.UserDetails;
import myapps.solutions.wrapper.utils.UserDetailsValidation.ValidateUserDetails;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateUserDetails.class)
public @interface UserDetailsValidation {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public static class ValidateUserDetails implements ConstraintValidator<UserDetailsValidation, Object> {

		@Override
		public void initialize(UserDetailsValidation constraintAnnotation) {

		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			context.disableDefaultConstraintViolation();
			boolean isValid = true;
			String data = null;
			if (value == null)
				return false;
			if (value instanceof UserDetails) {
				UserDetails user = (UserDetails) value;

				data = user.getProduct();
				if (TextUtils.isEmpty(data) || data.equals("ProductNotValid")) {
					isValid = false;
					context.buildConstraintViolationWithTemplate("Product.NotValid").addPropertyNode("product")
							.addConstraintViolation();
				}

				if (isValid && data.equalsIgnoreCase("cashup"))
					return UserValidator.validateCashupUser(user, context);
				else if (isValid && data.equalsIgnoreCase("huddil"))
					return UserValidator.validateHuddilUser(user, context);
			}
			return isValid;
		}

	}

}
