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

import th.co.aerothai.callservice.customtype.ProjectType;
import th.co.aerothai.callservice.dao.GenericDao;
import th.co.aerothai.callservice.dao.RequestDao;
import th.co.aerothai.callservice.model.AssignedOperator;
import th.co.aerothai.callservice.model.ProjectManagerComment;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.utils.SessionUtils;

@ManagedBean(name="projectManManager")
@ViewScoped
public class ProjectManManager {
	private Request editRequest = new Request();
	private String id;
	private List<ProjectType> projectTypes = new ArrayList<ProjectType>();
	private ProjectManagerComment editProjectManComment = new ProjectManagerComment();
	private List<Request> requestList = new ArrayList<Request>();
	private Request viewRequest = new Request();
	private List<SelectItem> filterOptions = new ArrayList<SelectItem>();
	private List<Request> filtered;
	private List<PersonalInfo> selectedOperators = new ArrayList<PersonalInfo>();
	
	public ProjectManManager(){
		createProjectTypeList();
		createRequestList();
		createFilterOptions();
	}
	
	public void createRequestList(){
		int counter = 0;
		requestList.clear();
		requestList.addAll(RequestDao.getRequestList("projectmanager"));
		for(Request request : requestList){
			request.setStatusValue("projectmanager");
			if(request.getStatus().equals("NEW"))
				counter++;
		}
		SessionUtils.getNotification().setProjectManager(counter);
	}
	
	private void createFilterOptions(){
		filterOptions.clear();
		filterOptions.add(new SelectItem("", "ALL"));
		filterOptions.add(new SelectItem("NEW", "NEW"));
		filterOptions.add(new SelectItem("DONE", "DONE"));
	}
	
	private void createProjectTypeList(){
		if(projectTypes != null) projectTypes.clear();
		for(ProjectType type : ProjectType.values()){
			projectTypes.add(type);
		}
	}
	
	public void setData(){
		selectedOperators.clear();
		if(editRequest.getProjectManagerComment() != null){
			editProjectManComment = editRequest.getProjectManagerComment();
			if(editProjectManComment.getOperators() != null){
				for(AssignedOperator operator : editProjectManComment.getOperators()){
					selectedOperators.add(operator.getOperator());
				}
			}
		} else {
			editProjectManComment = new ProjectManagerComment();
		}
	}
	
	public void saveProjectManagerComment(){
		editProjectManComment.setManager(SessionUtils.getUserSession().getStaff());
		editProjectManComment.setRequest(editRequest);
		if(editProjectManComment.getId() == null){
			editProjectManComment.setIssueDate(new Date());
			SessionUtils.getNotification().setProjectManager(SessionUtils.getNotification().getProjectManager()-1);
		}
		
		RequestDao.saveProjectManagerComment(editRequest, editProjectManComment, selectedOperators);

		if(filtered != null){
			Map<Integer, Request> map = new HashMap<Integer, Request>();
			int index = 0;
			for(Request req : filtered){
				if(req.getId().equals(editRequest.getId())){
					req = RequestDao.findRequest(req.getId());
					req.setStatusValue("projectmanager");
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
		editProjectManComment = new ProjectManagerComment();
		editRequest = new Request();
		selectedOperators.clear();
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

	public List<ProjectType> getProjectTypes() {
		return projectTypes;
	}

	public void setProjectTypes(List<ProjectType> projectTypes) {
		this.projectTypes = projectTypes;
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

	public List<SelectItem> getFilterOptions() {
		return filterOptions;
	}

	public void setFilterOptions(List<SelectItem> filterOptions) {
		this.filterOptions = filterOptions;
	}

	public List<Request> getFiltered() {
		return filtered;
	}

	public void setFiltered(List<Request> filtered) {
		this.filtered = filtered;
	}

	public ProjectManagerComment getEditProjectManComment() {
		return editProjectManComment;
	}

	public void setEditProjectManComment(ProjectManagerComment editProjectManComment) {
		this.editProjectManComment = editProjectManComment;
	}

	public List<PersonalInfo> getSelectedOperators() {
		return selectedOperators;
	}

	public void setSelectedOperators(List<PersonalInfo> selectedOperators) {
		this.selectedOperators = selectedOperators;
	}
}
