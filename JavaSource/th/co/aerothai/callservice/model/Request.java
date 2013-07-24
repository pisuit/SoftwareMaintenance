package th.co.aerothai.callservice.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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

import org.apache.neethi.All;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.jboss.resteasy.spi.touri.MappedBy;

import th.co.aerothai.callservice.customtype.ApprovalType;
import th.co.aerothai.callservice.customtype.BudgetType;
import th.co.aerothai.callservice.customtype.DataStatus;
import th.co.aerothai.callservice.customtype.IssueType;
import th.co.aerothai.callservice.customtype.StateType;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.utils.CustomComparator;

@Entity
@Table(name = "request")
@GenericGenerator(strategy = "th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name = "IDGENERATOR")
public class Request implements Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "IDGENERATOR")
	private Long id;

	@Column(name = "REQUEST_NUMBER")
	private int requestNumber;

	@Column(name = "TITLE")
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_TYPE_ID", referencedColumnName = "ID")
	private ServiceType serviceType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MANAGER", referencedColumnName = "STAFFCODE")
	private PersonalInfo manager;

	@Column(name = "REFERENCE_DOC")
	private String referenceDocument;

	@Column(name = "ISSUE_DATE")
	private Date issueDate;

	@Column(name = "USE_DATE")
	private Date useDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQUESTER", referencedColumnName = "STAFFCODE")
	private PersonalInfo requester;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "PURPOSE")
	private String purpose;

	@Column(name = "SYSTEM_NAME")
	private String systemName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "IS_BUDGET")
	private boolean isBudget = false;

	@Column(name = "ISSUE_TYPE")
	@Enumerated(EnumType.STRING)
	private IssueType issueType;

	@Column(name = "BUDGET_TYPE")
	@Enumerated(EnumType.STRING)
	private BudgetType budgetType;

	@Column(name = "BUDGET_USE")
	private BigDecimal budgetUse = new BigDecimal("0");

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_ID", referencedColumnName = "ID")
	private Job job;

	@Column(name = "SATISFACTION_COMMENT")
	private String satisfactionComment;

	@Column(name = "DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;

	@OneToOne(mappedBy = "request", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private UserDirectorComment userDirectorComment;

	@OneToOne(mappedBy = "request", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private ProviderDirectorComment providerDirectorComment;
	
	@OneToOne(mappedBy="request", fetch=FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	private CallServiceComment callServiceComment;
	
	@OneToOne(mappedBy="request", fetch=FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	private InspectorComment inspectorComment;
	
	@OneToOne(mappedBy="request", fetch=FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	private ProjectManagerComment projectManagerComment;
	
	@OneToOne(mappedBy="request", fetch=FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	private OperatorComment operatorComment;
	
	@OneToMany(mappedBy="request", fetch=FetchType.LAZY)
	private Set<RequestLog> requestLogs;
	
	@OneToMany(mappedBy="request", fetch=FetchType.LAZY)
	private Set<Attachment> attachments;
	
	@Column(name="MARKED")
	private boolean isMarked = false;
	
	@Transient
	private List<Attachment> attachmentList;
	
	@Column(name="STATE")
	@Enumerated(EnumType.STRING)
	private StateType stateType;
	
	@Column(name="RATING")
	private Integer rating = 0;
	
	@Transient
	private String status;
	
	@Transient
	private List<RequestLog> logList;
	
	@Transient
	private int progress;
	
	@Transient
	private String nextRole;
	
	@Transient
	private String nextAction;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public PersonalInfo getManager() {
		return manager;
	}

	public void setManager(PersonalInfo manager) {
		this.manager = manager;
	}

	public String getReferenceDocument() {
		return referenceDocument;
	}

	public void setReferenceDocument(String referenceDocument) {
		this.referenceDocument = referenceDocument;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public PersonalInfo getRequester() {
		return requester;
	}

	public void setRequester(PersonalInfo requester) {
		this.requester = requester;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isBudget() {
		return isBudget;
	}

	public void setBudget(boolean isBudget) {
		this.isBudget = isBudget;
	}

	public BudgetType getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(BudgetType budgetType) {
		this.budgetType = budgetType;
	}

	public BigDecimal getBudgetUse() {
		return budgetUse;
	}

	public void setBudgetUse(BigDecimal budgetUse) {
		this.budgetUse = budgetUse;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getSatisfactionComment() {
		return satisfactionComment;
	}

	public void setSatisfactionComment(String satisfactionComment) {
		this.satisfactionComment = satisfactionComment;
	}

	public DataStatus getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	public IssueType getIssueType() {
		return issueType;
	}

	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public UserDirectorComment getUserDirectorComment() {
		return userDirectorComment;
	}

	public void setUserDirectorComment(UserDirectorComment userDirectorComment) {
		this.userDirectorComment = userDirectorComment;
	}

	public ProviderDirectorComment getProviderDirectorComment() {
		return providerDirectorComment;
	}

	public void setProviderDirectorComment(
			ProviderDirectorComment providerDirectorComment) {
		this.providerDirectorComment = providerDirectorComment;
	}

	public CallServiceComment getCallServiceComment() {
		return callServiceComment;
	}

	public void setCallServiceComment(CallServiceComment callServiceComment) {
		this.callServiceComment = callServiceComment;
	}

	public OperatorComment getOperatorComment() {
		return operatorComment;
	}

	public void setOperatorComment(OperatorComment operatorComment) {
		this.operatorComment = operatorComment;
	}

	public int getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(int requestNumber) {
		this.requestNumber = requestNumber;
	}

	public StateType getStateType() {
		return stateType;
	}

	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setStatusValue(String role){
		if(role.equals("requester")){
			if(stateType.equals(StateType.NEW_D) || stateType.equals(StateType.NEW_P))
				status = "NEW";
			if(callServiceComment != null || userDirectorComment != null)
				status = "PENDING";
			if((userDirectorComment != null && userDirectorComment.getApprovalType().equals(ApprovalType.DECLINED)) || (providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.DECLINED)))
				status = "DECLINED";
			if(((userDirectorComment != null && !userDirectorComment.getApprovalType().equals(ApprovalType.DECLINED)) || callServiceComment != null || (providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.DECLINED))) && (inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.ACCEPTED)))
				status = "COMPLETED";
			if(inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.ACCEPTED) && rating > 0)
				status = "CLOSED";
		}
		if(role.equals("userdirector")){
			if(userDirectorComment == null)
				status = "NEW";
			if(userDirectorComment != null && userDirectorComment.getApprovalType().equals(ApprovalType.DECLINED))
				status = "DECLINED";
			if(userDirectorComment != null && userDirectorComment.getApprovalType().equals(ApprovalType.APPROVED))
				status = "APPROVED";
		}
		if(role.equals("callservice")){
			if(callServiceComment == null)
				status = "NEW";
			if(callServiceComment != null)
				status = "DONE";
			if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.REJECTED))
				status = "REJECTED";
		}
		if(role.equals("director")){
			if(projectManagerComment != null)
				status = "WAITING";
			if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.DECLINED))
				status = "DECLINED";
			if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.APPROVED))
				status = "APPROVED";
			if(projectManagerComment == null && providerDirectorComment == null)
				status = "NEW";
			if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.ACCEPTED) && projectManagerComment == null)
				status = "ACCEPTED";
			if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.REJECTED) && projectManagerComment == null)
				status = "REJECTED";
		}
		if(role.equals("projectmanager")){
			if(projectManagerComment == null)
				status = "NEW";
			if(projectManagerComment != null)
				status = "DONE";
		}
		if(role.equals("operator")){
			if(operatorComment == null)
				status = "NEW";
			if(operatorComment != null)
				status = "DONE";
			if(inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.REJECTED))
				status = "REJECTED";
		}
		if(role.equals("inspector")){
			if(inspectorComment == null)
				status = "NEW";
			if(inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.ACCEPTED))
				status = "ACCEPTED";
			if(inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.REJECTED))
				status = "REJECTED";
		}
	}

	public ProjectManagerComment getProjectManagerComment() {
		return projectManagerComment;
	}

	public void setProjectManagerComment(ProjectManagerComment projectManagerComment) {
		this.projectManagerComment = projectManagerComment;
	}

	public InspectorComment getInspectorComment() {
		return inspectorComment;
	}

	public void setInspectorComment(InspectorComment inspectorComment) {
		this.inspectorComment = inspectorComment;
	}

	public Set<RequestLog> getRequestLogs() {
		return requestLogs;
	}

	public void setRequestLogs(Set<RequestLog> requestLogs) {
		this.requestLogs = requestLogs;
	}

	public List<RequestLog> getLogList() {
		if(requestLogs != null){
			List<RequestLog> logs = new ArrayList<RequestLog>(requestLogs);
			Collections.sort(logs, new CustomComparator());
			return logs;
		} else {
			return null;
		}
		
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<Attachment> getAttachmentList() {
		if(attachments != null){
			List<Attachment> list = new ArrayList<Attachment>(attachments);
			return list;
		} else {
			return attachmentList;
		}
		
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public int getProgress() {
		if(stateType.equals(StateType.NEW_D) || stateType.equals(StateType.NEW_P)){
			return 0;
		} else {
			float a = Float.parseFloat(stateType.getValue().substring(0, 1));
			float b = Float.parseFloat(stateType.getValue().substring(2, 3));
			
			return Math.round((100/b)*a);
		}	
	}

	public String getNextRole() {
		if(stateType.equals(StateType.NEW_D)){
			return "User Director";
		} else if(stateType.equals(StateType.NEW_P)){
			return "Call Service";
		} else if(userDirectorComment != null && userDirectorComment.getApprovalType().equals(ApprovalType.APPROVED) && callServiceComment == null){
			return "Call Service";
		} else if(userDirectorComment != null && userDirectorComment.getApprovalType().equals(ApprovalType.DECLINED)){
			return "None";
		} else if(callServiceComment != null && providerDirectorComment == null){
			return "Provider Director";
		} else if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.REJECTED)){
			return "Call Service";
		} else if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.ACCEPTED) && projectManagerComment == null){
			return "Project Manager";
		} else if(projectManagerComment != null && providerDirectorComment != null && (!providerDirectorComment.getApprovalType().equals(ApprovalType.DECLINED) && !providerDirectorComment.getApprovalType().equals(ApprovalType.APPROVED))){
			return "Provider Director";
		} else if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.DECLINED)){
			return "None";
		} else if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.APPROVED) && operatorComment == null){
			return "Operator";
		} else if(operatorComment != null && inspectorComment == null){
			return "Inspector";
		} else if(inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.ACCEPTED)){
			return "None";
		} else if(inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.REJECTED)){
			return "Operator";
		}
		else return "None";
	}

	public String getNextAction() {
		if(stateType.equals(StateType.NEW_D)){
			return "Approve / Decline";
		} else if(stateType.equals(StateType.NEW_P)){
			return "Task Classify";
		} else if(userDirectorComment != null && userDirectorComment.getApprovalType().equals(ApprovalType.APPROVED) && callServiceComment == null){
			return "Task Classify";
		} else if(userDirectorComment != null && userDirectorComment.getApprovalType().equals(ApprovalType.DECLINED)){
			return "None";
		} else if(callServiceComment != null && providerDirectorComment == null){
			return "Accept / Reject";
		} else if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.REJECTED)){
			return "Task Classify";
		} else if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.ACCEPTED) && projectManagerComment == null){
			return "Give Suggestion";
		} else if(projectManagerComment != null && providerDirectorComment != null && (!providerDirectorComment.getApprovalType().equals(ApprovalType.DECLINED) && !providerDirectorComment.getApprovalType().equals(ApprovalType.APPROVED))){
			return "Approve / Decline";
		} else if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.DECLINED)){
			return "None";
		} else if(providerDirectorComment != null && providerDirectorComment.getApprovalType().equals(ApprovalType.APPROVED) && operatorComment == null){
			return "Operate";
		} else if(operatorComment != null && inspectorComment == null){
			return "Accept / Reject";
		} else if(inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.ACCEPTED)){
			return "None";
		} else if(inspectorComment != null && inspectorComment.getApprovalType().equals(ApprovalType.REJECTED)){
			return "Operate";
		}
		else return "None";
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

}
