package th.co.aerothai.callservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import th.co.aerothai.callservice.customtype.DataStatus;
import th.co.aerothai.callservice.model.hr.Department;
import th.co.aerothai.callservice.model.hr.PersonalInfo;


@Entity
@Table(name="CALL_SERVICE_COMMENT")
@GenericGenerator(strategy="th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class CallServiceComment implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REQUEST_ID", referencedColumnName="ID")
	private Request request;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SERVICE_OFFICER_ID", referencedColumnName="STAFFCODE")
	private PersonalInfo serviceOfficer;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COORDINATOR_ID", referencedColumnName="STAFFCODE")
	private PersonalInfo coordinator;
	
	@Column(name="PHONE_NUMBER")
	private String phoneNumber;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="WORK_PLACE_ID", referencedColumnName="ID")
	private WorkPlace workPlace;
	
	@Column(name="ISSUE_DATE")
	private Date issueDate;
	
	@Column(name="REMARK")
	private String remark;
	
	@Column(name="APPROVAL")
	private boolean approval;
	
	@Column(name="DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;
	
	@OneToMany(mappedBy="callServiceComment", fetch=FetchType.LAZY)
	private Set<CallServiceJob> callServiceJobs;
	
	@Transient
	private String jobsAsString;

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

	public PersonalInfo getServiceOfficer() {
		return serviceOfficer;
	}

	public void setServiceOfficer(PersonalInfo serviceOfficer) {
		this.serviceOfficer = serviceOfficer;
	}

	public PersonalInfo getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(PersonalInfo coordinator) {
		this.coordinator = coordinator;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public WorkPlace getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(WorkPlace workPlace) {
		this.workPlace = workPlace;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<CallServiceJob> getCallServiceJobs() {
		return callServiceJobs;
	}

	public void setCallServiceJobs(Set<CallServiceJob> callServiceJobs) {
		this.callServiceJobs = callServiceJobs;
	}

	public String getJobsAsString() {
		List<String> stringList = new ArrayList<String>();
		for(CallServiceJob job : callServiceJobs){
			stringList.add(job.getJob().getJobName());
		}
		return StringUtils.join(stringList, ", ");
	}
}
