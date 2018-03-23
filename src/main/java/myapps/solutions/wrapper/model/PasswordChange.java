package myapps.solutions.wrapper.model;

public class PasswordChange {
	
	private byte[] newPassword;
	private byte[] oldPassword;
	private byte[] confirmPassword;
	
	public PasswordChange() {
		
	}
	
	public PasswordChange(byte[] oldPassword, byte[]newPassword, byte[] confirmPassword){
		
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword; 
	}
	
	public PasswordChange(byte[]newPassword, byte[] confirmPassword){
		
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword; 
	}

	
	public PasswordChange(byte[] newPassword){
		this.newPassword=newPassword;
	}

	public byte[] getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(byte[] newPassword) {
		this.newPassword = newPassword;
	}
	
	public byte[] getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(byte[] oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public byte[] getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(byte[] confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}

