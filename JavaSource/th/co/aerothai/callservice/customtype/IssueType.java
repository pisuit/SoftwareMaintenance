package th.co.aerothai.callservice.customtype;

public enum IssueType {
	PERSONAL("PERSONAL","Personal"),
	DEPARTMENT("DEPARTMENT","Department");
	
	private String id;
	private String value;
	
	IssueType(String aID, String aValue){
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
	
	public static IssueType find(String aID){
		for (IssueType status : IssueType.values()) {
			if (status.id.equals(aID)){
				return status;
			}
		}
		return null;
	}
}
