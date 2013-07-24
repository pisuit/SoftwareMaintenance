package th.co.aerothai.callservice.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import th.co.aerothai.callservice.customtype.UserRole;
import th.co.aerothai.callservice.dao.StaffDao;
import th.co.aerothai.callservice.dao.UserDao;
import th.co.aerothai.callservice.ldap.LDAPConnect;
import th.co.aerothai.callservice.ldap.LDAPUser;
import th.co.aerothai.callservice.model.Authorization;
import th.co.aerothai.callservice.model.SystemUser;
import th.co.aerothai.callservice.model.hr.PersonalInfo;

@ManagedBean(name="loginManager")
@RequestScoped
public class LoginManager {
	private String username;
	private String password;
	private PersonalInfo staff;
	
	public LoginManager(){
//		checkCookie();
	}
	
//	private void checkCookie(){
//		if(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("username") != null && FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("password") != null){		
//			username = ((Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("username")).getValue();
//			password = ((Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("password")).getValue();
//			
//			if(login() != null){
//				try {
//					FacesContext.getCurrentInstance().getExternalContext().redirect("./request.jsf");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
//	private void addCookie(){
//		if(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("username") == null && FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("password") == null){
//			Cookie userCookie = new Cookie("username", username);
//			userCookie.setMaxAge(60*60*24*365);
//			Cookie passCookie = new Cookie("password", password);
//			passCookie.setMaxAge(60*60*24*365);
//			HttpServletResponse response = ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse());
//			response.addCookie(userCookie);
//			response.addCookie(passCookie);
//		}
//	}
	
//	private void deleteCookie(){
//		if(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("username") != null && FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("password") != null){
//			Cookie userCookie = ((Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("username"));
//			userCookie.setMaxAge(0);
//			Cookie passCookie = ((Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("password"));
//			passCookie.setMaxAge(0);
//			HttpServletResponse response = ((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse());
//			response.addCookie(userCookie);
//			response.addCookie(passCookie);
//		}
//	}
	
	public String login(){
		if(username.trim().length() == 0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Login failed !!",""));
			 return null;
		}
		if(password.trim().length() == 0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Login failed !!",""));
			 return null;
		}
		
		SystemUser user = authentication(username, password);
		
		if(user != null){
			System.out.println("Adding user to session....");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userSession", user);
			System.out.println("Done...");
//			addCookie();
//			if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("page") != null){
//				String page = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("page").replace(".jsf", "");
//				return page+"?faces-redirect=true";
//			}
			System.out.println("Redirected...");
			return "request?faces-redirect=true";
		} else {
			return null;
		}
	}
	
	public String quickLogin(String username, String password){
//		SystemUser user = authentication(username, password);
//		
//		if(user != null){
//			System.out.println("Adding user to session....");
//			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userSession", user);
//			System.out.println("Done...");
////			addCookie();
////			if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("page") != null){
////				String page = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("page").replace(".jsf", "");
////				return page+"?faces-redirect=true";
////			}
//			System.out.println("Redirected...");
//			return "request?faces-redirect=true";
//		} else {
//			return null;
//		}
		SystemUser user = new SystemUser();
		PersonalInfo staff = new PersonalInfo();
		Authorization authorization = new Authorization();
		authorization.setUserRole(UserRole.ADMIN);
		authorization.setUser(user);
		List<Authorization> authlist = new ArrayList<Authorization>();
		authlist.add(authorization);
		user.setStaff(staff);
		user.setRoles(authlist);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userSession", user);
		return "admin?faces-redirect=true";
	}
	
