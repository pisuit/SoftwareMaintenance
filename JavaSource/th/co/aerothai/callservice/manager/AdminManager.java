package th.co.aerothai.callservice.manager;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

import th.co.aerothai.callservice.customtype.DataStatus;
import th.co.aerothai.callservice.customtype.UserRole;
import th.co.aerothai.callservice.dao.GenericDao;
import th.co.aerothai.callservice.dao.StaffDao;
import th.co.aerothai.callservice.dao.UserDao;
import th.co.aerothai.callservice.model.Authorization;
import th.co.aerothai.callservice.model.Job;
import th.co.aerothai.callservice.model.SystemUser;
import th.co.aerothai.callservice.model.WorkPlace;
import th.co.aerothai.callservice.model.hr.Department;

@ManagedBean(name="adminManager")
@ViewScoped
public class AdminManager {
	private List<SystemUser> userList = new ArrayList<SystemUser>();
	private SystemUser editUser = new SystemUser();
	private List<UserRole> userRoleList = new ArrayList<UserRole>();
	private List<UserRole> selectedRoleList = new ArrayList<UserRole>();
	private String userDialogHeader = "New User";
	private String jobDialogHeader = "New Job";
	private List<Job> jobList = new ArrayList<Job>();
	private Job editJob = new Job();
	private List<SelectItem> deptSelectItemList = new ArrayList<SelectItem>();
	private List<WorkPlace> workPlaceList = new ArrayList<WorkPlace>();
	private WorkPlace editWorkPlace = new WorkPlace();
	private String workPlaceDialogHeader = "New Work Place";

	public AdminManager(){
		createUserList();
		createUserRoleList();
		createJobList();
		createDepartmentList();
		createWorkPlaceList();
	}
	
	private void createUserList(){
		userList.clear();
		userList.addAll(UserDao.getUserList());
	}
	
	private void createJobList(){
		jobList.clear();
		jobList.addAll(GenericDao.getJobList());
	}
	
	private void createWorkPlaceList(){
		workPlaceList.clear();
		workPlaceList.addAll(GenericDao.getWorkPlaceList());
	}
	
	private void createDepartmentList(){
		deptSelectItemList.add(new SelectItem("","Department"));
		
		for(Department department : StaffDao.getDepartments()){
			deptSelectItemList.add(new SelectItem(department.getTDEPS(), department.getTDEPS()));
		}
	}
	
	private void createUserRoleList(){
		userRoleList.clear();
		for(UserRole role : UserRole.values()){
			if(!role.equals(UserRole.REQUESTER)){
				userRoleList.add(role);
			}
		}
	}
	
	public void saveUser(){
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		if(editUser.getStaff() == null)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "User is required."));
		if(selectedRoleList.size() == 0)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Role is required."));
		
		if(msgs.size() == 0){
			UserDao.saveUser(editUser, selectedRoleList);
			createUserList();
			resetUserData();	
			RequestContext.getCurrentInstance().execute("newUserDialog.hide()");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Your transaction was successful."));
		} else {
			for(FacesMessage msg : msgs){
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}
	
	public void saveJob(){
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		if(editJob.getJobName().trim().length() == 0)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Job name is required."));
		if(editJob.getDepartment().equals(""))
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Department is required."));
		
		if(msgs.size() == 0){
			GenericDao.saveJob(editJob);
			resetJobData();
			createJobList();
			RequestContext.getCurrentInstance().execute("newJobDialog.hide()");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Your transaction was successful."));
		} else {
			for(FacesMessage msg : msgs){
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}
	
	public void saveWorkPlace(){
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		if(editWorkPlace.getName().trim().length() == 0)
			msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing !!", "Work place is required."));
		
		if(msgs.size() == 0){
			GenericDao.saveWorkPlace(editWorkPlace);
			resetWorkPlaceData();
			createWorkPlaceList();
			RequestContext.getCurrentInstance().execute("newWorkDialog.hide()");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Your transaction was successful."));
		} else {
			for(FacesMessage msg : msgs){
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}
	
	public void resetUserData(){
		userDialogHeader = "New User";
		editUser = new SystemUser();
		selectedRoleList.clear();
	}
	
	public void resetJobData(){
		jobDialogHeader = "New Job";
		editJob = new Job();
	}
	
	public void resetWorkPlaceData(){
		workPlaceDialogHeader = "New Work Place";
		editWorkPlace = new WorkPlace();
	}
	
	public void setUserData(){
		userDialogHeader = "Edit User";
		selectedRoleList.clear();
		for(Authorization auth : editUser.getRoles()){
			selectedRoleList.add(auth.getUserRole());
		}
	}
	
	public void setJobData(){
		jobDialogHeader = "Edit Job";
	}
	
	public void setWorkPlaceData(){
		workPlaceDialogHeader = "Edit Work Place";
	}
	
	public void deleteUser(){
		UserDao.deleteUser(editUser);
		editUser = new SystemUser();
		createUserList();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "User has been deleted."));
	}
	
	public void deleteJob(){
		editJob.setDataStatus(DataStatus.DELETED);
		GenericDao.saveJob(editJob);
		editJob = new Job();
		createJobList();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Job has been deleted."));
	}
	
	public void deleteWorkPlace(){
		editWorkPlace.setDataStatus(DataStatus.DELETED);
		GenericDao.saveWorkPlace(editWorkPlace);
		editWorkPlace = new WorkPlace();
		createWorkPlaceList();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Work place has been deleted."));
	}
	
	public List<SystemUser> getUserList() {
		return userList;
	}

	public void setUserList(List<SystemUser> userList) {
		this.userList = userList;
	}

	public SystemUser getEditUser() {
		return editUser;
	}

	public void setEditUser(SystemUser editUser) {
		this.editUser = editUser;
	}

	public List<UserRole> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<UserRole> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public List<UserRole> getSelectedRoleList() {
		return selectedRoleList;
	}

	public void setSelectedRoleList(List<UserRole> selectedRoleList) {
		this.selectedRoleList = selectedRoleList;
	}

	public String getUserDialogHeader() {
		return userDialogHeader;
	}

	public void setUserDialogHeader(String userDialogHeader) {
		this.userDialogHeader = userDialogHeader;
	}

	public List<Job> getJobList() {
		return jobList;
	}

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}

	public Job getEditJob() {
		return editJob;
	}

	public void setEditJob(Job editJob) {
		this.editJob = editJob;
	}

	public String getJobDialogHeader() {
		return jobDialogHeader;
	}

	public void setJobDialogHeader(String jobDialogHeader) {
		this.jobDialogHeader = jobDialogHeader;
	}

	public List<SelectItem> getDeptSelectItemList() {
		return deptSelectItemList;
	}

	public void setDeptSelectItemList(List<SelectItem> deptSelectItemList) {
		this.deptSelectItemList = deptSelectItemList;
	}

	public List<WorkPlace> getWorkPlaceList() {
		return workPlaceList;
	}

	public void setWorkPlaceList(List<WorkPlace> workPlaceList) {
		this.workPlaceList = workPlaceList;
	}

	public WorkPlace getEditWorkPlace() {
		return editWorkPlace;
	}

	public void setEditWorkPlace(WorkPlace editWorkPlace) {
		this.editWorkPlace = editWorkPlace;
	}

	public String getWorkPlaceDialogHeader() {
		return workPlaceDialogHeader;
	}

	public void setWorkPlaceDialogHeader(String workPlaceDialogHeader) {
		this.workPlaceDialogHeader = workPlaceDialogHeader;
	}

}
