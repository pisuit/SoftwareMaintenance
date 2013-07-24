package th.co.aerothai.callservice.customtype;

public enum UserRole {
	REQUESTER("REQUESTER", "Requester"),
	USER_DIRECTOR("USER_DIRECTOR", "User Director"),
	CALL_SERVICE("CALL_SERVICE", "Call Service"),
	INSPECTOR("INSPECTOR", "Inspector"),
	PROVIDER_DIRECTOR("PROVIDER_DIRECTOR", "Provider Director"),
	PROJECT_MANAGER("PROJECT_MANAGER", "Project Manager"),
	OPERATOR("OPERATOR", "Operator"),
	ADMIN("ADMIN", "Admin");
	
	private String id;
	private String value;
	
	UserRole(String aID, String aValue){
		id = aID;
		value = aValue;
	}
	
	public String getID(){
		return id;
	}
	
	public String getValue(){
		return value;
	}

	public String toString() {
		return value;
	}
	
	public static UserRole find(String aID){
		for (UserRole role : UserRole.values()) {
			if (role.id.equals(aID)){
				return role;
			}
		}
		return null;
	}
}
