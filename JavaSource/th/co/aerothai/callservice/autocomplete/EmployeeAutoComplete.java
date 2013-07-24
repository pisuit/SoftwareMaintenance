package th.co.aerothai.callservice.autocomplete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import th.co.aerothai.callservice.dao.StaffDao;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.utils.HibernateUtil;

@ManagedBean(name="empAutoComplete")
@RequestScoped
public class EmployeeAutoComplete {
		
	public List<PersonalInfo> employeeCompleteMethod(String input) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			String[] searchArr = null;
					String param = "";
			
					if(input.contains(" ")){
						searchArr = input.split(" ");
						
						
						for (int i=0; i<searchArr.length; i++)   {
							String string =  ":param" + i;
							param += "and (personal.TNAME like "+string+
									" or personal.TSURNAME like "+string+")";
						} 
					} else {
						param = "and (personal.TNAME like :param0"+
								" or personal.TSURNAME like :param0)";
					}
			
			Query query = session.createQuery(
					"SELECT distinct personal " +
					"FROM PersonalInfo personal " +
					"left join fetch personal.employeeInfos emp " +
					"WHERE emp != null " + param+ 			 
					" ORDER BY personal.TNAME ");
		
			if(searchArr != null){
				for (int i=0; i<searchArr.length; i++)   {
					String string = "param" + i;
					query.setParameter(string, "%"+searchArr[i]+"%");
				}
			}else {
				query.setParameter("param0", "%"+input+"%");
			}
			
			List<PersonalInfo> personalList = query.list();
			
			tx.commit();
			
			return personalList;
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
