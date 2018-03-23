package myapps.solutions.wrapper;

import java.util.Locale;
import java.util.Set;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import myapps.solutions.wrapper.utils.ResponseCode;
import myapps.solutions.wrapper.utils.ValidationErrorDTO;

@ControllerAdvice
public class ControllerValidationHandler {

	@Autowired
	private MessageSource msgSource;

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<?> processValidationError(ConstraintViolationException ex) {
		return ResponseEntity.ok().header("ResponseCode", ResponseCode.CreateUserFailure)
				.body(processFieldErrors(ex.getConstraintViolations()));
	}
	
	@ExceptionHandler(RollbackException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<?> processRollBackToValidationError(TransactionSystemException ex) {
		Throwable excep = ex.getCause();
		if(excep instanceof RollbackException){
			excep = excep.getCause();
			if(excep instanceof ConstraintViolationException){
				return ResponseEntity.ok().header("ResponseCode", ResponseCode.UpdateUserFailure)
						.body(processFieldErrors(((ConstraintViolationException) excep).getConstraintViolations()));
			}
		}
		return ResponseEntity.ok().header("ResponseCode", ResponseCode.UpdateUserFailure)
				.body(null);
	}

	private ValidationErrorDTO processFieldErrors(Set<ConstraintViolation<?>> violations) {
		ValidationErrorDTO dto = new ValidationErrorDTO();
		for (ConstraintViolation<?> violation : violations) {
			String localizedErrorMessage = resolveLocalizedErrorMessage(violation.getMessage());
			dto.addFieldError(violation.getPropertyPath().toString(), localizedErrorMessage);
		}
		return dto;
	}

	private String resolveLocalizedErrorMessage(String property) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		return msgSource.getMessage(property, null, currentLocale);
	}

}