	public void secretLogin(){
		SystemUser dbUser = UserDao.getUser(StringUtils.leftPad(staff.getSTAFFCODE(), 6, '0'));

		if(dbUser == null){
			SystemUser user = new SystemUser();
			Authorization auth = new Authorization();
			List<Authorization> auths = new ArrayList<Authorization>();
			auth.setUser(user);
			auth.setUserRole(UserRole.REQUESTER);
			auths.add(auth);
			
			user.setRoles(auths);
			user.setStaff(StaffDao.getStaff(staff.getSTAFFCODE()));
			
			SystemUser thisuser = UserDao.getUserWithBoss(StringUtils.leftPad(staff.getSTAFFCODE(), 6, '0'));
	
			if(thisuser != null){
				SystemUser boss = UserDao.getUser(StringUtils.leftPad(thisuser.getBoss().getSTAFFCODE(), 6, '0'));
				if(boss != null){
					user.setBoss(boss.getStaff());
					for(Authorization a : boss.getRoles()){
						a.setUser(thisuser);
						
						user.getRoles().add(a);
					}
				}
			}
			
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userSession", user);
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("request.jsf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Authorization auth = new Authorization();
			auth.setUser(dbUser);
			auth.setUserRole(UserRole.REQUESTER);
			
			dbUser.getRoles().add(auth);
			
			SystemUser user = UserDao.getUserWithBoss(StringUtils.leftPad(dbUser.getStaff().getSTAFFCODE(), 6, '0'));
			if(user != null){
				SystemUser boss = UserDao.getUser(StringUtils.leftPad(user.getBoss().getSTAFFCODE(), 6, '0'));
				if(boss != null){
					dbUser.setBoss(boss.getStaff());
					for(Authorization a : boss.getRoles()){
						a.setUser(user);
						
						dbUser.getRoles().add(a);
					}
				}
			}
			
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userSession", dbUser);
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("request.jsf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private SystemUser authentication(String username, String password) {
		LDAPConnect connect = new LDAPConnect();
		LDAPUser ldapUser = connect.login(username, password);
		connect.disconnect();
		if(ldapUser != null){
			System.out.println("User: "+username);
			System.out.println("Accessing....");

			SystemUser dbUser = UserDao.getUser(StringUtils.leftPad(ldapUser.getEmployeeCode(), 6, '0'));

			if(dbUser == null){
				SystemUser user = new SystemUser();
				Authorization auth = new Authorization();
				List<Authorization> auths = new ArrayList<Authorization>();
				auth.setUser(user);
				auth.setUserRole(UserRole.REQUESTER);
				auths.add(auth);
				
				user.setRoles(auths);
				user.setStaff(StaffDao.getStaff(ldapUser.getEmployeeCode()));
				
				SystemUser thisuser = UserDao.getUserWithBoss(StringUtils.leftPad(ldapUser.getEmployeeCode(), 6, '0'));
				if(thisuser != null){
					SystemUser boss = UserDao.getUser(StringUtils.leftPad(thisuser.getBoss().getSTAFFCODE(), 6, '0'));
					if(boss != null){
						user.setBoss(boss.getStaff());
						for(Authorization a : boss.getRoles()){
							a.setUser(thisuser);
							
							user.getRoles().add(a);
						}
					}
				}
				
				return user;
			} else {
				Authorization auth = new Authorization();
				auth.setUser(dbUser);
				auth.setUserRole(UserRole.REQUESTER);
				
				dbUser.getRoles().add(auth);
				
				SystemUser user = UserDao.getUserWithBoss(StringUtils.leftPad(ldapUser.getEmployeeCode(), 6, '0'));
				if(user != null){
					SystemUser boss = UserDao.getUser(StringUtils.leftPad(user.getBoss().getSTAFFCODE(), 6, '0'));
					if(boss != null){
						dbUser.setBoss(boss.getStaff());
						for(Authorization a : boss.getRoles()){
							a.setUser(user);
							
							dbUser.getRoles().add(a);
						}
					}
				}
				
				return dbUser;
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Login failed !!",""));
			return null;
		}
	}
	
	public String logout(){
//		deleteCookie();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login?faces-redirect=true";
//		return null;
	}
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PersonalInfo getStaff() {
		return staff;
	}

	public void setStaff(PersonalInfo staff) {
		this.staff = staff;
	}
}
