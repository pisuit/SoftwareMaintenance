package th.co.aerothai.callservice.customtype;

public enum BudgetType {
	CAPITAL("CAPITAL","Capital"),
	OPERATING("OPERATING","Operating");
	
	private String id;
	private String value;
	
	BudgetType(String aID, String aValue){
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
	
	public static BudgetType find(String aID){
		for (BudgetType status : BudgetType.values()) {
			if (status.id.equals(aID)){
				return status;
			}
		}
		return null;
	}
}
