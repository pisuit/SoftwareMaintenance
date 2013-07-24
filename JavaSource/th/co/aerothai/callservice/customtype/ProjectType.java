package th.co.aerothai.callservice.customtype;

public enum ProjectType {
	CANNOT("CANNOT", "Cannot Operate"),
	NEW("NEW", "New Project"),
	IMPROVE("IMPROVE", "Improvement Project"),
	CUST_CARE("CUST_CARE", "Customer Care");
	
	private String id;
	private String value;
	
	ProjectType(String aID, String aValue){
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
	
	public static ProjectType find(String aID){
		for (ProjectType type : ProjectType.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}
}
