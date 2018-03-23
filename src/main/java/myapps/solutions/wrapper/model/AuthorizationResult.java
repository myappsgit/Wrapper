package myapps.solutions.wrapper.model;

public class AuthorizationResult {
	
	public enum AuthorizationCodes{
		Success, 
		Fail,
		DeviceLimitReached,
		PasswordMisMatch,
		NotFound,
		SessionExist,
		MobileNumberNotVerified,
		EmailIdNotVerified,
		AccountDeActivated
	}
	
	private int userId;
	private String sessionId;
	private String userType;
	private AuthorizationCodes result;
	private SyncDetails syncDetails;
	
	public AuthorizationResult(int userId){
		this.userId = userId;
	}
	
	public AuthorizationResult(int userId, String userType, String sessionId, AuthorizationCodes result){
		this.userId = userId;
		this.userType = userType;
		this.sessionId = sessionId;
		this.result = result;
	}
	
	public AuthorizationResult(int userId, String userType, String sessionId, AuthorizationCodes result, SyncDetails syncDetails){
		this.userId = userId;
		this.userType = userType;
		this.sessionId = sessionId;
		this.result = result;
		this.syncDetails = syncDetails;
	}
	
	public AuthorizationResult(AuthorizationCodes result){
		this.result = result;
	}

	public AuthorizationResult(int userId, AuthorizationCodes result){
		this.userId = userId;
		this.result = result;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public AuthorizationCodes getResult() {
		return result;
	}

	public void setResult(AuthorizationCodes result) {
		this.result = result;
	}

	public SyncDetails getSyncDetails() {
		return syncDetails;
	}

	public void setSyncDetails(SyncDetails syncDetails) {
		this.syncDetails = syncDetails;
	}
	
}
