package th.co.aerothai.callservice.customtype;

public enum ApprovalType {
	APPROVED("APPROVED", "Approved"),
	DECLINED("DECLINED", "Declined"),
	ACCEPTED("ACCEPTED", "Accepted"),
	REJECTED("REJECTED", "Rejected"),
	READ("READ", "Read"),
	UNREAD("UNREAD", "Unread"),
	RECONSIDER("RECONSIDER", "Reconsider");
	
	private String id;
	private String value;
	
	ApprovalType(String aID, String aValue){
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
	
	public static ApprovalType find(String aID){
		for (ApprovalType type : ApprovalType.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}
}
