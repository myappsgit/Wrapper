package myapps.solutions.wrapper.utils;

public interface ResponseCode {

	String invalidData = "9999";
	String invalidSession = "9998";
	String accessRestricted = "9997";
	String invalidProductName = "9996";
	
	String UserNameExist = "1011";
	String UserNameDoesNotExist = "1012";
	String EmailIDExist = "1021";
	String EmailIdDoesNotExist = "1022";
	String MobileNoExist = "1031";
	String MobileNoDoesNotExist = "1032";
	String UpdateMobileNumberSuccessful = "1033";
	String UpdateMobileNumberFailure = "1034";

	String CreateUserSuccessful = "1111";
	String CreateUserFailure = "1112";
	String CreateTCPSSuccessful = "1121";
	String CreateTCPSFailure = "1122";

	String ReadUserSuccessful = "1211";
	String ReadUserFailure = "1212";
	String ReadTCPSSuccessful = "1221";
	String ReadTCPSFailure = "1222";
	String ReadDeviceSuccesful = "1231";
	String ReadDeviceFailure = "1232";

	String UpdateUserSuccessful = "1311";
	String UpdateUserFailure = "1312";
	String UpdateTCPSSuccessful = "1321";
	String UpdateTCPSFailure = "1322";

	String DeleteUserSuccessful = "1411";
	String DeleteUserFailure = "1412";
	
	String DeleteAccountSuccessful = "1421";
	String DeleteAccountFailure = "1422";

	String AuthUserSuccessful = "1511";
	String AuthDeviceLimitReached = "1512";
	String AuthUserFailure = "1513";
	String AuthSessionExist = "1514";
	String AccountDeActivated = "1515";
	String AccountAlreadyActivated = "1516";

	String LogoutUserSuccessful = "1611";
	String LogoutUserFailure = "1612";

	String BlockUserSuccessful = "1811";
	String BlockUserFailure = "1812";

	String EmailSendSuccessfully = "1911";

	String OTPSendSuccessfully = "1901";
	String OTPSendFailure = "1902";
	String MobileNoRegisteredWithAnotherUser = "1903";

	String PasswordChangeSuccessful = "1921";
	String PasswordChangeFailure = "1922";
	String PasswordsDoNotMatch = "1923";
	String PasswordCannotBeNull = "1924";
	String OldPasswordMatchFailed = "1925";
	String WrongOTP = "1926";
	
	String MobileNumberVerificationSuccessful = "1927";
	String MobileNumberVerificationFailed="1928";
	String MobileNumberAlreadyVerified = "1929";

	String ActivationLinkSendSuccessful = "1931";
	String ActivationLinkSendFailure = "1932";

	String AccountActivationSuccessful = "1941";
	String AccountActivationFailure = "1942";
	String EmailNotVerified = "1943";
	String EmailIdNotVerified = "1944";

	String ActivationOTPSendSuccessful = "1951";
	String ActivationOTPSendFailure = "1952";

	String SyncDetailsReadSuccessful = "1961";
	String SyncDetailsReadFailure = "1962";
	
	String CreatePaymnetRequestSuccessfull = "2011";
	String CreatePaymentRequestFailure = "2012";
	String PaymentSuccessfull = "2021";
	String PaymentFailure = "2022";
	String PaymentDetailsExist = "2023";
	String PaymentUpdateFailed = "2024";
	String PaymentIdNotValid = "2025";
	
	String FileUploadSuccessful = "2031";
	String FileUploadFailure = "2032";
}
