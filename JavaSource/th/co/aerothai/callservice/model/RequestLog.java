package th.co.aerothai.callservice.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import th.co.aerothai.callservice.customtype.UserRole;
import th.co.aerothai.callservice.model.hr.PersonalInfo;

@Entity
@Table(name="REQUEST_LOG")
@GenericGenerator(strategy="th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class RequestLog implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REQUEST_ID", referencedColumnName="ID")
	private Request request;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ACTOR_ID", referencedColumnName="STAFFCODE")
	private PersonalInfo actor;
	
	@Column(name="ROLE")
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="ISSUE_TIME")
	private Date issueTime;
	
	public RequestLog(){
		
	}
	
	public RequestLog(Request request, PersonalInfo actor, UserRole role, String description, Date issueTime){
		this.request = request;
		this.actor = actor;
		this.role = role;
		this.description = description;
		this.issueTime = issueTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public PersonalInfo getActor() {
		return actor;
	}

	public void setActor(PersonalInfo actor) {
		this.actor = actor;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}
}
