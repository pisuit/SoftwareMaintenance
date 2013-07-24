package th.co.aerothai.callservice.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import th.co.aerothai.callservice.customtype.UserRole;
import th.co.aerothai.callservice.dao.RequestDao;
import th.co.aerothai.callservice.dao.UserDao;
import th.co.aerothai.callservice.model.Authorization;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.SystemUser;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.security.UserSession;
import th.co.aerothai.callservice.sessionobject.Notification;
import th.co.aerothai.callservice.utils.SessionUtils;

@ManagedBean(name="menuManager")
@SessionScoped
public class MenuManager {
	private boolean requester;
	private boolean userDirector;
	private boolean callService;
	private boolean inspector;
	private boolean providerDirector;
	private boolean operator;
	private boolean admin;
	private boolean loggedIn;
	private boolean projectManager;
	private int activeTab = 0;
	private MenuModel menuModel;
	private PersonalInfo selectedStaff = null;
	private List<SystemUser> repList = new ArrayList<SystemUser>();
	private String screenHeight;
	
	public MenuManager(){
		createRepList();
	}
	
	public void setHeight(){
		screenHeight = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("height");
	}
	
	public void saveRep(){
		SystemUser user = new SystemUser();
		user.setBoss(SessionUtils.getUserSession().getStaff());
		user.setStaff(selectedStaff);
		repList.add(user);
		UserDao.saveUser(user, null);
		selectedStaff = null;
	}
	
	private void createRepList(){
		repList.addAll(UserDao.getRepList());
	}
		
	public void deleteRep(SystemUser staff){
		UserDao.deleteUser(staff);
		repList.remove(staff);
	}
	
	public void updateRep(SystemUser staff){
		UserDao.saveUser(staff, null);
	}
	
