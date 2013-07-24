package th.co.aerothai.callservice.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import th.co.aerothai.callservice.customtype.UserRole;
import th.co.aerothai.callservice.model.Authorization;
import th.co.aerothai.callservice.model.SystemUser;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.utils.HibernateUtil;
import th.co.aerothai.callservice.utils.SessionUtils;

public abstract class UserDao {
	public static SystemUser getUser(String staffcode){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			SystemUser user = (SystemUser) session.createQuery(
					"SELECT distinct sysuser " +
					"FROM SystemUser sysuser " +
					"left join fetch sysuser.staff staff " +
					"left join fetch staff.employeeInfos emp " +
					"left join fetch sysuser.roles " +
					"WHERE emp != null " +
					"AND staff.STAFFCODE = :pstaffcode " +
					"and sysuser.boss is null ")
					.setParameter("pstaffcode", staffcode)
					.uniqueResult();
			tx.commit();
			
			return user;
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
	
	public static SystemUser getUserWithBoss(String staffcode){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			SystemUser user = (SystemUser) session.createQuery(
					"SELECT distinct sysuser " +
					"FROM SystemUser sysuser " +
					"left join fetch sysuser.staff staff " +
					"left join fetch staff.employeeInfos emp " +
					"left join fetch sysuser.roles " +
					"WHERE emp != null " +
					"AND staff.STAFFCODE = :pstaffcode " +
					"and sysuser.boss is not null ")
					.setParameter("pstaffcode", staffcode)
					.uniqueResult();
			tx.commit();
			
			return user;
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
	
	public static SystemUser getRep(String staffcode){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			SystemUser user = (SystemUser) session.createQuery(
					"SELECT distinct sysuser " +
					"FROM SystemUser sysuser " +
					"left join fetch sysuser.staff staff " +
					"left join fetch staff.employeeInfos emp " +
					"left join fetch sysuser.roles " +
					"WHERE emp != null " +
					"and sysuser.boss = :pboss ")
					.setParameter("pboss", staffcode)
					.uniqueResult();
			tx.commit();
			
			return user;
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
	
	public static List<SystemUser> getRepList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<SystemUser> users = session.createQuery(
					"SELECT distinct sysuser " +
					"FROM SystemUser sysuser " +
					"left join fetch sysuser.staff staff " +
					"left join fetch staff.employeeInfos emp " +
					"left join fetch sysuser.roles " +
					"WHERE emp != null " +
					"and sysuser.boss = :pboss ")
					.setParameter("pboss", SessionUtils.getUserSession().getStaff())
					.list();
			tx.commit();
			
			return users;
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
	
	public static List<SystemUser> getUserList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<SystemUser> users = session.createQuery(
					"SELECT distinct sysuser " +
					"FROM SystemUser sysuser " +
					"left join fetch sysuser.staff staff " +
					"left join fetch staff.employeeInfos emp " +
					"left join fetch sysuser.roles " +
					"WHERE emp != null " +
					"and sysuser.boss is null ")
					.list();
			tx.commit();
			
			return users;
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
	
	public static void saveUser(SystemUser user, List<UserRole> roles){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(user);
			
			if(user.getRoles() != null){
				for(Authorization auth : user.getRoles()){
					session.delete(auth);
				}
			}
			
			if(roles != null){
				for(UserRole role : roles){
					Authorization authorization = new Authorization();
					authorization.setUser(user);
					authorization.setUserRole(role);
					session.saveOrUpdate(authorization);
				}
			}
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static void deleteUser(SystemUser user){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();	
			
			if(user.getRoles() != null){
				for(Authorization auth : user.getRoles()){
					session.delete(auth);
				}
			}
			
			session.delete(user);
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.clear();
			session.close();
		}
	}
}
