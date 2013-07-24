package th.co.aerothai.callservice.customfunction;

import java.util.Collection;
import java.util.Date;

import th.co.aerothai.callservice.model.Authorization;

public final class Functions {
	private Functions(){
		
	}
	
	public static boolean containAuthorize(Collection<Authorization> authorizations, String role){
		for(Authorization authorization : authorizations){
			if(authorization.getUserRole().getID().equals(role)){
				return true;
			}		
		}
		return false;
	}
	
	public static boolean compareDate(Date end, Date today){
		if(end.compareTo(today) == 0){
			return false;
		} else if(end.compareTo(today) == -1){
			return false;
		} else {
			return true;
		}
	}
}
