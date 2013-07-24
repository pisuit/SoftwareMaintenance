package th.co.aerothai.callservice.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import th.co.aerothai.callservice.dao.StaffDao;
import th.co.aerothai.callservice.model.hr.Department;

@FacesConverter(value="departmentConverter")
public class DepartmentConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		return StaffDao.findDepartment(Long.valueOf(arg2));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		return ((Department)arg2).getSN().toString();
	}

}
