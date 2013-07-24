package th.co.aerothai.callservice.customtype;

public enum StateType {
	NEW_P("NEW_P","1/6"),
	NEW_D("NEW_D","1/7"),
	DU_APPROVED("DU_APPROVED","2/7"),
	CALL_SERVICE_P("CALL_SERVICE_P","2/6"),
	CALL_SERVICE_D("CALL_SERVICE_D","3/7"),
	PM_P("PM_P","3/6"),
	PM_D("PM_D","4/7"),
	D_APPROVED_P("D_APPROVED_P","4/6"),
	D_APPROVED_D("D_APPROVED_D","5/7"),
	OPERATE_P("OPERATE_P","5/6"),
	OPERATE_D("OPERATE_D","6/7"),
	INSPECT_P("INSPECT_P","6/6"),
	INSPECT_D("INSPECT_D","7/7");
	
	private String id;
	private String value;
	
	StateType(String aID, String aValue){
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
	
	public static StateType find(String aID){
		for (StateType status : StateType.values()) {
			if (status.id.equals(aID)){
				return status;
			}
		}
		return null;
	}
}
