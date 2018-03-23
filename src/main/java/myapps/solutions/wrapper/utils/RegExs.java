package myapps.solutions.wrapper.utils;

public interface RegExs {

	String userName = "^[a-zA-Z0-9_-]*$";
	String emailId = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	String indianMobileNo = "\\+91[0-9]{10}";
	String pincode = "[0-9]{6}";
	int minUserLen = 6;
	int maxUserLen = 15;
	int minAddressingLen = 2;
	int maxAddressingLen = 46;
	int minAge = 13;
}
