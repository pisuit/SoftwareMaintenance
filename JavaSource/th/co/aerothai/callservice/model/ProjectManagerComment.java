package th.co.aerothai.callservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import th.co.aerothai.callservice.customtype.DataStatus;
import th.co.aerothai.callservice.customtype.ProjectType;
import th.co.aerothai.callservice.model.hr.PersonalInfo;

@Entity
@Table(name="PROJECT_MANAGER_COMMENT")
@GenericGenerator(strategy="th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class ProjectManagerComment implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REQUEST_ID", referencedColumnName="ID")
	private Request request;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MANAGER_ID", referencedColumnName="STAFFCODE")
	private PersonalInfo manager;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OPERATOR_ID", referencedColumnName="STAFFCODE")
	private PersonalInfo operator;
	
	@Column(name="ISSUE_DATE")
	private Date issueDate;
	
	@Column(name="PROJECT_TYPE")
	@Enumerated(EnumType.STRING)
	private ProjectType projectType;
	
	@OneToMany(mappedBy="projectManagerComment", fetch=FetchType.LAZY)
	private Set<AssignedOperator> operators;
	
	@Column(name="COMMENT_BEFORE")
	private String commentBefore;
	
	@Column(name="COMMENT_AFTER")
	private String commentAfter;
	
	@Column(name="PROBLEMS")
	private String problems;
	
	@Column(name="START_DATE")
	private Date startDate;
	
	@Column(name="END_DATE")
	private Date endDate;
	
	@Column(name="APPROVAL")
	private boolean approval;
	
	@Column(name="DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;
	
	@Transient
	private String operatorAsString;

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

	public PersonalInfo getOperator() {
		return operator;
	}

	public void setOperator(PersonalInfo operator) {
		this.operator = operator;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public String getCommentBefore() {
		return commentBefore;
	}

	public void setCommentBefore(String commentBefore) {
		this.commentBefore = commentBefore;
	}

	public String getCommentAfter() {
		return commentAfter;
	}

	public void setCommentAfter(String commentAfter) {
		this.commentAfter = commentAfter;
	}

	public String getProblems() {
		return problems;
	}

	public void setProblems(String problems) {
		this.problems = problems;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isApproval() {
		return approval;
	}

	public void setApproval(boolean approval) {
		this.approval = approval;
	}

	public DataStatus getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	public PersonalInfo getManager() {
		return manager;
	}

	public void setManager(PersonalInfo manager) {
		this.manager = manager;
	}

	public Set<AssignedOperator> getOperators() {
		return operators;
	}

	public void setOperators(Set<AssignedOperator> operators) {
		this.operators = operators;
	}

	public String getOperatorAsString() {
		if(operators != null){
			List<String> strings = new ArrayList<String>();
			for(AssignedOperator a : operators){
				strings.add(a.getOperator().toString());
			}
			return StringUtils.join(strings, ", ");
		} else {
			return "";
		}
	}
}
