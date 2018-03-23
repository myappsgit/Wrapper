package myapps.solutions.wrapper.model;
// Generated May 3, 2017 10:43:48 AM by Hibernate Tools 5.2.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * AccountNotification generated by hbm2java
 */
@Entity
@Table(name = "account_notification", catalog = "wrapper")
public class AccountNotification implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -613832476664427173L;
	@NotEmpty
	private int userId;
	@NotEmpty
	private UserDetails userDetails;
	private Boolean promotionToDevice;
	private Boolean promotionThruMail;
	private Boolean licenseExpiry;

	public AccountNotification() {
	}

	public AccountNotification(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public AccountNotification(UserDetails userDetails, Boolean promotionToDevice, Boolean promotionThruMail,
			Boolean licenseExpiry) {
		this.userDetails = userDetails;
		this.promotionToDevice = promotionToDevice;
		this.promotionThruMail = promotionThruMail;
		this.licenseExpiry = licenseExpiry;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "userDetails"))
	@Id
	@GeneratedValue(generator = "generator")
	@JsonIgnore
	@Column(name = "userId", unique = true, nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public UserDetails getUserDetails() {
		return this.userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	@Column(name = "promotionToDevice")
	public Boolean getPromotionToDevice() {
		return this.promotionToDevice;
	}

	public void setPromotionToDevice(Boolean promotionToDevice) {
		this.promotionToDevice = promotionToDevice;
	}

	@Column(name = "promotionThruMail")
	public Boolean getPromotionThruMail() {
		return this.promotionThruMail;
	}

	public void setPromotionThruMail(Boolean promotionThruMail) {
		this.promotionThruMail = promotionThruMail;
	}

	@Column(name = "licenseExpiry")
	public Boolean getLicenseExpiry() {
		return this.licenseExpiry;
	}

	public void setLicenseExpiry(Boolean licenseExpiry) {
		this.licenseExpiry = licenseExpiry;
	}

}