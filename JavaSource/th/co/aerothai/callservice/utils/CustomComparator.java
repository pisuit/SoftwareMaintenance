package th.co.aerothai.callservice.utils;

import java.util.Comparator;

import th.co.aerothai.callservice.model.RequestLog;

public class CustomComparator implements Comparator<RequestLog>{
	@Override
    public int compare(RequestLog o1, RequestLog o2) {
        return o2.getIssueTime().compareTo(o1.getIssueTime());
    }
}
