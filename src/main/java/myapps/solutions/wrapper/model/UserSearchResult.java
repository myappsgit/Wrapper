package myapps.solutions.wrapper.model;

public class UserSearchResult {

	String userName;
	String emailId;
	String mobileNo;
	int isActive;
	Integer id;
	String displayName;

	public UserSearchResult() {

	}

	public UserSearchResult(int id, String userName, String displayName, String emailId, String mobileNo, int isActive) {
		this.id = id;
		this.userName = userName;
		this.displayName = displayName;
		this.emailId = emailId;
		this.mobileNo = mobileNo;
		this.isActive = isActive;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int isActive() {
		return isActive;
	}

	public void setActive(int isActive) {
		this.isActive = isActive;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}