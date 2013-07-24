package th.co.aerothai.callservice.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.security.identity.RoleType;
import org.joda.time.DateTime;

import th.co.aerothai.callservice.customtype.ApprovalType;
import th.co.aerothai.callservice.customtype.DataStatus;
import th.co.aerothai.callservice.customtype.IssueType;
import th.co.aerothai.callservice.customtype.StateType;
import th.co.aerothai.callservice.customtype.UserRole;
import th.co.aerothai.callservice.model.AssignedOperator;
import th.co.aerothai.callservice.model.Attachment;
import th.co.aerothai.callservice.model.CallServiceComment;
import th.co.aerothai.callservice.model.CallServiceJob;
import th.co.aerothai.callservice.model.InspectorComment;
import th.co.aerothai.callservice.model.ProjectManagerComment;
import th.co.aerothai.callservice.model.Job;
import th.co.aerothai.callservice.model.OperatorComment;
import th.co.aerothai.callservice.model.ProviderDirectorComment;
import th.co.aerothai.callservice.model.Request;
import th.co.aerothai.callservice.model.RequestLog;
import th.co.aerothai.callservice.model.UserDirectorComment;
import th.co.aerothai.callservice.model.hr.PersonalInfo;
import th.co.aerothai.callservice.utils.HibernateUtil;
import th.co.aerothai.callservice.utils.SVNUtils;
import th.co.aerothai.callservice.utils.SessionUtils;

