package th.co.aerothai.callservice.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import th.co.aerothai.callservice.dao.GenericDao;
import th.co.aerothai.callservice.model.WorkPlace;

@FacesConverter(value="workPlaceConverter")
public class WorkPlaceConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		return GenericDao.findWorkPlace(Long.valueOf(arg2));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		if(arg2 instanceof WorkPlace){
			return ((WorkPlace)arg2).getId().toString();
		} else {
			return null;
		}
	}

}
