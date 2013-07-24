package th.co.aerothai.callservice.manager;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.tmatesoft.svn.core.SVNException;

import th.co.aerothai.callservice.customtype.BudgetType;
import th.co.aerothai.callservice.customtype.DataStatus;
import th.co.aerothai.callservice.customtype.IssueType;
import th.co.aerothai.callservice.customtype.StateType;
import th.co.aerothai.callservice.dao.RequestDao;
import th.co.aerothai.callservice.model.Attachment;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator;
import th.co.aerothai.callservice.utils.SVNUtils;
import th.co.aerothai.callservice.utils.SessionUtils;

@ManagedBean(name="requestManager")
@ViewScoped
public class RequestManager {
	private List<Request> requestList = new ArrayList<Request>();
	private Request editRequest = new Request();
	private List<IssueType> issueTypes = new ArrayList<IssueType>();
	private List<BudgetType> budgetTypes = new ArrayList<BudgetType>();
	private Request viewRequest = new Request();
	private List<Request> filtered;
	private String dialogHeader = "New Request";
	private List<SelectItem> filterOptions = new ArrayList<SelectItem>();
	private List<Attachment> attachments = new ArrayList<Attachment>();
	private List<Attachment> deletedAttachment = new ArrayList<Attachment>();
	private boolean isDeptList = false;
	private Integer rating;
	
	public RequestManager(){
		createIssueTypes();
		createBudgetType();
		createRequestList();
		createFilterOptions();
	}
	
	private void createFilterOptions(){
		filterOptions.clear();
		filterOptions.add(new SelectItem("", "ALL"));
		filterOptions.add(new SelectItem("NEW", "NEW"));
		filterOptions.add(new SelectItem("PENDING", "PENDING"));
		filterOptions.add(new SelectItem("DECLINED", "DECLINED"));
		filterOptions.add(new SelectItem("COMPLETED", "COMPLETED"));
		filterOptions.add(new SelectItem("CLOSED", "CLOSED"));
	}
	
	public void newDialog(){
		editRequest = new Request();
		attachments.clear();
		deletedAttachment.clear();
		dialogHeader = "New Request";
		
		editRequest.setPhoneNumber(RequestDao.getPhoneNumber());
	}
	
	public void editDialog(){
		dialogHeader = "Edit Request";
		attachments.clear();
		deletedAttachment.clear();
		attachments.addAll(editRequest.getAttachments());
	}
	
	private void createIssueTypes(){
		for(IssueType type : IssueType.values()){
			issueTypes.add(type);
		}
	}
	
	public void handleFIle(FileUploadEvent event){
		Attachment attachment = new Attachment();
		attachment.setContentType(event.getFile().getContentType());
		attachment.setLogicalName(new HibernateCurrentTimeIDGenerator().gen().toString());
		attachment.setPhysicalName(event.getFile().getFileName());
		attachment.setUploadedFile(event.getFile());
		attachments.add(attachment);
	}
	
	private void createBudgetType(){
		for(BudgetType type : BudgetType.values()){
			budgetTypes.add(type);
		}
	}
	
	public void createRequestList(){
		requestList.clear();
		if(isDeptList){
			requestList.addAll(RequestDao.getRequestList("requesterd"));
		} else {
			requestList.addAll(RequestDao.getRequestList("requesterp"));
		}
		for(Request request : requestList){
			request.setStatusValue("requester");
		}
	}
	
	public void onRate(Request request){
		if(request.getStatus().equals("CLOSED")){
			if(request.getRating() == 0){
				SessionUtils.getNotification().setRequester(SessionUtils.getNotification().getRequester()+1);
			}
		} else {
			SessionUtils.getNotification().setRequester(SessionUtils.getNotification().getRequester()-1);
		}
		
		RequestDao.saveRating(request);
		for(Request req : requestList){
			req.setStatusValue("requester");
		}
	}
	
	public void deleteRequest(){
		editRequest.setDataStatus(DataStatus.DELETED);
		RequestDao.saveRequest(editRequest);	
		if(filtered != null){
			filtered.remove(editRequest);
		}
		editRequest = new Request();
		createRequestList();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Your request has been deleted."));	
	}
	
