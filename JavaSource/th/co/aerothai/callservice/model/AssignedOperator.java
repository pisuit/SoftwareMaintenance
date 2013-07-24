package th.co.aerothai.callservice.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import th.co.aerothai.callservice.model.hr.PersonalInfo;

@Entity
@Table(name="ASSIGNED_OPERATOR")
@GenericGenerator(strategy = "th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name = "IDGENERATOR")
public class AssignedOperator implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PM_COMMENT_ID", referencedColumnName="ID")
	private ProjectManagerComment projectManagerComment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OPERATOR", referencedColumnName = "STAFFCODE")
	private PersonalInfo operator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProjectManagerComment getProjectManagerComment() {
		return projectManagerComment;
	}

	public void setProjectManagerComment(ProjectManagerComment projectManagerComment) {
		this.projectManagerComment = projectManagerComment;
	}

	public PersonalInfo getOperator() {
		return operator;
	}

	public void setOperator(PersonalInfo operator) {
		this.operator = operator;
	}
}
