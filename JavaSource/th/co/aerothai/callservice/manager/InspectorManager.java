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
import th.co.aerothai.callservice.model.InspectorComment;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.UserDirectorComment;
import th.co.aerothai.callservice.utils.SessionUtils;

@ManagedBean(name="inspectorManager")
@ViewScoped
public class InspectorManager {
	private Request editRequest = new Request();
	private InspectorComment editInspectorComment = new InspectorComment();
	private List<Request> requestList = new ArrayList<Request>();
	private List<Request> filtered;
	private List<SelectItem> filterOptions = new ArrayList<SelectItem>();
	private Request viewRequest = new Request();
	private List<Request> tempList = new ArrayList<Request>();
	private boolean onlyMarked = false;
	
	public InspectorManager(){
		createRequestList();
		createFilterOptions();
	}
	
	public void createRequestList(){
		int counter = 0;
		requestList.clear();
		requestList.addAll(RequestDao.getRequestList("inspector"));
		for(Request request : requestList){
			request.setStatusValue("inspector");
			if(request.getStatus().equals("NEW"))
				counter++;
		}
		SessionUtils.getNotification().setInspector(counter);
	}
	
	public void onlyMarkedListener(){
		if(onlyMarked){
			tempList.clear();
			tempList.addAll(requestList);
			
			List<Request> temp = new ArrayList<Request>();
			temp.addAll(requestList);
			
			for(Request req : temp){
				if(!req.isMarked()){
					requestList.remove(req);
				}
			}
		} else {
			requestList.clear();
			requestList.addAll(tempList);
		}
	}
	
	public void setData(){
		if(editRequest.getInspectorComment() != null){
			editInspectorComment = editRequest.getInspectorComment();
		} else {
			editInspectorComment = new InspectorComment();
		}
	}
	
	private void createFilterOptions(){
		filterOptions.clear();
		filterOptions.add(new SelectItem("", "ALL"));
		filterOptions.add(new SelectItem("NEW", "NEW"));
		filterOptions.add(new SelectItem("ACCEPTED", "ACCEPTED"));
		filterOptions.add(new SelectItem("REJECTED", "REJECTED"));
	}
	
	public void accept(boolean accept){
		if(editInspectorComment.getId() == null){
			SessionUtils.getNotification().setInspector(SessionUtils.getNotification().getInspector()-1);
		}
		
		if(accept){
			editInspectorComment.setApprovalType(ApprovalType.ACCEPTED);
		} else {
			editInspectorComment.setApprovalType(ApprovalType.REJECTED);
		}
		editInspectorComment.setIssueDate(new Date());
		editInspectorComment.setRequest(editRequest);
		editInspectorComment.setInspector(SessionUtils.getUserSession().getStaff());
		RequestDao.saveInspectorComment(editRequest, editInspectorComment);
			
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
					req.setStatusValue("inspector");
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
		
		createRequestList();
		resetData();
	}
	
	public void markInterest(Request req){
		RequestDao.saveRequest(req);
	}
	
	private void resetData(){
		editRequest = new Request();
		editInspectorComment = new InspectorComment();
	}
	
	
	public Request getEditRequest() {
		return editRequest;
	}
	public void setEditRequest(Request editRequest) {
		this.editRequest = editRequest;
	}
	public InspectorComment getEditInspectorComment() {
		return editInspectorComment;
	}
	public void setEditInspectorComment(InspectorComment editInspectorComment) {
		this.editInspectorComment = editInspectorComment;
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
	public List<SelectItem> getFilterOptions() {
		return filterOptions;
	}
	public void setFilterOptions(List<SelectItem> filterOptions) {
		this.filterOptions = filterOptions;
	}

	public Request getViewRequest() {
		return viewRequest;
	}

	public void setViewRequest(Request viewRequest) {
		this.viewRequest = viewRequest;
	}

	public boolean isOnlyMarked() {
		return onlyMarked;
	}

	public void setOnlyMarked(boolean onlyMarked) {
		this.onlyMarked = onlyMarked;
	}
	
}
