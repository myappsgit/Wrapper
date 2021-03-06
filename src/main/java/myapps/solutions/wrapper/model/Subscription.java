package myapps.solutions.wrapper.model;
// Generated May 8, 2017 3:02:42 PM by Hibernate Tools 5.2.1.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Subscription generated by hbm2java
 */
@Entity
@Table(name = "subscription", catalog = "wrapper")
public class Subscription implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6019056231201750368L;
	private Integer subId;
	private Package package_;
	private String subName;
	private String description;
	private BigDecimal price;
	private String type;
	private List<UserSubscription> userSubscriptions = new ArrayList<UserSubscription>(0);

	public Subscription() {
	}

	public Subscription(String subName, BigDecimal price, String type) {
		this.subName = subName;
		this.price = price;
		this.type = type;
	}

	public Subscription(Package package_, String subName, String description, BigDecimal price, String type,
			List<UserSubscription> userSubscriptions) {
		this.package_ = package_;
		this.subName = subName;
		this.description = description;
		this.price = price;
		this.type = type;
		this.userSubscriptions = userSubscriptions;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "subID", unique = true, nullable = false)
	public Integer getSubId() {
		return this.subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "packageId")
	public Package getPackage() {
		return this.package_;
	}

	public void setPackage(Package package_) {
		this.package_ = package_;
	}

	@Column(name = "subName", nullable = false, length = 45)
	public String getSubName() {
		return this.subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "price", nullable = false, precision = 4)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "type", nullable = false, length = 45)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subscription")
	public List<UserSubscription> getUserSubscriptions() {
		return this.userSubscriptions;
	}

	public void setUserSubscriptions(List<UserSubscription> userSubscriptions) {
		this.userSubscriptions = userSubscriptions;
	}

}
