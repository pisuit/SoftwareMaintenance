package th.co.aerothai.callservice.converter;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import th.co.aerothai.callservice.customtype.UserRole;

@FacesConverter("userRoleConverter")
public class UserRoleConverter extends EnumConverter{
	public UserRoleConverter(){
		super(UserRole.class);
	}
}