public abstract class RequestDao {
	public static void saveRequest(Request request){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if(request.getId() != null){
				if(request.getDataStatus().equals(DataStatus.NORMAL)){
					session.saveOrUpdate(request);
					session.saveOrUpdate(new RequestLog(request, request.getRequester(), UserRole.REQUESTER, "Edit request", new Date()));
				} else {
					session.saveOrUpdate(request);
					if(request.getAttachmentList() != null){
						for(Attachment attach : request.getAttachmentList()){
							SVNUtils.deleteFile(attach.getLogicalName());
						}
					}
				}
			} else {
				session.saveOrUpdate(request);
				session.saveOrUpdate(new RequestLog(request, request.getRequester(), UserRole.REQUESTER, "Create request", new Date()));
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
	
	public static void saveRating(Request request){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(request);
			session.saveOrUpdate(new RequestLog(request, request.getRequester(), UserRole.REQUESTER, "Rating request", new Date()));
			
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
	
	public static void saveAttachment(Attachment attach){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(attach);
			
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
	
	public static void deleteAttachment(Attachment attach){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.delete(attach);
			
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
	
	public static void saveUserDirectorComment(Request request, UserDirectorComment comment){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			request.setStateType(StateType.DU_APPROVED);
			
			session.saveOrUpdate(request);
			session.saveOrUpdate(comment);
			
			if(comment.getApprovalType().equals(ApprovalType.APPROVED)){
				session.saveOrUpdate(new RequestLog(request, comment.getDirector(), UserRole.USER_DIRECTOR, "Approve request", new Date()));
			} else {
				session.saveOrUpdate(new RequestLog(request, comment.getDirector(), UserRole.USER_DIRECTOR, "Decline request", new Date()));
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
	
	public static void saveProviderDirectorComment(Request request, ProviderDirectorComment comment){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(comment);
			
//			if(!comment.getApprovalType().equals(ApprovalType.ACCEPTED) && !comment.getApprovalType().equals(ApprovalType.REJECTED)){
//				if(request.getStateType().equals(StateType.PM_D)){
//					request.setStateType(StateType.D_APPROVED_D);
//				} else if(request.getStateType().equals(StateType.PM_P)){
//					request.setStateType(StateType.D_APPROVED_P);
//				}
//				
//				session.saveOrUpdate(request);
//			}
			
			if(!comment.getApprovalType().equals(ApprovalType.ACCEPTED) && !comment.getApprovalType().equals(ApprovalType.REJECTED)){
				if(request.getIssueType().equals(IssueType.DEPARTMENT)){
					request.setStateType(StateType.D_APPROVED_D);
				} else {
					request.setStateType(StateType.D_APPROVED_P);
				}
			
				session.saveOrUpdate(request);
			}
			
			if(comment.getApprovalType().equals(ApprovalType.APPROVED)){
				session.saveOrUpdate(new RequestLog(request, comment.getDirector(), UserRole.PROVIDER_DIRECTOR, "Approve request", new Date()));
			} else if(comment.getApprovalType().equals(ApprovalType.ACCEPTED)){
				session.saveOrUpdate(new RequestLog(request, comment.getDirector(), UserRole.PROVIDER_DIRECTOR, "Accept request", new Date()));
			} else if(comment.getApprovalType().equals(ApprovalType.REJECTED)){
				session.saveOrUpdate(new RequestLog(request, comment.getDirector(), UserRole.PROVIDER_DIRECTOR, "Reject request", new Date()));
			} else if(comment.getApprovalType().equals(ApprovalType.DECLINED)){
				session.saveOrUpdate(new RequestLog(request, comment.getDirector(), UserRole.PROVIDER_DIRECTOR, "Decline request", new Date()));
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
	
	public static void saveProjectManagerComment(Request request, ProjectManagerComment comment, List<PersonalInfo> operators){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if(comment.getId() != null){
				session.saveOrUpdate(comment);
				session.saveOrUpdate(new RequestLog(request, comment.getManager(), UserRole.PROJECT_MANAGER, "Edit suggestion", new Date()));
			} else {
				session.saveOrUpdate(comment);
				session.saveOrUpdate(new RequestLog(request, comment.getManager(), UserRole.PROJECT_MANAGER, "Give suggestion", new Date()));
			}
			
			if(comment.getOperators() != null){
				for(AssignedOperator operator : comment.getOperators()){
					session.delete(operator);
				}
			}
			
			for(PersonalInfo staff : operators){
				AssignedOperator assign = new AssignedOperator();
				assign.setOperator(staff);
				assign.setProjectManagerComment(comment);
				session.saveOrUpdate(assign);
			}
			
//			if(request.getProjectManagerComment() == null){
//				if(request.getStateType().equals(StateType.CALL_SERVICE_P)){
//					request.setStateType(StateType.PM_P);
//				} else if(request.getStateType().equals(StateType.CALL_SERVICE_D)){
//					request.setStateType(StateType.PM_D);
//				}
//				
//				session.saveOrUpdate(request);
//			}
			
			if(request.getProjectManagerComment() == null){
				if(request.getIssueType().equals(IssueType.PERSONAL)){
					request.setStateType(StateType.PM_P);
				} else {
					request.setStateType(StateType.PM_D);
				}
				
				session.saveOrUpdate(request);
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
	
	public static void saveOperatorComment(Request request, OperatorComment comment){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if(comment.getId() != null){
				session.saveOrUpdate(comment);
				session.saveOrUpdate(new RequestLog(request, comment.getOperator(), UserRole.OPERATOR, "Edit operation detail", new Date()));
			} else {
				session.saveOrUpdate(comment);
				session.saveOrUpdate(new RequestLog(request, comment.getOperator(), UserRole.OPERATOR, "Give operation detail", new Date()));
			}
			
//			if(request.getOperatorComment() == null){
//				if(request.getStateType().equals(StateType.D_APPROVED_P)){
//					request.setStateType(StateType.OPERATE_P);
//				} else if(request.getStateType().equals(StateType.D_APPROVED_D)){
//					request.setStateType(StateType.OPERATE_D);
//				}
//				
//				session.saveOrUpdate(request);
//			}
			
			if(request.getOperatorComment() == null){
				if(request.getIssueType().equals(IssueType.PERSONAL)){
					request.setStateType(StateType.OPERATE_P);
				} else {
					request.setStateType(StateType.OPERATE_D);
				}
				
				session.saveOrUpdate(request);
			}
			
			if(request.getInspectorComment() != null && request.getInspectorComment().getApprovalType().equals(ApprovalType.REJECTED)){
				session.delete(request.getInspectorComment());
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
	
	public static void saveInspectorComment(Request request, InspectorComment comment){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(comment);
			
			if(comment.getApprovalType().equals(ApprovalType.ACCEPTED)){
				if(request.getIssueType().equals(IssueType.PERSONAL)){
					request.setStateType(StateType.INSPECT_P);
				} else {
					request.setStateType(StateType.INSPECT_D);
				}
				
				session.saveOrUpdate(request);
			} else {
				if(request.getIssueType().equals(IssueType.PERSONAL)){
					request.setStateType(StateType.OPERATE_P);
				} else {
					request.setStateType(StateType.OPERATE_D);
				}
				
				session.saveOrUpdate(request);
			}
			
			if(comment.getApprovalType().equals(ApprovalType.ACCEPTED)){
				session.saveOrUpdate(new RequestLog(request, comment.getInspector(), UserRole.INSPECTOR, "Accept request", new Date()));
			} else {
				session.saveOrUpdate(new RequestLog(request, comment.getInspector(), UserRole.INSPECTOR, "Reject request", new Date()));
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
	
	public static void saveCallServiceComment(Request request, CallServiceComment comment, List<Job> jobs){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if(comment.getId() != null){
				session.saveOrUpdate(comment);
				session.saveOrUpdate(new RequestLog(request, comment.getServiceOfficer(), UserRole.CALL_SERVICE, "Edit task classification", new Date()));
			} else {
				session.saveOrUpdate(comment);
				session.saveOrUpdate(new RequestLog(request, comment.getServiceOfficer(), UserRole.CALL_SERVICE, "Classify task", new Date()));
			}
			
			if(comment.getCallServiceJobs() != null){
				for(CallServiceJob cj : comment.getCallServiceJobs()){
					session.delete(cj);
				}
			}
			
			for(Job job : jobs){
				CallServiceJob callServiceJob = new CallServiceJob();
				callServiceJob.setCallServiceComment(comment);
				callServiceJob.setJob(job);
				session.saveOrUpdate(callServiceJob);
			}
			
//			if(request.getCallServiceComment() == null){
//				if(request.getStateType().equals(StateType.NEW_P)){
//					request.setStateType(StateType.CALL_SERVICE_P);
//				} else if(request.getStateType().equals(StateType.DU_APPROVED)){
//					request.setStateType(StateType.CALL_SERVICE_D);
//				}
//				
//				session.saveOrUpdate(request);
//			}
			
			if(request.getCallServiceComment() == null){
				if(request.getIssueType().equals(IssueType.PERSONAL)){
					request.setStateType(StateType.CALL_SERVICE_P);
				} else {
					request.setStateType(StateType.CALL_SERVICE_D);
				}
				
				session.saveOrUpdate(request);
			}
			
			if(request.getProviderDirectorComment() != null && request.getProviderDirectorComment().getApprovalType().equals(ApprovalType.REJECTED)){
				session.delete(request.getProviderDirectorComment());
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
	
	public static Request findRequest(Long id){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Request request = (Request) session.createQuery(
					"select request " +
					"from Request request " +
					"left join fetch request.callServiceComment callservice " +
					"left join fetch callservice.workPlace " +
					"left join fetch callservice.callServiceJobs calljob " +
					"left join fetch calljob.job j " +
					"left join fetch request.operatorComment operator " +
					"left join fetch request.requestLogs " +
					"left join fetch request.attachments " +
					"left join fetch request.projectManagerComment projectman " +
					"left join fetch projectman.operators " +
					"where request.id = :pid")
					.setParameter("pid", id)
					.uniqueResult();
			
			tx.commit();
			
			return request;
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
	
	public static Long countNotify(String role){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Long count = null;
			String dep = null;
			
			if(SessionUtils.getUserSession().getStaff().getEmployee().getDEPFINANCE().equals("½¨.")){
				dep = SessionUtils.getUserSession().getStaff().getEmployee().getDEPPERSON();
			} else {
				dep = SessionUtils.getUserSession().getStaff().getEmployee().getDEPFINANCE();
			}
			
			if(role.equals("userdirector")){
				count = (Long) session.createQuery(
						"select count(request) " +
						"from Request request " +
						"left join request.requester staff " +
						"left join staff.employeeInfos emp " +
						"where request.dataStatus = 'NORMAL' " +
						"and (request.issueType = 'DEPARTMENT') " +
						"and emp != null " +
						"and (emp.DEPFINANCE = :pdep OR emp.DEPPERSON = :pdep) ")
						.setParameter("pdep", dep)
						.uniqueResult();
//			} else if(role.equals("callservice")){
//				requests = session.createQuery(
//						"select distinct request " +
//						"from Request request " +
//						"left join fetch request.callServiceComment callservice " +
//						"left join fetch callservice.workPlace " +
//						"left join fetch callservice.callServiceJobs calljob " +
//						"left join fetch calljob.job " +
//						"left join fetch request.requester staff " +
//						"left join fetch staff.employeeInfos emp " +
//						"left join fetch request.userDirectorComment udcomment " +
//						"left join fetch request.requestLogs " +
//						"left join fetch request.attachments " +
//						"left join fetch request.projectManagerComment projectman " +
//						"left join fetch projectman.operators " +
//						"where request.dataStatus = 'NORMAL' " +
//						"and emp != null " +
//						"and ((request.issueType = 'DEPARTMENT' and udcomment != null and udcomment.approvalType = 'APPROVED') " +
//						"or request.issueType = 'PERSONAL') " +
//						"order by request.id desc" )
//						.list();
//			} else if(role.equals("projectmanager")){
//				requests = session.createQuery(
//						"select distinct request " +
//						"from Request request " +
//						"left join fetch request.callServiceComment callservice " +
//						"left join fetch callservice.workPlace " +
//						"left join fetch callservice.callServiceJobs calljob " +
//						"left join fetch calljob.job j " +
//						"left join fetch request.requester staff " +
//						"left join fetch staff.employeeInfos emp " +
//						"left join fetch request.providerDirectorComment provider " +
//						"left join fetch request.requestLogs " +
//						"left join fetch request.attachments " +
//						"left join fetch request.projectManagerComment projectman " +
//						"left join fetch projectman.operators " +
//						"where request.dataStatus = 'NORMAL' " +
//						"and emp != null " +
//						"and (provider != null " +
//						"and provider.approvalType != 'REJECTED') " +
//						"and j.department = :pdep " +
//						"order by request.id desc" )
//						.setParameter("pdep", dep)
//						.list();
//			} else if(role.equals("providerdirector")){
//				requests = session.createQuery(
//						"select distinct request " +
//						"from Request request " +
//						"left join fetch request.callServiceComment callservice " +
//						"left join fetch callservice.workPlace " +
//						"left join fetch callservice.callServiceJobs calljob " +
//						"left join fetch calljob.job j " +
//						"left join fetch request.requester staff " +
//						"left join fetch staff.employeeInfos emp " +
//						"left join fetch request.requestLogs " +
//						"left join fetch request.attachments " +
//						"left join fetch request.projectManagerComment projectman " +
//						"left join fetch projectman.operators " +
//						"where request.dataStatus = 'NORMAL' " +
//						"and emp != null " +
//						"and callservice != null " +
//						"and j.department = :pdep " +
//						"order by request.id desc" )
//						.setParameter("pdep", dep)
//						.list();
//			} else if(role.equals("operator")){
//				requests = session.createQuery(
//						"select distinct request " +
//						"from Request request " +
//						"left join fetch request.callServiceComment callservice " +
//						"left join fetch callservice.workPlace " +
//						"left join fetch callservice.callServiceJobs calljob " +
//						"left join fetch calljob.job " +
//						"left join fetch request.requester staff " +
//						"left join fetch staff.employeeInfos emp " +
//						"left join fetch request.providerDirectorComment director " +
//						"left join fetch request.projectManagerComment manager " +
//						"left join fetch request.requestLogs " +
//						"left join fetch request.attachments " +
//						"left join fetch request.projectManagerComment projectman " +
//						"left join fetch projectman.operators operator " +
//						"where request.dataStatus = 'NORMAL' " +
//						"and emp != null " +
//						"and (director != null " +
//						"and director.approvalType = 'APPROVED') " +
//						"and operator.operator = :poperator " +
//						"order by request.id desc" )
//						.setParameter("poperator", SessionUtils.getUserSession().getStaff())
//						.list();
//			} else if(role.equals("inspector")){
//				requests = session.createQuery(
//						"select distinct request " +
//						"from Request request " +
//						"left join fetch request.callServiceComment callservice " +
//						"left join fetch callservice.workPlace " +
//						"left join fetch callservice.callServiceJobs calljob " +
//						"left join fetch calljob.job j " +
//						"left join fetch request.requester staff " +
//						"left join fetch staff.employeeInfos emp " +
//						"left join fetch request.operatorComment operator " +
//						"left join fetch request.requestLogs " +
//						"left join fetch request.attachments " +
//						"left join fetch request.projectManagerComment projectman " +
//						"left join fetch projectman.operators " +
//						"where request.dataStatus = 'NORMAL' " +
//						"and emp != null " +
//						"and operator != null " +
//						"and j.department = :pdep " +
//						"order by request.id desc" )
//						.setParameter("pdep", dep)
//						.list();
			}
			
			tx.commit();
			
			return count;
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
	public static List<Request> getRequestList(String role){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Request> requests = null;
			String dep = null;
			
			if(SessionUtils.getUserSession().getStaff().getEmployee().getDEPFINANCE().equals("½¨.")){
				dep = SessionUtils.getUserSession().getStaff().getEmployee().getDEPPERSON();
			} else {
				dep = SessionUtils.getUserSession().getStaff().getEmployee().getDEPFINANCE();
			}
			
			if(role.equals("all")){
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job " +
						"where request.dataStatus = 'NORMAL' " +
						"order by request.id desc" )
						.list();
			} else if(role.equals("requesterp")){		
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job " +
						"left join fetch request.requester staff " +
						"left join fetch staff.employeeInfos emp " +
						"left join fetch request.requestLogs " +
						"left join fetch request.attachments " +
						"left join fetch request.projectManagerComment projectman " +
						"left join fetch projectman.operators " +
						"where request.dataStatus = 'NORMAL' " +
						"and emp != null " +
						"and request.requester = :prequester " +
						"order by request.id desc" )
						.setParameter("prequester", SessionUtils.getUserSession().getStaff())
						.list();
			} else if(role.equals("requesterd")){		
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job " +
						"left join fetch request.requester staff " +
						"left join fetch staff.employeeInfos emp " +
						"left join fetch request.requestLogs " +
						"left join fetch request.attachments " +
						"left join fetch request.projectManagerComment projectman " +
						"left join fetch projectman.operators " +
						"where request.dataStatus = 'NORMAL' " +
						"and emp != null " +
						"and (emp.DEPFINANCE = :pdep OR emp.DEPPERSON = :pdep) " +
						"order by request.id desc" )
						.setParameter("pdep", dep)
						.list();
			} else if(role.equals("userdirector")){
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job " +
						"left join fetch request.requester staff " +
						"left join fetch staff.employeeInfos emp " +
						"left join fetch request.requestLogs " +
						"left join fetch request.attachments " +
						"left join fetch request.projectManagerComment projectman " +
						"left join fetch projectman.operators " +
						"where request.dataStatus = 'NORMAL' " +
						"and request.issueType = 'DEPARTMENT' " +
						"and emp != null " +
						"and (emp.DEPFINANCE = :pdep OR emp.DEPPERSON = :pdep) " +
						"order by request.id desc" )
						.setParameter("pdep", dep)
						.list();
			} else if(role.equals("callservice")){
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job " +
						"left join fetch request.requester staff " +
						"left join fetch staff.employeeInfos emp " +
						"left join fetch request.userDirectorComment udcomment " +
						"left join fetch request.requestLogs " +
						"left join fetch request.attachments " +
						"left join fetch request.projectManagerComment projectman " +
						"left join fetch projectman.operators " +
						"where request.dataStatus = 'NORMAL' " +
						"and emp != null " +
						"and ((request.issueType = 'DEPARTMENT' and udcomment != null and udcomment.approvalType = 'APPROVED') " +
						"or request.issueType = 'PERSONAL') " +
						"order by request.id desc" )
						.list();
			} else if(role.equals("projectmanager")){
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job j " +
						"left join fetch request.requester staff " +
						"left join fetch staff.employeeInfos emp " +
						"left join fetch request.providerDirectorComment provider " +
						"left join fetch request.requestLogs " +
						"left join fetch request.attachments " +
						"left join fetch request.projectManagerComment projectman " +
						"left join fetch projectman.operators " +
						"where request.dataStatus = 'NORMAL' " +
						"and emp != null " +
						"and (provider != null " +
						"and provider.approvalType != 'REJECTED') " +
						"and j.department = :pdep " +
						"order by request.id desc" )
						.setParameter("pdep", dep)
						.list();
			} else if(role.equals("providerdirector")){
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job j " +
						"left join fetch request.requester staff " +
						"left join fetch staff.employeeInfos emp " +
						"left join fetch request.requestLogs " +
						"left join fetch request.attachments " +
						"left join fetch request.projectManagerComment projectman " +
						"left join fetch projectman.operators " +
						"where request.dataStatus = 'NORMAL' " +
						"and emp != null " +
						"and callservice != null " +
						"and j.department = :pdep " +
						"order by request.id desc" )
						.setParameter("pdep", dep)
						.list();
			} else if(role.equals("operator")){
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job " +
						"left join fetch request.requester staff " +
						"left join fetch staff.employeeInfos emp " +
						"left join fetch request.providerDirectorComment director " +
						"left join fetch request.projectManagerComment manager " +
						"left join fetch request.requestLogs " +
						"left join fetch request.attachments " +
						"left join fetch request.projectManagerComment projectman " +
						"left join fetch projectman.operators operator " +
						"where request.dataStatus = 'NORMAL' " +
						"and emp != null " +
						"and (director != null " +
						"and director.approvalType = 'APPROVED') " +
						"and operator.operator = :poperator " +
						"order by request.id desc" )
						.setParameter("poperator", SessionUtils.getUserSession().getStaff())
						.list();
			} else if(role.equals("inspector")){
				requests = session.createQuery(
						"select distinct request " +
						"from Request request " +
						"left join fetch request.callServiceComment callservice " +
						"left join fetch callservice.workPlace " +
						"left join fetch callservice.callServiceJobs calljob " +
						"left join fetch calljob.job j " +
						"left join fetch request.requester staff " +
						"left join fetch staff.employeeInfos emp " +
						"left join fetch request.operatorComment operator " +
						"left join fetch request.requestLogs " +
						"left join fetch request.attachments " +
						"left join fetch request.projectManagerComment projectman " +
						"left join fetch projectman.operators " +
						"where request.dataStatus = 'NORMAL' " +
						"and emp != null " +
						"and operator != null " +
						"and j.department = :pdep " +
						"order by request.id desc" )
						.setParameter("pdep", dep)
						.list();
			}
			
			tx.commit();
			
			return requests;
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
	
	public static ProjectManagerComment getProjectManagerComment(Request request){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			ProjectManagerComment comment = (ProjectManagerComment) session.createQuery(
					"select comment " +
					"from ProjectManagerComment comment " +
					"left join fetch comment.operators " +
					"where comment.dataStatus = 'NORMAL' " +
					"and comment.request = :prequest ")
					.setParameter("prequest", request)
					.uniqueResult();
			
			tx.commit();		
					
			return comment;
		}  catch (Exception e) {
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
	
	public static int getNextRequestNumber(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Integer maxFormNumber = (Integer) session.createQuery(
					"select max(request.requestNumber) " +
					"from Request request")
					.uniqueResult();
			
			tx.commit();
			
			if (maxFormNumber == null ){
				maxFormNumber = Integer.parseInt(Integer.toString(DateTime.now().getYear())+"0001");
			} else {
				if(!Integer.toString(DateTime.now().getYear()).equals(StringUtils.substring(Integer.toString(maxFormNumber), 0, 4))){
					maxFormNumber = Integer.parseInt(Integer.toString(DateTime.now().getYear())+"0001");
				} else {
					maxFormNumber = Integer.valueOf(maxFormNumber.intValue()+1);
				}			
			}
			
			return maxFormNumber;
			
		}  catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return 0;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static String getPhoneNumber(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Request request = (Request) session.createQuery(
					"select request " +
					"from Request request " +
					"where request.id = " +
					"	(select max(req.id) " +
					"	from Request req " +
					"	where req.requester = :prequester " +
					"	and req.dataStatus = 'NORMAL') ")
					.setParameter("prequester", SessionUtils.getUserSession().getStaff())
					.uniqueResult();
			
			tx.commit();		
			
			if(request != null){
				return request.getPhoneNumber();
			} else {
				return "";
			}
						
		}  catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return "";
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static String getCoorPhoneNumber(PersonalInfo coordinator){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			CallServiceComment comment = (CallServiceComment) session.createQuery(
					"select comment " +
					"from CallServiceComment comment " +
					"where comment.id = " +
					"	(select max(com.id) " +
					"	from CallServiceComment com " +
					"	where com.dataStatus = 'NORMAL' " +
					"	and com.coordinator = :pcoordinator) ")
					.setParameter("pcoordinator", coordinator)
					.uniqueResult();
			
			tx.commit();		
			
			if(comment != null){
				return comment.getPhoneNumber();
			} else {
				return "";
			}
						
		}  catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return "";
		} finally {
			session.clear();
			session.close();
		}
	}
}
