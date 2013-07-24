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

import com.sun.org.apache.bcel.internal.generic.NEW;

import th.co.aerothai.callservice.customtype.ApprovalType;
import th.co.aerothai.callservice.dao.RequestDao;
import th.co.aerothai.callservice.model.ProviderDirectorComment;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.UserDirectorComment;
import th.co.aerothai.callservice.utils.SessionUtils;

@ManagedBean(name="directorManager")
@ViewScoped
public class DirectorManager {
	private Request editRequest = new Request();
	private ProviderDirectorComment editProviderDirectorComment = new ProviderDirectorComment();
	private String id;
	private List<Request> requestList = new ArrayList<Request>();
	private Request viewRequest = new Request();
	private List<Request> filtered;
	private List<SelectItem> filterOptions = new ArrayList<SelectItem>();
	
	public DirectorManager(){
		createRequestList();
		createFilterOptions();
	}
	
	public void createRequestList(){
		int counter = 0;
		requestList.clear();
		requestList.addAll(RequestDao.getRequestList("providerdirector"));
		for(Request request : requestList){
			request.setStatusValue("director");
			if(request.getStatus().equals("NEW") || request.getStatus().equals("WAITING"))
				counter++;
		}
		SessionUtils.getNotification().setProviderDirector(counter);
	}
	
	public void setData(){
		if(editRequest.getProviderDirectorComment() != null){
			editProviderDirectorComment = editRequest.getProviderDirectorComment();
		} else {
			editProviderDirectorComment = new ProviderDirectorComment();
		}
	}
	
	private void createFilterOptions(){
		filterOptions.clear();
		filterOptions.add(new SelectItem("", "ALL"));
		filterOptions.add(new SelectItem("NEW", "NEW"));
		filterOptions.add(new SelectItem("APPROVED", "APPROVED"));
		filterOptions.add(new SelectItem("DECLINED", "DECLINED"));
		filterOptions.add(new SelectItem("ACCEPTED", "ACCEPTED"));
		filterOptions.add(new SelectItem("REJECTED", "REJECTED"));
		filterOptions.add(new SelectItem("WAITING", "WAITING"));
	}
	
	public void approve(boolean approval){
		if(editProviderDirectorComment.getApprovalType().equals(ApprovalType.ACCEPTED)){
			SessionUtils.getNotification().setProviderDirector(SessionUtils.getNotification().getProviderDirector()-1);
		}
		
		if(approval) {
			editProviderDirectorComment.setApprovalType(ApprovalType.APPROVED);
		} else {
			editProviderDirectorComment.setApprovalType(ApprovalType.DECLINED);
		}
				
		editProviderDirectorComment.setIssueDate(new Date());
		
		editProviderDirectorComment.setDirector(SessionUtils.getUserSession().getStaff());
		editProviderDirectorComment.setRequest(editRequest);
		RequestDao.saveProviderDirectorComment(editRequest, editProviderDirectorComment);
		createRequestList();
		
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
					req.setStatusValue("director");
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
	}

	public void accept(boolean accept){
		if(accept){
			if(editRequest.getProviderDirectorComment() != null){
				editProviderDirectorComment = editRequest.getProviderDirectorComment();
			} else {
				editProviderDirectorComment = new ProviderDirectorComment();
			}
			editProviderDirectorComment.setApprovalType(ApprovalType.ACCEPTED);
		} else {
			editProviderDirectorComment.setApprovalType(ApprovalType.REJECTED);
		}
		
		if(editRequest.getProviderDirectorComment() == null){
			SessionUtils.getNotification().setProviderDirector(SessionUtils.getNotification().getProviderDirector()-1);
		}
		
		editProviderDirectorComment.setIssueDate(new Date());
		editProviderDirectorComment.setDirector(SessionUtils.getUserSession().getStaff());
		editProviderDirectorComment.setRequest(editRequest);
		RequestDao.saveProviderDirectorComment(editRequest, editProviderDirectorComment);
		createRequestList();
		
		if(accept){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Request #"+editRequest.getRequestNumber()+" was accepted."));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Request #"+editRequest.getRequestNumber()+" was rejected."));			
		}
		
		if(filtered != null){
			Map<Integer, Request> map = new HashMap<Integer, Request>();
			int index = 0;
			for(Request req : filtered){
				if(req.getId().equals(editRequest.getId())){
					req = RequestDao.findRequest(req.getId());
					req.setStatusValue("director");
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
	}
	
	private void resetData(){
		editRequest = new Request();
		editProviderDirectorComment = new ProviderDirectorComment();
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

	public ProviderDirectorComment getEditProviderDirectorComment() {
		return editProviderDirectorComment;
	}

	public void setEditProviderDirectorComment(
			ProviderDirectorComment editProviderDirectorComment) {
		this.editProviderDirectorComment = editProviderDirectorComment;
	}

	public List<Request> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<Request> requestList) {
		this.requestList = requestList;
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

	public List<SelectItem> getFilterOptions() {
		return filterOptions;
	}

	public void setFilterOptions(List<SelectItem> filterOptions) {
		this.filterOptions = filterOptions;
	}
	
	
}
