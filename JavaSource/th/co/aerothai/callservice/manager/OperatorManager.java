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

import th.co.aerothai.callservice.dao.RequestDao;
import th.co.aerothai.callservice.model.ProjectManagerComment;
import th.co.aerothai.callservice.model.OperatorComment;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.utils.SessionUtils;

@ManagedBean(name="operatorManager")
@ViewScoped
public class OperatorManager {
	private Request editRequest = new Request();
	private OperatorComment editOperatorComment = new OperatorComment();
	private String id;
	private List<Request> requestList = new ArrayList<Request>();
	private List<SelectItem> filterOptions = new ArrayList<SelectItem>();
	private Request viewRequest = new Request();
	private List<Request> filtered;
	
	public OperatorManager(){
		createRequestList();
		createFilterOptions();
	}
	
	public void saveOperatorComment(){
		editOperatorComment.setOperator(SessionUtils.getUserSession().getStaff());
		editOperatorComment.setRequest(editRequest);
		if(editOperatorComment.getId() == null){
			editOperatorComment.setIssueDate(new Date());
			SessionUtils.getNotification().setOperator(SessionUtils.getNotification().getOperator()-1);
		}
		RequestDao.saveOperatorComment(editRequest, editOperatorComment);
		
		if(filtered != null){
			Map<Integer, Request> map = new HashMap<Integer, Request>();
			int index = 0;
			for(Request req : filtered){
				if(req.getId().equals(editRequest.getId())){
					req = RequestDao.findRequest(req.getId());
					req.setStatusValue("operator");
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
	
	public void setData(){
		if(editRequest.getOperatorComment() != null){
			editOperatorComment = editRequest.getOperatorComment();
		} else {
			editOperatorComment = new OperatorComment();
		}
	}
	
	public void createRequestList(){
		int counter = 0;
		requestList.clear();
		requestList.addAll(RequestDao.getRequestList("operator"));
		for(Request request : requestList){
			request.setStatusValue("operator");
			request.setProjectManagerComment(RequestDao.getProjectManagerComment(request));
			if(request.getStatus().equals("NEW") || request.getStatus().equals("REJECTED"))
				counter++;
		}
		SessionUtils.getNotification().setOperator(counter);
	}
	
	private void createFilterOptions(){
		filterOptions.clear();
		filterOptions.add(new SelectItem("", "ALL"));
		filterOptions.add(new SelectItem("NEW", "NEW"));
		filterOptions.add(new SelectItem("DONE", "DONE"));
		filterOptions.add(new SelectItem("REJECTED", "REJECTED"));
	}
	
	private void resetData(){
		editOperatorComment = new OperatorComment();
		editRequest = new Request();
	}
	
	public Request getEditRequest() {
		return editRequest;
	}
	public void setEditRequest(Request editRequest) {
		this.editRequest = editRequest;
	}
	public OperatorComment getEditOperatorComment() {
		return editOperatorComment;
	}
	public void setEditOperatorComment(OperatorComment editOperatorComment) {
		this.editOperatorComment = editOperatorComment;
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

	public List<Request> getFiltered() {
		return filtered;
	}

	public void setFiltered(List<Request> filtered) {
		this.filtered = filtered;
	}
}
