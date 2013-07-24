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

import th.co.aerothai.callservice.customtype.ApprovalType;
import th.co.aerothai.callservice.dao.RequestDao;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.UserDirectorComment;
import th.co.aerothai.callservice.utils.SessionUtils;

@ManagedBean(name="userDirectorManager")
@ViewScoped
public class UserDirectorManager {
	private Request editRequest = new Request();
	private UserDirectorComment editDirectorComment = new UserDirectorComment();
	private String id;
	private List<Request> requestList = new ArrayList<Request>();
	private List<Request> filtered;
	private Request viewRequest;
	private List<SelectItem> filterOptions = new ArrayList<SelectItem>();
	
	public UserDirectorManager(){
		createRequestList();
		createFilterOptions();
	}
	
	private void createFilterOptions(){
		filterOptions.clear();
		filterOptions.add(new SelectItem("", "ALL"));
		filterOptions.add(new SelectItem("NEW", "NEW"));
		filterOptions.add(new SelectItem("APPROVED", "APPROVED"));
		filterOptions.add(new SelectItem("DECLINED", "DECLINED"));
	}
	
	public void createRequestList(){
		int counter = 0;
		requestList.clear();
		requestList.addAll(RequestDao.getRequestList("userdirector"));
		for(Request request : requestList){
			request.setStatusValue("userdirector");
			if(request.getStatus().equals("NEW"))
				counter++;
		}
		SessionUtils.getNotification().setUserDirector(counter);
	}
	
	public void setData(){
		if(editRequest.getUserDirectorComment() != null){
			editDirectorComment = editRequest.getUserDirectorComment();
		} else {
			editDirectorComment = new UserDirectorComment();
		}
	}
	
	public void approve(boolean approval){
		editDirectorComment.setIssueDate(new Date());
		
		if(editDirectorComment.getId() == null){
			SessionUtils.getNotification().setUserDirector(SessionUtils.getNotification().getUserDirector()-1);
		}
		
		if(approval) {
			editDirectorComment.setApprovalType(ApprovalType.APPROVED);
		} else {
			editDirectorComment.setApprovalType(ApprovalType.DECLINED);
		}
		
		editDirectorComment.setDirector(SessionUtils.getUserSession().getStaff());
		editDirectorComment.setRequest(editRequest);
		RequestDao.saveUserDirectorComment(editRequest, editDirectorComment);	
	
		if(approval){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Request #"+editRequest.getRequestNumber()+" was approved."));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Request #"+editRequest.getRequestNumber()+" was declined."));
		}
		
		if(filtered != null){
			Map<Integer, Request> map = new HashMap<Integer, Request>();
			int index = 0;
			for(Request req : filtered){
				if(req.getId().equals(editRequest.getId())){
					req = RequestDao.findRequest(req.getId());
					req.setStatusValue("userdirector");
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
	}
	
	private void resetData(){
		editRequest = new Request();
		editDirectorComment = new UserDirectorComment();
	}
	
	public Request getEditRequest() {
		return editRequest;
	}
	public void setEditRequest(Request editRequest) {
		this.editRequest = editRequest;
	}
	public UserDirectorComment getEditDirectorComment() {
		return editDirectorComment;
	}
	public void setEditDirectorComment(UserDirectorComment editDirectorComment) {
		this.editDirectorComment = editDirectorComment;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
