package th.co.aerothai.callservice.lazymodel;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import th.co.aerothai.callservice.model.Request;

public class RequestLazyModel extends LazyDataModel<Request>{
	
	private List<Request> datasource;
	
	public RequestLazyModel(List<Request> datasource){
		this.datasource = datasource;
	}
	
	@Override
	public Request getRowData(String rowKey){
		for(Request request : datasource){
			if(request.getId().equals(rowKey))
				return request;
		}
		return null;
	}
	
	@Override
	public Object getRowKey(Request request){
		return request.getId();
	}
	
//	public List<Request> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters){
//		
//	}
}
