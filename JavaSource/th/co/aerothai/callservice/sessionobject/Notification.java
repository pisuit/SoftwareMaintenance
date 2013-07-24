package th.co.aerothai.callservice.sessionobject;

public class Notification {
	private int requester = 0;
	private int userDirector = 0;
	private int callService = 0;
	private int projectManager = 0;
	private int providerDirector = 0;
	private int operator = 0;
	private int inspector = 0;
	
	public int getUserDirector() {
		return userDirector;
	}
	public void setUserDirector(int userDirector) {
		this.userDirector = userDirector;
	}
	public int getCallService() {
		return callService;
	}
	public void setCallService(int callService) {
		this.callService = callService;
	}
	public int getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(int projectManager) {
		this.projectManager = projectManager;
	}
	public int getProviderDirector() {
		return providerDirector;
	}
	public void setProviderDirector(int providerDirector) {
		this.providerDirector = providerDirector;
	}
	public int getOperator() {
		return operator;
	}
	public void setOperator(int operator) {
		this.operator = operator;
	}
	public int getInspector() {
		return inspector;
	}
	public void setInspector(int inspector) {
		this.inspector = inspector;
	}
	public int getRequester() {
		return requester;
	}
	public void setRequester(int requester) {
		this.requester = requester;
	}
}
