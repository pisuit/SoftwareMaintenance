package th.co.aerothai.callservice.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import th.co.aerothai.callservice.model.Job;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.WorkPlace;
import th.co.aerothai.callservice.utils.HibernateUtil;

public abstract class GenericDao {
	public static List<WorkPlace> getWorkPlaceList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<WorkPlace> workPlaceList = session.createQuery(
					"select workplace " +
					"from WorkPlace workplace " +
					"where workplace.dataStatus = 'NORMAL'")
					.list();
			
			tx.commit();
			
			return workPlaceList;
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
	
	public static List<Job> getInternalJobList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Job> joblist = session.createQuery(
					"select job " +
					"from Job job " +
					"where job.dataStatus = 'NORMAL' " +
					"and job.internal = true ")
					.list();
			
			tx.commit();
			
			return joblist;
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
	
	public static List<Job> getExternalJobList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Job> joblist = session.createQuery(
					"select job " +
					"from Job job " +
					"where job.dataStatus = 'NORMAL' " +
					"and job.internal = false ")
					.list();
			
			tx.commit();
			
			return joblist;
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
	
	public static List<Job> getJobList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Job> joblist = session.createQuery(
					"select job " +
					"from Job job " +
					"where job.dataStatus = 'NORMAL' ")
					.list();
			
			tx.commit();
			
			return joblist;
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
	
	public static WorkPlace findWorkPlace(Long id){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			WorkPlace workPlace = (WorkPlace) session.createQuery(
					"select workplace " +
					"from WorkPlace workplace " +
					"where workplace.id = :pid")
					.setParameter("pid", id)
					.uniqueResult();

			tx.commit();
			return workPlace;
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
	
	public static Job findJob(Long id){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Job job = (Job) session.createQuery(
					"select job " +
					"from Job job " +
					"where job.id = :pid")
					.setParameter("pid", id)
					.uniqueResult();
			
			tx.commit();
			
			return job;
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
	
	public static void saveJob(Job job){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(job);
			
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
	
	public static void saveWorkPlace(WorkPlace work){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(work);
			
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
	
	public static void deleteJob(Job job){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.delete(job);
			
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
	
	public static void deleteWorkPlace(WorkPlace work){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.delete(work);
			
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
