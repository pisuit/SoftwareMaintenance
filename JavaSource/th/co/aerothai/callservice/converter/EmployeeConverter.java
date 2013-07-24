package th.co.aerothai.callservice.converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import th.co.aerothai.callservice.autocomplete.EmployeeAutoComplete;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.model.hr.Photo;
import th.co.aerothai.callservice.utils.HibernateUtil;

@FacesConverter(value="employeeConverter")
public class EmployeeConverter implements Converter {	
	public EmployeeConverter(){	

	}
	
	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submitedValue) {
		// TODO Auto-generated method stub	
		if(submitedValue.trim().equals("")){
			return null;
		} else {
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = null;
			Transaction tx = null;
			
			try {
				session = sf.openSession();
				tx = session.beginTransaction();
				
				PersonalInfo person = (PersonalInfo) session.createQuery(
						"SELECT distinct personal " +
						"FROM PersonalInfo personal " +
						"left join fetch personal.employeeInfos emp " +
						"WHERE emp != null " +
						"and personal.STAFFCODE = :pstaffcode")
						.setParameter("pstaffcode", submitedValue)
						.uniqueResult();
				
				tx.commit();
				return person;
			} catch (Exception e) {
				e.printStackTrace();
				if (tx != null) {
					tx.rollback();
				}
				return null;
			} finally {
				session.clear();
				session.close();
			}
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub
		if(value == null || value.equals("")){
			return "";
		} else {
			return ((PersonalInfo)value).getSTAFFCODE();
		}
	}

}
