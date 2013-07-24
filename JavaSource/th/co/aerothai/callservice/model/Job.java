package th.co.aerothai.callservice.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import th.co.aerothai.callservice.customtype.DataStatus;

@Entity
@Table(name="JOB")
@GenericGenerator(strategy="th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Job implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="JOB_NAME")
	private String jobName;
	
	@Column(name="INTERNAL")
	private boolean internal = false;
	
	@Column(name="DEPARTMENT")
	private String department;
	
	@Column(name="DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public DataStatus getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Job){
			if(((Job)obj).getId().equals(id)){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
