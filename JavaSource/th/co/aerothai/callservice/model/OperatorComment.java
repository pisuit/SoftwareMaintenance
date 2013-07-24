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

import th.co.aerothai.callservice.customtype.DataStatus;
import th.co.aerothai.callservice.model.hr.PersonalInfo;


@Entity
@Table(name="OPERATOR_COMMENT")
@GenericGenerator(strategy="th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class OperatorComment implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REQUEST_ID", referencedColumnName="ID")
	private Request request;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OPERATOR_ID", referencedColumnName="STAFFCODE")
	private PersonalInfo operator;
	
	@Column(name="ISSUE_DATE")
	private Date issueDate;
	
	@Column(name="REMARK")
	private String remark;
	
	@Column(name="PROBLEMS")
	private String problems;
	
	@Column(name="APPROVAL")
	private boolean approval;
	
	@Column(name="DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;

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

	public String getProblems() {
		return problems;
	}

	public void setProblems(String problems) {
		this.problems = problems;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
