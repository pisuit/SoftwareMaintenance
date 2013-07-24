package th.co.aerothai.callservice.utils;

import javax.faces.context.FacesContext;

import th.co.aerothai.callservice.model.SystemUser;
import th.co.aerothai.callservice.security.UserSession;
import th.co.aerothai.callservice.sessionobject.Notification;

public abstract class SessionUtils {
	public static SystemUser getUserSession(){
		return (SystemUser) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSession");
	}
	
	public static Notification getNotification(){
		return (Notification) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("notification");
	}
}
