package th.co.aerothai.callservice.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import th.co.aerothai.callservice.customtype.StateType;
import th.co.aerothai.callservice.dao.GenericDao;
import th.co.aerothai.callservice.dao.RequestDao;
import th.co.aerothai.callservice.model.CallServiceComment;
import th.co.aerothai.callservice.model.CallServiceJob;
import th.co.aerothai.callservice.model.Job;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.WorkPlace;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.utils.SessionUtils;

@ManagedBean(name="callServiceManager")
@ViewScoped
public class CallServiceManager {
	private Request editRequest = new Request();
	private String id;
	private CallServiceComment editCallServiceComment = new CallServiceComment();
	private List<WorkPlace> workPlaceList = new ArrayList<WorkPlace>();
	private List<Job> internalJobList = new ArrayList<Job>();
	private List<Job> externalJobList = new ArrayList<Job>();
	private List<Job> selectedInternalJob = new ArrayList<Job>();
	private List<Job> selectedExternalJob = new ArrayList<Job>();
	private List<Request> requestList = new ArrayList<Request>();
	private List<Request> filtered;
	private Request viewRequest = new Request();
	private List<SelectItem> filterOptions = new ArrayList<SelectItem>();
	
	public CallServiceManager(){	
		createWorkPlaceList();
		createInternalJobList();
		createExternalJobList();
		createRequestList();
		createFilterOptions();                          
	}
	
	public void setData(){
		if(editRequest.getCallServiceComment() != null){
			editCallServiceComment = editRequest.getCallServiceComment();
			if(editCallServiceComment.getCallServiceJobs() != null){
				for(CallServiceJob job : editCallServiceComment.getCallServiceJobs()){
					if(job.getJob().isInternal()){
						selectedInternalJob.add(job.getJob());
					} else {
						selectedExternalJob.add(job.getJob());
					}
				}
			}
		} else {
			editCallServiceComment = new CallServiceComment();
		}
	}
	
	public void coordinatorAutocompleteListener(){
		editCallServiceComment.setPhoneNumber(RequestDao.getCoorPhoneNumber(editCallServiceComment.getCoordinator()));
	}
	
	private void createFilterOptions(){
		filterOptions.clear();
		filterOptions.add(new SelectItem("", "ALL"));
		filterOptions.add(new SelectItem("NEW", "NEW"));
		filterOptions.add(new SelectItem("DONE", "DONE"));
		filterOptions.add(new SelectItem("REJECTED", "REJECTED"));
	}
	
	public void createRequestList(){
		int counter = 0;
		requestList.clear();
		requestList.addAll(RequestDao.getRequestList("callservice"));
		for(Request request : requestList){
			request.setStatusValue("callservice");
			if(request.getStatus().equals("NEW") || request.getStatus().equals("REJECTED"))
				counter++;
		}
		SessionUtils.getNotification().setCallService(counter);
	}
	
	public void saveCallService(){
		editCallServiceComment.setServiceOfficer(SessionUtils.getUserSession().getStaff());
		editCallServiceComment.setRequest(editRequest);
		if(editCallServiceComment.getId() == null){
			editCallServiceComment.setIssueDate(new Date());
			SessionUtils.getNotification().setCallService(SessionUtils.getNotification().getCallService()-1);
		}
		
		List<Job> allJob = new ArrayList<Job>();
		allJob.addAll(selectedExternalJob);
		allJob.addAll(selectedInternalJob);
		
		RequestDao.saveCallServiceComment(editRequest, editCallServiceComment, allJob);
		
		if(filtered != null){
			Map<Integer, Request> map = new HashMap<Integer, Request>();
			int index = 0;
			for(Request req : filtered){
				if(req.getId().equals(editRequest.getId())){
					req = RequestDao.findRequest(req.getId());
					req.setStatusValue("callservice");
					map.put(index, req);
				}
				index++;
			}
			for(Map.Entry<Integer, Request> entry : map.entrySet()){
				int target = entry.getKey();
				filtered.remove(target);
				filtered.add(target, entry.getValue());
			}
		}
		
		resetData();
		createRequestList();
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Your transaction was successful."));
	}
	
	private void resetData(){
		editRequest = new Request();
		editCallServiceComment = new CallServiceComment();
		selectedInternalJob.clear();
		selectedExternalJob.clear();
	}
	
	private void createWorkPlaceList(){
		if(workPlaceList != null) workPlaceList.clear();
		
		workPlaceList.addAll(GenericDao.getWorkPlaceList());
	}
	
	private void createInternalJobList(){
		if(internalJobList != null) internalJobList.clear();
		
		internalJobList.addAll(GenericDao.getInternalJobList());
	}
	
	private void createExternalJobList(){
		if(externalJobList != null) externalJobList.clear();
		
		externalJobList.addAll(GenericDao.getExternalJobList());
	}
	
	public Request getEditRequest() {
		return editRequest;
	}
	public void setEditRequest(Request editRequest) {
		this.editRequest = editRequest;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public CallServiceComment getEditCallServiceComment() {
		return editCallServiceComment;
	}

	public void setEditCallServiceComment(CallServiceComment editCallServiceComment) {
		this.editCallServiceComment = editCallServiceComment;
	}

	public List<WorkPlace> getWorkPlaceList() {
		return workPlaceList;
	}

	public void setWorkPlaceList(List<WorkPlace> workPlaceList) {
		this.workPlaceList = workPlaceList;
	}

	public List<Job> getInternalJobList() {
		return internalJobList;
	}

	public void setInternalJobList(List<Job> internalJobList) {
		this.internalJobList = internalJobList;
	}

	public List<Job> getExternalJobList() {
		return externalJobList;
	}

	public void setExternalJobList(List<Job> externalJobList) {
		this.externalJobList = externalJobList;
	}

	public List<Job> getSelectedInternalJob() {
		return selectedInternalJob;
	}

	public void setSelectedInternalJob(List<Job> selectedInternalJob) {
		this.selectedInternalJob = selectedInternalJob;
	}

	public List<Job> getSelectedExternalJob() {
		return selectedExternalJob;
	}

	public void setSelectedExternalJob(List<Job> selectedExternalJob) {
		this.selectedExternalJob = selectedExternalJob;
	}

	public List<Request> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<Request> requestList) {
		this.requestList = requestList;
	}

	public List<Request> getFiltered() {
		return filtered;
	}

	public void setFiltered(List<Request> filtered) {
		this.filtered = filtered;
	}

	public Request getViewRequest() {
		return viewRequest;
	}

	public void setViewRequest(Request viewRequest) {
		this.viewRequest = viewRequest;
	}

	public List<SelectItem> getFilterOptions() {
		return filterOptions;
	}

	public void setFilterOptions(List<SelectItem> filterOptions) {
		this.filterOptions = filterOptions;
	}
}
