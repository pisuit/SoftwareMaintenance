package th.co.aerothai.callservice.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import th.co.aerothai.callservice.model.hr.Department;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.model.hr.Photo;
import th.co.aerothai.callservice.utils.HibernateUtil;

public abstract class StaffDao {
	public static List<PersonalInfo> getEmployeeList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<PersonalInfo> personalList = session.createQuery(
					"SELECT distinct personal " +
					"FROM PersonalInfo personal " +
					"left join fetch personal.employeeInfos emp " +
					"WHERE emp != null " +
					"ORDER BY personal.TNAME ")
					.list();
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
	
	public static PersonalInfo getStaff(String empCode){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			PersonalInfo emp = (PersonalInfo) session.createQuery(
					"SELECT distinct personal " +
					"FROM PersonalInfo personal " +
					"left join fetch personal.employeeInfos emp " +
					"WHERE emp != null " +
					"AND personal.STAFFCODE = :pempcode")
					.setParameter("pempcode", StringUtils.leftPad(empCode, 6, '0'))
					.uniqueResult();
			
			tx.commit();
			
			return emp;
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
	
	@SuppressWarnings("unchecked")
	public static List<PersonalInfo> findEmployeeAutoComplete(String searchStr){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			String[] searchArr = null;
					String param = "";
			
					if(searchStr.contains(" ")){
						searchArr = searchStr.split(" ");
						
						
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
				query.setParameter("param0", "%"+searchStr+"%");
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
	
	public static Photo getPhoto(String staffCode){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Photo photo = (Photo) session.createQuery(
					"SELECT photo " +
					"FROM Photo photo " +
					"WHERE photo.staffCode = :pstaffcode ")
					.setParameter("pstaffcode", staffCode)
					.uniqueResult();
			
			tx.commit();
			return photo;
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
	
	@SuppressWarnings("unchecked")
	public static List<Department> getDepartments(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Department> depList = session.createQuery(
					"SELECT dep " +
					"FROM Department dep " +
					"WHERE dep.PFLAG = 'C' " +
					"AND dep.FFLAG = 'C' " +
					"ORDER BY dep.TDEPS")
					.list();
			
			tx.commit();
			
			return depList;
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
	
	public static Department findDepartment(Long sn){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Department department = (Department) session.createQuery(
					"select dep " +
					"from Department dep " +
					"where dep.SN = :psn")
					.setParameter("psn", sn)
					.uniqueResult();
			
			tx.commit();
			
			return department;
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