	public boolean isRequester() {
		if(SessionUtils.getUserSession() != null){
			for(Authorization auth : SessionUtils.getUserSession().getRoles()){
				if(auth.getUserRole().equals(UserRole.REQUESTER))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
	public boolean isUserDirector() {
		if(SessionUtils.getUserSession() != null){
			for(Authorization auth : SessionUtils.getUserSession().getRoles()){
				if(auth.getUserRole().equals(UserRole.USER_DIRECTOR))
//					userDirectorNotify = RequestDao.countNotify("userdirector");
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
	public boolean isCallService() {
		if(SessionUtils.getUserSession() != null){
			for(Authorization auth : SessionUtils.getUserSession().getRoles()){
				if(auth.getUserRole().equals(UserRole.CALL_SERVICE))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
	public boolean isInspector() {
		if(SessionUtils.getUserSession() != null){
			for(Authorization auth : SessionUtils.getUserSession().getRoles()){
				if(auth.getUserRole().equals(UserRole.INSPECTOR))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
	public boolean isProviderDirector() {
		if(SessionUtils.getUserSession() != null){
			for(Authorization auth : SessionUtils.getUserSession().getRoles()){
				if(auth.getUserRole().equals(UserRole.PROVIDER_DIRECTOR))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
	public boolean isOperator() {
		if(SessionUtils.getUserSession() != null){
			for(Authorization auth : SessionUtils.getUserSession().getRoles()){
				if(auth.getUserRole().equals(UserRole.OPERATOR))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
	public boolean isAdmin() {
		if(SessionUtils.getUserSession() != null){
			for(Authorization auth : SessionUtils.getUserSession().getRoles()){
				if(auth.getUserRole().equals(UserRole.ADMIN))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
	public boolean isProjectManager() {
		if(SessionUtils.getUserSession() != null){
			for(Authorization auth : SessionUtils.getUserSession().getRoles()){
				if(auth.getUserRole().equals(UserRole.PROJECT_MANAGER))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
	public boolean isLoggedIn() {
		return SessionUtils.getUserSession() != null;
	}
	public int getActiveTab() {
		return activeTab;
	}
	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}
	
	public void listener(String page, int index){
		activeTab = index;
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(page+".jsf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initNotification(){	
		if(SessionUtils.getNotification() == null){
			Notification notification = new Notification();
			
			if(isRequester()){
				List<Request> reqs = RequestDao.getRequestList("requesterp");
				if(reqs != null){
					int counter = 0;
					for(Request req : reqs){
						req.setStatusValue("requester");
						if(req.getStatus().equals("COMPLETED"))
							counter++;
					}
					notification.setRequester(counter);
				}
			}
			
			if(isUserDirector()){
				List<Request> reqs = RequestDao.getRequestList("userdirector");
				if(reqs != null){
					int counter = 0;
					for(Request req : reqs){
						req.setStatusValue("userdirector");
						if(req.getStatus().equals("NEW"))
							counter++;
					}
					notification.setUserDirector(counter);
				}
			}
			
			if(isCallService()){
				List<Request> reqs = RequestDao.getRequestList("callservice");
				if(reqs != null){
					int counter = 0;
					for(Request req : reqs){
						req.setStatusValue("callservice");
						if(req.getStatus().equals("NEW") || req.getStatus().equals("REJECTED"))
							counter++;
					}
					notification.setCallService(counter);
				}
			}
			
			if(isProjectManager()){
				List<Request> reqs = RequestDao.getRequestList("projectmanager");
				if(reqs != null){
					int counter = 0;
					for(Request req : reqs){
						req.setStatusValue("projectmanager");
						if(req.getStatus().equals("NEW"))
							counter++;
					}
					notification.setProjectManager(counter);
				}
			}
			
			if(isProviderDirector()){
				List<Request> reqs = RequestDao.getRequestList("providerdirector");
				if(reqs != null){
					int counter = 0;
					for(Request req : reqs){
						req.setStatusValue("director");
						if(req.getStatus().equals("NEW") || req.getStatus().equals("WAITING"))
							counter++;
					}
					notification.setProviderDirector(counter);
				}
			}
			
			if(isOperator()){
				List<Request> reqs = RequestDao.getRequestList("operator");
				if(reqs != null){
					int counter = 0;
					for(Request req : reqs){
						req.setStatusValue("operator");
						if(req.getStatus().equals("NEW") || req.getStatus().equals("REJECTED"))
							counter++;
					}
					notification.setOperator(counter);
				}
			}
			
			if((isInspector())){
				List<Request> reqs = RequestDao.getRequestList("inspector");
				if(reqs != null){
					int counter = 0;
					for(Request req : reqs){
						req.setStatusValue("inspector");
						if(req.getStatus().equals("NEW"))
							counter++;
					}
					notification.setInspector(counter);
				}
			}
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("notification", notification);
		}
	}
	
	public MenuModel getMenuModel() {
		MenuModel model = new DefaultMenuModel();
		int index = 0;
		initNotification();
		
		if(isRequester()){
			MenuItem item = new MenuItem();
			if(SessionUtils.getNotification().getRequester() > 0){
				item.setValue("Requester "+"("+SessionUtils.getNotification().getRequester()+")");	
			} else {
				item.setValue("Requester");
			}		
			item.setIcon("ui-icon-person");	
			item.addActionListener(createMethodExpressionActionListener("#{menuManager.listener('request',"+index+")}"));
			model.addMenuItem(item);
			index++;
		}
		
		if(isUserDirector()){	
			MenuItem item = new MenuItem();
			if(SessionUtils.getNotification().getUserDirector() > 0){
				item.setValue("User Director "+"("+SessionUtils.getNotification().getUserDirector()+")");
			} else {
				item.setValue("User Director");
			}
			item.setIcon("ui-icon-check");	
			item.addActionListener(createMethodExpressionActionListener("#{menuManager.listener('userdirector',"+index+")}"));
			model.addMenuItem(item);
			index++;
		}
		
		if(isCallService()){
			MenuItem item = new MenuItem();
			if(SessionUtils.getNotification().getCallService() > 0){
				item.setValue("Call Service "+"("+SessionUtils.getNotification().getCallService()+")");
			} else {
				item.setValue("Call Service");
			}
			item.setIcon("ui-icon-transferthick-e-w");	
			item.addActionListener(createMethodExpressionActionListener("#{menuManager.listener('callservice',"+index+")}"));
			model.addMenuItem(item);
			index++;
		}
		
		if(isProjectManager()){
			MenuItem item = new MenuItem();
			if(SessionUtils.getNotification().getProjectManager() > 0){
				item.setValue("Project Manager "+"("+SessionUtils.getNotification().getProjectManager()+")");
			} else {
				item.setValue("Project Manager");
			}
			item.setIcon("ui-icon-comment");	
			item.addActionListener(createMethodExpressionActionListener("#{menuManager.listener('projectmanager',"+index+")}"));
			model.addMenuItem(item);
			index++;
		}
		
		if(isProviderDirector()){
			MenuItem item = new MenuItem();
			if(SessionUtils.getNotification().getProviderDirector() > 0){
				item.setValue("Provider Director "+"("+SessionUtils.getNotification().getProviderDirector()+")");
			} else {
				item.setValue("Provider Director");
			}
			item.setIcon("ui-icon-check");	
			item.addActionListener(createMethodExpressionActionListener("#{menuManager.listener('providerdirector',"+index+")}"));
			model.addMenuItem(item);
			index++;
		}
		
		if(isOperator()){
			MenuItem item = new MenuItem();
			if(SessionUtils.getNotification().getOperator() > 0){
				item.setValue("Operator "+"("+SessionUtils.getNotification().getOperator()+")");
			} else {
				item.setValue("Operator");
			}
			item.setIcon("ui-icon-wrench");	
			item.addActionListener(createMethodExpressionActionListener("#{menuManager.listener('operator',"+index+")}"));
			model.addMenuItem(item);
			index++;
		}
		
		if(isInspector()){
			MenuItem item = new MenuItem();
			if(SessionUtils.getNotification().getInspector() > 0){
				item.setValue("Inspector "+"("+SessionUtils.getNotification().getInspector()+")");
			} else {
				item.setValue("Inspector");
			}
			item.setIcon("ui-icon-search");	
			item.addActionListener(createMethodExpressionActionListener("#{menuManager.listener('inspector',"+index+")}"));
			model.addMenuItem(item);
			index++;
		}
		
		if(isAdmin()){
			MenuItem item = new MenuItem();
			item.setValue("Admin");
			item.setIcon("ui-icon-star");	
			item.addActionListener(createMethodExpressionActionListener("#{menuManager.listener('admin',"+index+")}"));
			model.addMenuItem(item);
			index++;
		}
		
		return model;
	}
	
	public MethodExpressionActionListener createMethodExpressionActionListener(String exp){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		MethodExpressionActionListener actionListener = new MethodExpressionActionListener(expressionFactory.createMethodExpression(facesContext.getELContext(), exp, null, new Class[] {ActionEvent.class}));
		return actionListener;
	}
	public PersonalInfo getSelectedStaff() {
		return selectedStaff;
	}
	public void setSelectedStaff(PersonalInfo selectedStaff) {
		this.selectedStaff = selectedStaff;
	}
	public List<SystemUser> getRepList() {
		return repList;
	}
	public void setRepList(List<SystemUser> repList) {
		this.repList = repList;
	}

	public String getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(String screenHeight) {
		this.screenHeight = screenHeight;
	}


	
}