	public void deleteFile(Attachment file){
		attachments.remove(file);
		deletedAttachment.add(file);
	}
	
	public void saveRequest(){
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		
		if(editRequest.getTitle().trim().length() == 0)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Title is required."));
		if(editRequest.getUseDate() == null)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Use date is required."));
		if(editRequest.getIssueType() == null)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Issue type is required."));
		if(editRequest.getPhoneNumber().trim().length() == 0)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Phone number is required."));
		if(editRequest.getSystemName().trim().length() == 0)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "System is required."));
		if(editRequest.getPurpose().trim().length() == 0)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Purpose is required."));
		if(editRequest.getDescription().trim().length() == 0)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Description is required."));
		if(editRequest.isBudget() == true){
			if(editRequest.getBudgetType() == null)
				msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Budget type is required."));
			if(editRequest.getBudgetUse().equals(new BigDecimal("0")))
				msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Budget amount is required."));
		}
		
		if(msgs.size() == 0){
			editRequest.setRequester(SessionUtils.getUserSession().getStaff());
			
			if(editRequest.getId() == null){
				editRequest.setRequestNumber(RequestDao.getNextRequestNumber());
				editRequest.setIssueDate(new Date());
			}
			if(!editRequest.isBudget()){
				editRequest.setBudgetType(null);
			}
			if(editRequest.getIssueType().equals(IssueType.PERSONAL)){
				editRequest.setStateType(StateType.NEW_P);
			} else {
				editRequest.setStateType(StateType.NEW_D);
			}
			
			RequestDao.saveRequest(editRequest);
			
			for(Attachment file : attachments){
				if(file.getId() == null){
					try {
						boolean isSuccess = SVNUtils.uploadFile(file.getUploadedFile().getInputstream(), file.getLogicalName());
						
						if(isSuccess){
							file.setRequest(editRequest);
							RequestDao.saveAttachment(file);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			for(Attachment deletedFile : deletedAttachment){
				if(deletedFile.getId() != null){
					try {
						SVNUtils.deleteFile(deletedFile.getLogicalName());
						RequestDao.deleteAttachment(deletedFile);
					} catch (SVNException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			resetData();
			createRequestList();
			RequestContext.getCurrentInstance().execute("newRequestDialog.hide();blockUI.unblock()");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "We have received your request."));
		} else {
			for(FacesMessage msg : msgs){
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			RequestContext.getCurrentInstance().execute("blockUI.unblock()");
		}
	}
	
	private void resetData(){
		editRequest = new Request();
		attachments.clear();
		deletedAttachment.clear();
	}
	
	public List<Request> getRequestList() {
		return requestList;
	}
	public void setRequestList(List<Request> requestList) {
		this.requestList = requestList;
	}
	public Request getEditRequest() {
		return editRequest;
	}
	public void setEditRequest(Request editRequest) {
		this.editRequest = editRequest;
	}

	public List<IssueType> getIssueTypes() {
		return issueTypes;
	}

	public void setIssueTypes(List<IssueType> issueTypes) {
		this.issueTypes = issueTypes;
	}

	public List<BudgetType> getBudgetTypes() {
		return budgetTypes;
	}

	public void setBudgetTypes(List<BudgetType> budgetTypes) {
		this.budgetTypes = budgetTypes;
	}

	public Request getViewRequest() {
		return viewRequest;
	}

	public void setViewRequest(Request viewRequest) {
		this.viewRequest = viewRequest;
	}

	public List<Request> getFiltered() {
		return filtered;
	}

	public void setFiltered(List<Request> filtered) {
		this.filtered = filtered;
	}

	public String getDialogHeader() {
		return dialogHeader;
	}

	public void setDialogHeader(String dialogHeader) {
		this.dialogHeader = dialogHeader;
	}

	public List<SelectItem> getFilterOptions() {
		return filterOptions;
	}

	public void setFilterOptions(List<SelectItem> filterOptions) {
		this.filterOptions = filterOptions;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public boolean isDeptList() {
		return isDeptList;
	}

	public void setDeptList(boolean isDeptList) {
		this.isDeptList = isDeptList;
	}
}
