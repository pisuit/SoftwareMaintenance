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
@Table(name="WORK_PLACE")
@GenericGenerator(strategy="th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class WorkPlace implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataStatus getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof WorkPlace){
			if(((WorkPlace)obj).getId().equals(id)){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
