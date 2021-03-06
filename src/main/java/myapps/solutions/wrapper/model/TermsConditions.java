package myapps.solutions.wrapper.model;

import static javax.persistence.GenerationType.IDENTITY;

// Generated Aug 12, 2017 4:33:07 PM by Hibernate Tools 5.2.1.Final
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * TermsConditions generated by hbm2java
 */
@Entity
@Table(name = "terms_conditions", catalog = "wrapper")
@SqlResultSetMapping(name = "tcps", classes = { @ConstructorResult(targetClass = TermsConditions.class, columns = {
		@ColumnResult(name = "id"), @ColumnResult(name = "description") }) })
public class TermsConditions implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5599933532561141925L;
	private Integer id;
	private Product product;
	private UserType userType;
	private String description;
	private boolean status;
	private Date date;
	private Date startDate;
	private Set<TermsConditionsHistory> termsConditionsHistories = new HashSet<TermsConditionsHistory>(0);
	private String fileName;
	private String title;

	public TermsConditions() {
	}

	public TermsConditions(int id) {
		this.id = id;
	}
	
	public TermsConditions(int id, String description){
		this.id = id;
		this.description = description;
	}
	
	public TermsConditions(Product product, UserType userType, String description, boolean status, Date date) {
		this.product = product;
		this.userType = userType;
		this.description = description;
		this.status = status;
		this.date = date;
	}

	public TermsConditions(Product product, UserType userType, String description, boolean status, Date date,
			Date startDate, Set<TermsConditionsHistory> termsConditionsHistories) {
		this.product = product;
		this.userType = userType;
		this.description = description;
		this.status = status;
		this.date = date;
		this.startDate = startDate;
		this.termsConditionsHistories = termsConditionsHistories;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId", nullable = false)
	@JsonProperty(access = Access.WRITE_ONLY)
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userType", nullable = false)
	public UserType getUserType() {
		return this.userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Column(name = "description", nullable = false)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "status", nullable = false)
	@JsonIgnore
	public boolean isStatus() {
		return this.status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date", nullable = false, length = 19)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "startDate", length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "termsConditions")
	@JsonIgnore
	public Set<TermsConditionsHistory> getTermsConditionsHistories() {
		return this.termsConditionsHistories;
	}

	public void setTermsConditionsHistories(Set<TermsConditionsHistory> termsConditionsHistories) {
		this.termsConditionsHistories = termsConditionsHistories;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Transient
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
